package com.ht.connected.home.backend.category.zwave;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZWaveControl {
    @JsonProperty("gateway_no")
    int gateway_no;
    
    @JsonProperty("zwave_no")
    int zwave_no;
    
    @JsonProperty("endpoint_no")
    int endpoint_no;
    
    @JsonProperty("value")
    int value;
    
    @JsonProperty("duration")
    int duration;
    
    @JsonProperty("currentValue")
    int currentValue;

    public int getGateway_no() {
        return gateway_no;
    }

    public void setGateway_no(int gateway_no) {
        this.gateway_no = gateway_no;
    }

    public int getZwave_no() {
        return zwave_no;
    }

    public void setZwave_no(int zwave_no) {
        this.zwave_no = zwave_no;
    }

    public int getEndpoint_no() {
        return endpoint_no;
    }

    public void setEndpoint_no(int endpoint_no) {
        this.endpoint_no = endpoint_no;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    @Override
    public String toString() {
        return "ZWaveControl [gateway_no=" + gateway_no + ", zwave_no=" + zwave_no + ", endpoint_no=" + endpoint_no + ", value=" + value + ", duration=" + duration + ", currentValue=" + currentValue
                + "]";
    }
    
}
