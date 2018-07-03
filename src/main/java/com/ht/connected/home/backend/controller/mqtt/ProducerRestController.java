package com.ht.connected.home.backend.controller.mqtt;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
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
    
    private final CountDownLatch listenLatch = new CountDownLatch(1);

    private final CountDownLatch confirmLatch = new CountDownLatch(1);

    private final CountDownLatch returnLatch = new CountDownLatch(1);
    
    private static final Logger logger = LoggerFactory.getLogger(ProducerRestController.class);
   
    @PostMapping("/")
    public void run(@RequestBody Message message, HttpServletRequest request) throws InterruptedException {
        
        logger.info("Sending message... Start");
        
        org.springframework.amqp.core.Message replyMessage = this.rabbitTemplate.sendAndReceive(message.getMessageType(),
                new org.springframework.amqp.core.Message(message.getMessageBody().getBytes(), new MessageProperties()));
        
//        this.rabbitTemplate.convertAndSend("amq.topic", activemqQueueName, "a.aa", new CorrelationData("Correlation for message 1"));
        setupCallbacks();
        // send a message to the default exchange to be routed to the queue
        
        if (this.confirmLatch.await(10, TimeUnit.SECONDS)) {
            System.out.println("Confirm received");
        }
        else {
            System.out.println("Confirm NOT received");
        }
        if (this.listenLatch.await(10, TimeUnit.SECONDS)) {
            System.out.println("Message received by listener");
        }
        else {
            System.out.println("Message NOT received by listener");
        }
        
        if (this.returnLatch.await(10, TimeUnit.SECONDS)) {
            System.out.println("Return received");
        }
        else {
            System.out.println("Return NOT received");
        }
        logger.info("Sending message..messageType::: " + message.toString());
        
//        return new ResponseEntity<>(message.toString(), HttpStatus.OK);
//        return new ResponseEntity<>( HttpStatus.OK);
       
    }
    
    private void setupCallbacks() {
        /*
         * Confirms/returns enabled in application.properties - add the callbacks here.
         */
        this.rabbitTemplate.setConfirmCallback((correlation, ack, reason) -> {
            if (correlation != null) {
                System.out.println("Received " + (ack ? " ack " : " nack ") + "for correlation: " + correlation);
            }
            this.confirmLatch.countDown();
        });
        this.rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("Returned: " + message + "\nreplyCode: " + replyCode
                    + "\nreplyText: " + replyText + "\nexchange/rk: " + exchange + "/" + routingKey);
            this.returnLatch.countDown();
        });
        /*
         * Replace the correlation data with one containing the converted message in case
         * we want to resend it after a nack.
         */
        this.rabbitTemplate.setCorrelationDataPostProcessor((message, correlationData) ->
                new CompleteMessageCorrelationData(correlationData != null ? correlationData.getId() : null, message));
    }
    
    @RabbitListener
    public void listen(String in) {
        System.out.println("Listener received: " + in);
        this.listenLatch.countDown();
    }

}
