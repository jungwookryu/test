package com.ht.connected.home.backend.gov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.MqttConfig;
import com.ht.connected.home.backend.gatewayCategory.CategoryActive;
import com.ht.connected.home.backend.service.mqtt.Target;

@Service
public class GovService {

    Logger logger = LoggerFactory.getLogger(GovService.class);

    @Autowired
    MqttConfig.MqttGateway mqttGateway;

    @Autowired
    @Qualifier(value = "MqttOutbound")
    MqttPahoMessageHandler messageHandler;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void publish(GovDeviceRequest govDeviceRequest) throws JsonProcessingException {
        
        String topic = getMqttPublishTopic(govDeviceRequest, Target.host.name());
        messageHandler.setDefaultTopic(topic);
        String payload = objectMapper.writeValueAsString(govDeviceRequest);
        logger.info("publish gov topic:::::::::::" + topic+"\npayload:::"+payload);
        mqttGateway.sendToMqtt(payload);
    }
    /**
     * mqtt publish 토픽 생성
     * @param topicLeadingPath
     * @return
     */
    private String getMqttPublishTopic(GovDeviceRequest govDeviceRequest, String target) {
        String topic = "";
        String[] segments = new String[] { "/server", target, "hcs-w1001", govDeviceRequest.getSerial(), CategoryActive.gateway.ir.name()};
        topic = String.join("/", segments);
        logger.info("====================== GOV PROTO MQTT PUBLISH TOPIC START ======================");
        logger.info(topic);
        logger.info("====================== GOV PROTO MQTT PUBLISH TOPIC END======================");
        return topic;
    }


}
