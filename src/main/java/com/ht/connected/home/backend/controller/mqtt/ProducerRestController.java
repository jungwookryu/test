package com.ht.connected.home.backend.controller.mqtt;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mqtt")
public class ProducerRestController{

    
    @Value("${spring.activemq.queueName}")
    String activemqQueueName;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ProducerRestController.class);
   
    @PostMapping("/")
    public void run(@RequestBody Message message) throws InterruptedException {
        logger.info("Sending message... Start");
        rabbitTemplate.convertAndSend(activemqQueueName, message.getMessageType(), message.getMessageBody());
//        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
       
        logger.info("Sending message..messageType::: " + message.getMessageType()+":: messageBody" + message.getMessageBody()+" End");
    }

}
