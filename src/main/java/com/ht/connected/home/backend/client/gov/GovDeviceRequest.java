package com.ht.connected.home.backend.client.gov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ir 요청시 토픽 URI 경로 구분자(/)로 분할하여 getter통해 쉽게 사용하기 위한 클래스
 * @author injeongLee
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GovDeviceRequest {

    @JsonProperty("device")
    private String device;
    @JsonProperty("type")
    private String type;
    @JsonProperty("id")
    private String id;
    @JsonProperty("action")
    private String action;
    @JsonProperty("value")
    private String value;
    @JsonProperty("serial")
    private String serial;
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the model
     */
    public String getSerial() {
        return serial;
    }

    /**
     * @param model the model to set
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    @Override
    public String toString() {
        return "GovDeviceRequest [device=" + device + ", type=" + type + ", id=" + id + ", action=" + action + ", value=" + value + ", serial=" + serial + "]";
    }

}
