package com.ht.connected.home.backend.sip.message.service;

import static java.util.Objects.isNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ht.connected.home.backend.MqttConfig;
import com.ht.connected.home.backend.sip.message.model.dto.SipMqttRequestMessageDto;
import com.ht.connected.home.backend.sip.message.model.dto.SipMqttResponseMessageDto;

@Service
public class SipMqttPublishService {

    private static final Log LOGGER = LogFactory.getLog(SipMqttPublishService.class);

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
