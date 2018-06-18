package com.ht.connected.home.backend.controller.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class ConsumerListener extends MessageListenerAdapter{

    private static final Logger logger = LoggerFactory.getLogger(ConsumerListener.class);

    public void onMessage(Message message) {
        logger.info("Received < " + message.toString() + " >");
    }
}
