package com.ht.connected.home.backend.controller.mqtt;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProducerRestController{

    
    @Value("${spring.activemq.queueName}")
    String activemqQueueName;
    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public ProducerRestController(Receiver receiver, RabbitTemplate rabbitTemplate) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }
    private static final Logger logger = LoggerFactory.getLogger(ProducerRestController.class);
   
    public void run(Message message) throws InterruptedException {
        logger.info("Sending message... Start");
        rabbitTemplate.convertAndSend(activemqQueueName, message.getMessageType(), message.getMessageBody());
        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        logger.info("Sending message... End");
    }

}
