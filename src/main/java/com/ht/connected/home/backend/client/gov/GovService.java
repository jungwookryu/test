package com.ht.connected.home.backend.client.gov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.common.MqttCommon;
import com.ht.connected.home.backend.controller.mqtt.Message;
import com.ht.connected.home.backend.controller.mqtt.ProducerComponent;
import com.ht.connected.home.backend.device.category.CategoryActive;
import com.ht.connected.home.backend.service.mqtt.Target;

@Service
public class GovService {

    Logger logger = LoggerFactory.getLogger(GovService.class);

    @Autowired
    ProducerComponent producerRestController;
    private ObjectMapper objectMapper = new ObjectMapper();

    public void publish(GovDeviceRequest govDeviceRequest) throws JsonProcessingException, InterruptedException {
        
        String topic = getMqttPublishTopic(govDeviceRequest, Target.host.name());
        String payload = objectMapper.writeValueAsString(govDeviceRequest);
        Message message =  new Message(topic, payload);
        MqttCommon.publish(producerRestController, message);
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
