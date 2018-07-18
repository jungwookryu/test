package com.ht.connected.home.backend.controller.mqtt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ht.connected.home.backend.category.ir.IRService;
import com.ht.connected.home.backend.category.zwave.ZWaveRequest;
import com.ht.connected.home.backend.category.zwave.ZWaveService;
import com.ht.connected.home.backend.category.zwave.ZWaveStatusService;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.AlarmCommandClass;
import com.ht.connected.home.backend.gateway.Gateway;
import com.ht.connected.home.backend.gateway.GatewayService;
import com.ht.connected.home.backend.gatewayCategory.CategoryActive;
import com.ht.connected.home.backend.service.mqtt.Target;

@Component
public class ConsumerListener {

    @Autowired
    private ZWaveService zwaveService;
    
    @Autowired
    private ZWaveStatusService zWaveStatusService;
    
    
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
            Gateway gateway = new Gateway();
            if (4 <= topicSplited.length) {
                gateway = new Gateway(topicSplited[3].toString(), topicSplited[4].toString());
                gateway.setTargetType(topicSplited[1]);
            }
            // 서버에서 보낸것이 아닐경우만 subscribe함.
            if (!Target.server.name().equals(topicSplited[1].toString())) {
                logger.info(topicSplited[5].toString() + " subStart");
                if (CategoryActive.gateway.manager.name().equals(topicSplited[5].toString())) {
                    gateWayService.subscribe(topic, payload);
                }
                // zwave service
                if (CategoryActive.gateway.zwave.name().equals(topicSplited[5].toString())) {
                    ZWaveRequest zwaveRequest = new ZWaveRequest(topicSplited);
                    if (CategoryActive.zwave.certi.name().equals(topicSplited[6].toString())) {
                        if(AlarmCommandClass.INT_ID == zwaveRequest.getClassKey()) {
                            if(AlarmCommandClass.INT_ALARM_REPORT == zwaveRequest.getClassKey()) {
                                zWaveStatusService.subscribe(zwaveRequest, payload);
                            }
                        }else {
                            zwaveService.subscribe(zwaveRequest, payload);
                        }
                        
                        
                    }
                    if (CategoryActive.zwave.init.name().equals(topicSplited[6].toString())) {
                        zwaveService.subscribeInit(gateway);
                    }
                }
                if (CategoryActive.gateway.ir.name().equals(topicSplited[5].toString().trim())) {
                    irService.subscribe(topicSplited, payload);
                }
            }
            String mqttLog = "host :: category ::";
            if (topicSplited.length > 5) {
                mqttLog += topicSplited[5].toString();
            }
            if (topicSplited.length > 6) {
                mqttLog += "active ::" + topicSplited[6].toString() + " subEnd";
            }
            // TODO category가 정해지지 않은 mqtt에대한 로직이 생길경우 .else에 대한 로직을 넣을 예정
            logger.info(mqttLog);
        }
    }

}
