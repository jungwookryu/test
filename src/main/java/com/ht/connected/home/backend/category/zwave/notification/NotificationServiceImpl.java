package com.ht.connected.home.backend.category.zwave.notification;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.ht.connected.home.backend.category.zwave.ZWave;
import com.ht.connected.home.backend.category.zwave.ZWaveRepository;
import com.ht.connected.home.backend.category.zwave.ZWaveRequest;
import com.ht.connected.home.backend.category.zwave.ZWaveServiceImpl;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.AlarmCommandClass;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.BasicCommandClass;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.BinarySwitchCommandClass;
import com.ht.connected.home.backend.category.zwave.endpoint.Endpoint;
import com.ht.connected.home.backend.category.zwave.endpoint.EndpointRepository;
import com.ht.connected.home.backend.common.ByteUtil;
import com.ht.connected.home.backend.common.MqttCommon;
import com.ht.connected.home.backend.controller.mqtt.Message;
import com.ht.connected.home.backend.controller.mqtt.ProducerComponent;
import com.ht.connected.home.backend.gateway.Gateway;
import com.ht.connected.home.backend.gateway.GatewayRepository;
import com.ht.connected.home.backend.service.mqtt.Target;

import javapns.test.NotificationTest;

@Service
public class NotificationServiceImpl implements NotificationService {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    EndpointRepository endpointRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    @Qualifier(value = "callbackAckProperties")
    Properties callbackAckProperties;

    @Autowired
    @Qualifier("zWaveFunctionProperties")
    Properties zWaveFunctionProperties;
    
    @Autowired
    @Qualifier("zWaveProperties")
    Properties zWaveProperties;

    @Autowired
    ProducerComponent producerRestController;

    @Autowired
    GatewayRepository gatewayRepository;

    @Autowired
    ZWaveRepository zWaveRepository;

    private static final Log logger = LogFactory.getLog(ZWaveServiceImpl.class);

    @Override
    public void subscribe(ZWaveRequest zwaveRequest, String payload) throws JsonGenerationException, JsonMappingException, IOException, InterruptedException {
        if ((zwaveRequest.getClassKey() == BinarySwitchCommandClass.INT_ID) && (zwaveRequest.getCommandKey() == BinarySwitchCommandClass.INT_SWITCH_BINARY_REPORT)) {
            binaryReport(zwaveRequest, payload);
        } else if ((zwaveRequest.getClassKey() == AlarmCommandClass.INT_ID) && (zwaveRequest.getCommandKey() == AlarmCommandClass.INT_ALARM_REPORT)) {
            notificationReport(zwaveRequest, payload);
        }
    }

    private void notificationReport(ZWaveRequest zwaveRequest, String payload) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
        
        Endpoint endpoint = getEndpointInfo(zwaveRequest);
        RequestNotification requestNotification = objectMapper.readValue(payload, RequestNotification.class);
        String deviceTypeCode = endpoint.getGeneric() +"."+ endpoint.getSpecific();
        String functionCode = Integer.toString(AlarmCommandClass.INT_ID);
        functionCode = String.format("%2s", Integer.toString(AlarmCommandClass.INT_ID));
        Notification notification = new Notification(requestNotification.getNotificationType(), requestNotification.getMevent(), requestNotification.getSequence(), deviceTypeCode,
                endpoint.getZwaveNo(), endpoint.getNo());
        notification.setFunctionCode(functionCode);
        logger.debug("notification::" + notification.toString());
        Notification rtnNotification = saveNotification(notification);
        publishAppStatus(zwaveRequest, endpoint.getNo(), rtnNotification);

    }

    private void binaryReport(ZWaveRequest zwaveRequest, String payload) throws JsonGenerationException, JsonMappingException, IOException, InterruptedException {
        
        Endpoint endpoint = getEndpointInfo(zwaveRequest);
        Notification notification = new Notification();
        // binary Switch report
        RequestNotification requestNotification = objectMapper.readValue(payload, RequestNotification.class);
        int value = (int)requestNotification.getResult_data().getOrDefault("value", -1);
        String deviceTypeCode = endpoint.getGeneric() +"."+ endpoint.getSpecific();
        String functionCode = Integer.toString(BinarySwitchCommandClass.INT_ID);
        functionCode = String.format("%2s", Integer.toString(AlarmCommandClass.INT_ID));
        notification = new Notification(BinarySwitchCommandClass.MEVENT, BinarySwitchCommandClass.getNotificationCode(value), BinarySwitchCommandClass.DEFAULT_SEQUENCE, deviceTypeCode, endpoint.getZwaveNo(), endpoint.getNo());
        notification.setFunctionCode(functionCode);
        Notification rtnNotification = saveNotification(notification);
        publishAppStatus(zwaveRequest, endpoint.getNo(), rtnNotification);
        logger.debug("notification::" + rtnNotification.toString());
    }

    /**
     * save notification info
     * entity index unique : endpoint_no, notification_type
     * @param notification
     * @author ijlee
     * @since 07 19 2018
     * @return
     */
    public Notification saveNotification(Notification notification) {
        String deviceType = zWaveProperties.getProperty(notification.getDeviceTypeCode());
        String functionType = zWaveFunctionProperties.getProperty(notification.getFunctionCode());
        Notification selectNotificaiton = notificationRepository.findByNotificationCodeAndEndpointNo(notification.getNotificationCode(), notification.getEndpointNo());
        if(Objects.isNull(selectNotificaiton)) {
            notification.setDeviceTypeName(deviceType);
        }else {
            notification.setNo(selectNotificaiton.getNo());
        }
        return notificationRepository.save(notification);
    }

    private Endpoint getEndpointInfo(ZWaveRequest zwaveRequest) {
        Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
        List<ZWave> zwaves = zWaveRepository.findByGatewayNoAndNodeId(gateway.getNo(), zwaveRequest.getNodeId());
        int endpointId = zwaveRequest.getEndpointId();
        Endpoint endpoint = endpointRepository.findByZwaveNoAndEpid(zwaves.get(0).getNo(),endpointId);
        return endpoint;
    }

    private void publishAppStatus(ZWaveRequest zwaveRequest, int no, Notification rtnNotification) throws JsonGenerationException, JsonMappingException, IOException, InterruptedException {
//        zwave.device.status=/server/{target}/{model}/{serial}/zwave/device/{endpoint_no}/{sequence}
        String topic = callbackAckProperties.getProperty("zwave.device.status");
        String exeTopic = MqttCommon.rtnCallbackAck(topic, Target.app.name(), zwaveRequest.getModel(),  zwaveRequest.getSerialNo());
        exeTopic = exeTopic.replace("{endpoint_no}",ByteUtil.getHexString(rtnNotification.getEndpointNo()));
        exeTopic = exeTopic.replace("{sequence}",ByteUtil.getHexString(rtnNotification.getSequence()));
        String messgeBody = objectMapper.writeValueAsString(rtnNotification);
        producerRestController.run(new Message(exeTopic, messgeBody));
    }

    @Override
    public void delete(ZWave zwave) {
        notificationRepository.deleteByZwaveNo(zwave.getNo());
    }
    
}
