package com.ht.connected.home.backend.sip.message.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SipMqttResponseMessageDto {

    @JsonProperty(value = "Type")
    private String type = "response";

    @JsonProperty(value = "Method")
    private String method;

    @JsonProperty(value = "CRUDType")
    private String crudType;

    @JsonProperty(value = "ResponseTopic")
    private String responseTopic = "none";

    @JsonProperty(value = "TransactionID")
    private String transactionId = "TransactionIDArg";

    @JsonProperty(value = "ResponseTimeout")
    private String responseTimeout = "ResponseTimeoutArg";

    @JsonProperty(value = "Authentication")
    private String authentication = "AuthenticationArg";

    @JsonProperty(value = "MessageIntegrity")
    private String messageIntegrity = "MessageIntegrityArg";

    @JsonProperty(value = "Result")
    private Boolean result = true;

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

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

}
