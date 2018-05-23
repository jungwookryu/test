package com.ht.connected.home.backend.sip.message.service;

import java.beans.Introspector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.sip.message.model.dto.SipMqttRequestMessageDto;


@Service
public class SipMqttSubscribeService {

    private static final Log LOGGER = LogFactory.getLog(SipMqttSubscribeService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SipUserService userService;
    
    @Autowired
    private SipMqttPublishService mqttPublishService;
    
    @Autowired
    private SipDeviceService deviceService;
    
    
    public void user(SipMqttRequestMessageDto request) {
        if (request.getCrudType().equals("add")) {
            String userId = request.getBody().get("userID").toString();
            String userPassword = request.getBody().get("userPassword").toString();
            String userNickname = request.getBody().get("userAlias").toString();
            userService.addUser(userId, userPassword, userNickname);
            mqttPublishService.publish(request, null);
        }
    }
    
    public void device(SipMqttRequestMessageDto request) {
        if (request.getCrudType().equals("add")) {
            deviceService.addDevice(request);
            mqttPublishService.publish(request, null);
        }else if(request.getCrudType().equals("delete")) {
            deviceService.deleteDevice(request);
            mqttPublishService.publish(request, null);
        }
    }
    
    public void subscribe(Message<?> message) {
        SipMqttRequestMessageDto request = null;
        String topic = String.valueOf(message.getHeaders().get("mqtt_topic"));
        String payload = String.valueOf(message.getPayload());
        LOGGER.info("messageArrived: Topic=" + topic + ", Payload=" + payload);
        try {
            request = objectMapper.readValue(payload, SipMqttRequestMessageDto.class);
            if (!request.getMethod().equals("Z-Wave G/W")) {
                request.setTopic(topic);
                String method = Introspector.decapitalize(request.getMethod());
                SipMqttSubscribeService.class.getMethod(method, SipMqttRequestMessageDto.class).invoke(this, request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
