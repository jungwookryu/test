package com.ht.connected.home.backend.model.rabbit.consumer;

public interface Receiver {

    public void receiveMessage(String message) throws Exception;

}