package com.ht.connected.home.backend.sip.message.service;

import static java.util.Objects.isNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ht.connected.home.backend.config.service.MqttConfig;
import com.ht.connected.home.backend.sip.message.model.dto.SipMqttRequestMessageDto;
import com.ht.connected.home.backend.sip.message.model.dto.SipMqttResponseMessageDto;

@Service
public class SipMqttPublishService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SipMqttPublishService.class);

    @Autowired
    private MqttConfig.MqttGateway mqttGateway;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier(value = "MqttOutbound")
    private MqttPahoMessageHandler messageHandler;

    public void publish(SipMqttRequestMessageDto request, Object body) {
        SipMqttResponseMessageDto response = new SipMqttResponseMessageDto();
        response.setMethod(request.getMethod());
        response.setCrudType(request.getCrudType());
        response.setResult(request.getResult());
        ObjectNode resp = objectMapper.valueToTree(response);
        if (!isNull(body)) {
            resp.put("Body", objectMapper.valueToTree(body));
        }
        messageHandler.setDefaultTopic(request.getResponseTopic());
        try {
            String payload = objectMapper.writeValueAsString(resp);
            mqttGateway.sendToMqtt(payload);
            LOGGER.info(String.format("=== MQTT published Topic: %s ===", request.getResponseTopic()));
            LOGGER.info(String.format("=== MQTT published Message: %s ===", payload.toString()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
