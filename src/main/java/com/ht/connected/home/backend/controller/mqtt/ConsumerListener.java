package com.ht.connected.home.backend.controller.mqtt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ht.connected.home.backend.category.gateway.Gateway;
import com.ht.connected.home.backend.category.gateway.GatewayService;
import com.ht.connected.home.backend.category.ir.IRService;
import com.ht.connected.home.backend.category.zwave.ZWaveRequest;
import com.ht.connected.home.backend.category.zwave.ZWaveService;
import com.ht.connected.home.backend.category.zwave.certi.ZWaveCertiService;
import com.ht.connected.home.backend.gatewayCategory.CategoryActive;
import com.ht.connected.home.backend.service.mqtt.Target;

@Component
public class ConsumerListener {

    @Autowired
    private ZWaveService zwaveService;
    
    @Autowired
    private ZWaveCertiService zWaveCertiService;
    
    @Autowired
    private GatewayService gateWayService;

    @Autowired
    private IRService irService;

    private static final Logger logger = LoggerFactory.getLogger(ConsumerListener.class);

    /**
     * mqtt received component
     * @param message
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws Exception
     */

    public void receiveMessage(Message message) throws JsonParseException, JsonMappingException, IOException, Exception {
        String topic = String.valueOf(message.getMessageType());
        String payload = String.valueOf(message.getMessageBody());

        logger.info("messageArrived: Topic=" + topic + ", Payload=" + payload);
        String[] topicSplited = topic.trim().replace(".", ";").split(";");
        // message topic 4개이상이어야 gateway관련 메세지임.
        if (topicSplited.length > 4) {
            // 서버에서 보낸것이 아닐경우만 subscribe함.
            if ((!Target.server.name().equals(topicSplited[1].toString())) &&
                    (CategoryActive.gateway.manager.name().equals(topicSplited[5].toString()))) {
                logger.info(topicSplited[5].toString() + " subStart");
                gateWayService.subscribe(topic, payload);
            }
            // zwave service
            if ((!Target.server.name().equals(topicSplited[1].toString())) &&
                    (CategoryActive.gateway.zwave.name().equals(topicSplited[5].toString()))) {
                ZWaveRequest zwaveRequest = new ZWaveRequest(topicSplited);
                if (CategoryActive.zwave.certi.name().equals(topicSplited[6].toString())) {
                    zWaveCertiService.subscribe(zwaveRequest, payload);
                }else {
                    zwaveService.subscribe(zwaveRequest, payload);
                }
            }
            if ((!Target.server.name().equals(topicSplited[1].toString())) &&
                    (CategoryActive.gateway.ir.name().equals(topicSplited[5].toString().trim()))) {
                irService.subscribe(topicSplited, payload);
            }
        }
    }

}
