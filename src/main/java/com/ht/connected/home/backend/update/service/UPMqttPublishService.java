package com.ht.connected.home.backend.update.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.config.service.MqttConfig;

@Service
public class UPMqttPublishService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UPMqttPublishService.class);

    @Autowired
    private MqttConfig.MqttGateway mqttGateway;

    @Autowired
    @Qualifier(value = "MqttOutbound")
    private MqttPahoMessageHandler messageHandler;

    
    @Async
    public void publish(String topic, String payload) {
        messageHandler.setDefaultTopic(topic);
        mqttGateway.sendToMqtt(payload);
        LOGGER.info(String.format("=== MQTT Update Message server published Topic: %s ===", topic));
        LOGGER.info(String.format("=== MQTT Update Message server published Message: %s ===", payload.toString()));
    }    

}
