package com.ht.connected.home.backend.controller.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerRestController {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerRestController.class);

    @RabbitListener
    public void onMessage(Message message) {
        logger.info("Received < " + message.toString() + " >");
        System.out.println("Received < " + message.toString() + " >");
    }
}
