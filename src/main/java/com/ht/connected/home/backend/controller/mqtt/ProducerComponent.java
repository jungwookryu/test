package com.ht.connected.home.backend.controller.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ht.connected.home.backend.config.service.MqttConfig.MqttGateway;

@Controller
@RequestMapping("/mqtt")
public class ProducerComponent {

    @Autowired
    private AmqpTemplate amqpTemplate;
    
    @Autowired
    private MqttGateway mqttGateway;
    
    @Autowired
    @Qualifier(value = "MqttOutbound")
    MqttPahoMessageHandler messageHandler;
    
    private static final Logger logger = LoggerFactory.getLogger(ProducerComponent.class);

    /**
     * mqtt message publish 한는 component restful로 접근 가능함.
     * @param message
     * @return
     * @throws InterruptedException
     */
    @PostMapping("/")
    public ResponseEntity run(@RequestBody Message message) throws InterruptedException {

        logger.debug("Sending message... Start");
        messageHandler.setDefaultTopic(message.getMessageType());
        mqttGateway.sendToMqtt(message.getMessageBody());
        //TODO amqp message control
        //amqpTemplate.send(message.getMessageType(),new org.springframework.amqp.core.Message(message.getMessageBody().getBytes(),new MessageProperties()));
        return new ResponseEntity( HttpStatus.OK);

    }

}
