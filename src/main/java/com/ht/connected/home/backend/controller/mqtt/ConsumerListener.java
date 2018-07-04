package com.ht.connected.home.backend.controller.mqtt;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class ConsumerListener {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerListener.class);
    private CountDownLatch latch = new CountDownLatch(1);
    
    
    public <T> void receiveMessage(T message) {
        if (message instanceof String) {
            logger.info("onMessageReceived < " + message + " >");
        }
        else if (message instanceof Message) {
            logger.info("onMessageReceived < " + message.toString() + " >");
        }
        else if (message instanceof List) {
            logger.info("onMessageReceived < " + ((List) message).size() + " >");
        }
        if (message instanceof JSONObject) {
            logger.info("onMessageReceived < " + ((JSONObject) message).toString() + " >");
        }
        else if (message instanceof byte[]) {
            logger.info("onMessageReceived < " + ((byte[]) message).toString() + " >");
        }
        else if (message instanceof Object) {
            logger.info("onMessageReceived < " + message.toString() + " >");
        }
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
