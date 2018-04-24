package com.ht.connected.home.backend.controller.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ht.connected.home.backend.model.rabbit.producer.Message;

@Component
public class ProducerRestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.activemq.queueName}")
    String activemqQueueName;
    private static final Logger logger = LoggerFactory.getLogger(ProducerRestController.class);

//    public void onSend(String val) {
//        logger.info("Sending message... Start");
//        rabbitTemplate.convertAndSend(activemqQueueName, new Message("routingKey", val));
//        logger.info("Sending message... End");
//    }

}
