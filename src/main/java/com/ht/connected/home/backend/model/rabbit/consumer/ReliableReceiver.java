package com.ht.connected.home.backend.model.rabbit.consumer;

import java.util.concurrent.CountDownLatch;

public class ReliableReceiver implements Receiver {


    private MessageLogger messageLogger = MessageLogger.getInstance();
    private CountDownLatch latch = new CountDownLatch(1);
    private String consumerName;

    
    public ReliableReceiver(String consumerName) {
        this.consumerName = consumerName;
        
    }

    @Override
    public void receiveMessage(String message) throws Exception {
        messageLogger.log("[" + consumerName + "]  " + message);
        latch.countDown();
    }
    
    public CountDownLatch getLatch() {
        return latch;
    }

}