package com.ht.connected.home.backend.controller.mqtt;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.support.CorrelationData;

public class CompleteMessageCorrelationData extends CorrelationData {
    private final Message message;

    CompleteMessageCorrelationData(String id, Message message) {
        super(id);
        this.message = message;
    }

    public Message getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return "CompleteMessageCorrelationData [id=" + getId() + ", message=" + this.message + "]";
    }
}
