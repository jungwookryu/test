package com.ht.connected.home.backend.category.zwave.notification;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser;
import com.ht.connected.home.backend.category.zwave.ZWave;
import com.ht.connected.home.backend.category.zwave.ZWaveRepository;
import com.ht.connected.home.backend.category.zwave.ZWaveRequest;
import com.ht.connected.home.backend.category.zwave.ZWaveServiceImpl;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.AlarmCommandClass;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.BinarySwitchCommandClass;
import com.ht.connected.home.backend.category.zwave.endpoint.Endpoint;
import com.ht.connected.home.backend.category.zwave.endpoint.EndpointRepository;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.common.MqttCommon;
import com.ht.connected.home.backend.controller.mqtt.ProducerComponent;
import com.ht.connected.home.backend.gateway.Gateway;
import com.ht.connected.home.backend.gateway.GatewayRepository;
import com.ht.connected.home.backend.service.mqtt.Target;
import com.rabbitmq.tools.json.JSONWriter;

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
        RequestNotification result_data = objectMapper.readValue(payload, RequestNotification.class);
        
        RequestNotification requestNotification = objectMapper.convertValue(result_data.getResult_data(), RequestNotification.class);;
        String deviceTypeCode = endpoint.getGeneric() +"."+ endpoint.getSpecific();
        String functionCode = AlarmCommandClass.functionCode;
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
        String functionCode = BinarySwitchCommandClass.functionCode;
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
        String deviceType = Common.zwaveNickname(zWaveProperties, notification.getDevice_type_code());
        String functionName = zWaveFunctionProperties.getProperty(notification.getFunction_code());
        notification.setDeviceTypeName(deviceType);
        notification.setFunctionName(functionName);
        Notification selectNotificaiton = notificationRepository.findByNotificationCodeAndEndpointNo(notification.getNotification_code(), notification.getEndpoint_no());
        if(!Objects.isNull(selectNotificaiton)) {
            notification.setNo(selectNotificaiton.get_no());
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
//        zwave.device.status=/server/{target}/{model}/{serial}/zwave/device/
        
        ObjectMapper objectMapper1 = new ObjectMapper(); 
        String stnNotification = objectMapper1.writerWithDefaultPrettyPrinter().writeValueAsString(rtnNotification);
//        String stnNotification =         gson.toJson(rtnNotification);
        MqttCommon.publishNotificationData(producerRestController, callbackAckProperties, "zwave.device.status", Target.app.name()
                , zwaveRequest.getModel(), zwaveRequest.getSerialNo(), stnNotification);
    }

    @Override
    public void delete(ZWave zwave) {
        notificationRepository.deleteByZwaveNo(zwave.getNo());
    }
    
    @Override
    public List<Notification> getNotification(Endpoint endpoint) {
        return notificationRepository.findByEndpointNo(endpoint.getNo());
    }
    
}
