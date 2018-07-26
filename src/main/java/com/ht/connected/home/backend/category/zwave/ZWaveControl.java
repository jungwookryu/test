package com.ht.connected.home.backend.category.zwave;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.connected.home.backend.common.ByteUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZWaveControl {
    @JsonProperty("gateway_no")
    Integer gateway_no;
    
    @JsonProperty("zwave_no")
    Integer zwave_no;
    
    @JsonProperty("endpoint_no")
    Integer endpoint_no;
    
    @JsonProperty("value")
    Integer value;
    
    @JsonProperty("duration")
    Integer duration;
    
    @JsonProperty("currentValue")
    Integer currentValue;

    @JsonProperty(value="set_data")
    HashMap setData;
    
    Integer functionCode;
    
    Integer controlCode;
    
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

    public void setFunctionCode(String sfunctionCode) {
        
        this.functionCode = ByteUtil.getStringtoInt(sfunctionCode);
        
        switch(sfunctionCode) {
            case "20": 
            case "25": 
            case "26":
            case "40":
            case "43": 
            case "47": 
            case "56": 
            case "63": 
            case "73":
                this.controlCode = 1;
                break;
            case "32": 
                this.controlCode = 5;
                break;
            default:
                this.controlCode = 1;
                break;
        }
    }
    @Override
    public String toString() {
        return "ZWaveControl [gateway_no=" + gateway_no + ", zwave_no=" + zwave_no + ", endpoint_no=" + endpoint_no + ", value=" + value + ", duration=" + duration + ", currentValue=" + currentValue
                + "]";
    }

    /**
     * @return the functionCode
     */
    public Integer getFunctionCode() {
        return functionCode;
    }

    /**
     * @param functionCode the functionCode to set
     */
    public void setFunctionCode(Integer functionCode) {
        this.functionCode = functionCode;
    }

    /**
     * @return the controlCode
     */
    public Integer getControlCode() {
        return controlCode;
    }

    /**
     * @param controlCode the controlCode to set
     */
    public void setControlCode(Integer controlCode) {
        this.controlCode = controlCode;
    }

    /**
     * @return the setData
     */
    public HashMap getSetData() {
        return setData;
    }

    /**
     * @param setData the setData to set
     */
    public void setSetData(HashMap setData) {
        this.setData = setData;
    }

    /**
     * @param gateway_no the gateway_no to set
     */
    public void setGateway_no(Integer gateway_no) {
        this.gateway_no = gateway_no;
    }

    /**
     * @param zwave_no the zwave_no to set
     */
    public void setZwave_no(Integer zwave_no) {
        this.zwave_no = zwave_no;
    }

    /**
     * @param endpoint_no the endpoint_no to set
     */
    public void setEndpoint_no(Integer endpoint_no) {
        this.endpoint_no = endpoint_no;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * @param currentValue the currentValue to set
     */
    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }
    
}
