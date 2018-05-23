package com.ht.connected.home.backend.sip.message.model.dto;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"topic","result"})
public class SipMqttRequestMessageDto {

    @JsonProperty(value = "Type")
    private String type;

    @JsonProperty(value = "Method")
    private String method;

    @JsonProperty(value = "CRUDType")
    private String crudType;

    @JsonProperty(value = "ResponseTopic")
    private String responseTopic;

    @JsonProperty(value = "TransactionID")
    private String transactionId;

    @JsonProperty(value = "ResponseTimeout")
    private String responseTimeout;

    @JsonProperty(value = "Authentication")
    private String authentication;

    @JsonProperty(value = "MessageIntegrity")
    private String messageIntegrity;

    @JsonProperty(value = "Body")
    private HashMap<String, Object> body;

    private String[] topic;

    private boolean result = true;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCrudType() {
        return crudType;
    }

    public void setCrudType(String crudType) {
        this.crudType = crudType;
    }

    public String getResponseTopic() {
        return responseTopic;
    }

    public void setResponseTopic(String responseTopic) {
        this.responseTopic = responseTopic;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getResponseTimeout() {
        return responseTimeout;
    }

    public void setResponseTimeout(String responseTimeout) {
        this.responseTimeout = responseTimeout;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getMessageIntegrity() {
        return messageIntegrity;
    }

    public void setMessageIntegrity(String messageIntegrity) {
        this.messageIntegrity = messageIntegrity;
    }

    public HashMap<String, Object> getBody() {
        return body;
    }

    public void setBody(HashMap<String, Object> body) {
        this.body = body;
    }

    public String[] getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic.split("/");
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    
}
