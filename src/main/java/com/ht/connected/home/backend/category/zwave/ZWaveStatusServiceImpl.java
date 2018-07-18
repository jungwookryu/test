package com.ht.connected.home.backend.category.zwave;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.category.zwave.constants.commandclass.AlarmCommandClass;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.BasicCommandClass;
import com.ht.connected.home.backend.category.zwave.endpoint.Endpoint;
import com.ht.connected.home.backend.category.zwave.endpoint.EndpointRepository;
import com.ht.connected.home.backend.category.zwave.notification.Notification;
import com.ht.connected.home.backend.category.zwave.notification.NotificationRepository;
import com.ht.connected.home.backend.category.zwave.notification.RequestNotification;
import com.ht.connected.home.backend.common.MqttCommon;
import com.ht.connected.home.backend.controller.mqtt.Message;
import com.ht.connected.home.backend.controller.mqtt.ProducerComponent;
import com.ht.connected.home.backend.service.mqtt.Target;
@Service
public class ZWaveStatusServiceImpl implements ZWaveStatusService {
    
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
    ProducerComponent producerRestController;
    
    private static final Log logger = LogFactory.getLog(ZWaveServiceImpl.class);
    
    @Override
    public void subscribe(ZWaveRequest zwaveRequest, String payload) throws JsonGenerationException, JsonMappingException, IOException, InterruptedException {
        if ((zwaveRequest.getClassKey() == BasicCommandClass.INT_ID) && (zwaveRequest.getCommandKey() == BasicCommandClass.INT_BASIC_REPORT)) {
            basicReport(zwaveRequest, payload);
        }else if ((zwaveRequest.getClassKey() == BasicCommandClass.INT_ID) && (zwaveRequest.getCommandKey() == BasicCommandClass.INT_BASIC_REPORT)) {
            notificationReport(zwaveRequest, payload);
        }
    }
    
    private void basicReport(ZWaveRequest zwaveRequest, String payload) throws JsonGenerationException, JsonMappingException, IOException, InterruptedException {
        int endpointId = zwaveRequest.getEndpointId();
        Endpoint endpoint = endpointRepository.findOne(endpointId);
        RequestNotification requestNotification = objectMapper.convertValue(payload, RequestNotification.class);
        String deviceTypeCode =endpoint.getGeneric()+endpoint.getSpecific();
        Notification notification = 
                new Notification(requestNotification.getNotificationType(), requestNotification.getMevent()
                , requestNotification.getSequence(), deviceTypeCode, endpoint.getZwaveNo(), endpoint.getNo());
        logger.debug("notification::"+notification.toString());
        Notification rtnNotification = saveNotification(notification);
//        zwave.device.status=/server/{target}/{model}/{serial}/zwave/device/{endpoint_no}/{sequence}
        String topic = callbackAckProperties.getProperty("zwave.device.status");
        topic.replace(MqttCommon.STATIC_TARGET, zwaveRequest.getTarget());
        topic.replace(MqttCommon.STATIC_MODEL, zwaveRequest.getModel());
        topic.replace(MqttCommon.STATIC_SERIAL, zwaveRequest.getSerialNo());
        topic.replace(MqttCommon.STATIC_ENDPOINT_NO, Integer.toString(endpoint.getNo()));
        String messgeBpody = objectMapper.writeValueAsString(notification);
        ;
        producerRestController.run(new Message(topic, messgeBpody));
        
    }
    private void notificationReport(ZWaveRequest zwaveRequest, String payload) throws JsonGenerationException, JsonMappingException, IOException, InterruptedException {
        int endpointId = zwaveRequest.getEndpointId();
        Endpoint endpoint = endpointRepository.findOne(endpointId);
        RequestNotification requestNotification = objectMapper.convertValue(payload, RequestNotification.class);
        String deviceTypeCode =endpoint.getGeneric()+endpoint.getSpecific();
        Notification notification = 
                new Notification(requestNotification.getNotificationType(), requestNotification.getMevent()
                , requestNotification.getSequence(), deviceTypeCode, endpoint.getZwaveNo(), endpoint.getNo());
        logger.debug("notification::"+notification.toString());
        Notification rtnNotification = saveNotification(notification);
//        zwave.device.status=/server/{target}/{model}/{serial}/zwave/device/{endpoint_no}/{sequence}
        String topic = callbackAckProperties.getProperty("zwave.device.status");
        topic.replace(MqttCommon.STATIC_TARGET, zwaveRequest.getTarget());
        topic.replace(MqttCommon.STATIC_MODEL, zwaveRequest.getModel());
        topic.replace(MqttCommon.STATIC_SERIAL, zwaveRequest.getSerialNo());
        topic.replace(MqttCommon.STATIC_ENDPOINT_NO, Integer.toString(endpoint.getNo()));
        String messgeBpody = objectMapper.writeValueAsString(notification);
        ;
        producerRestController.run(new Message(topic, messgeBpody));
        
    }
    public Notification saveNotification(Notification notification) {
        String deviceType = zWaveFunctionProperties.getProperty(notification.getDeviceTypeCode());
        notification.setDeviceTypeName(deviceType);
        return notificationRepository.save(notification);
    }
    
}
