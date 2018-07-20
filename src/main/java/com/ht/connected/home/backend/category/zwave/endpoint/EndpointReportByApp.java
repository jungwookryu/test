package com.ht.connected.home.backend.category.zwave.endpoint;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.connected.home.backend.category.zwave.notification.Notification;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EndpointReportByApp {
    
    @JsonProperty("endpoint_no")
    int endpointNo;
    
    @JsonProperty("epid")
    int epid;
    
    @JsonProperty("nickname")
    String nickname;
    
    @JsonProperty("ep_status")
    int epStatus;

    @JsonProperty("function_code")
    String functionCode;
    
    @JsonProperty("device_type_code")
    String deviceTypeCode;
    
    @JsonProperty("device_type_name")
    String deviceTypeName;
   
    @JsonProperty("notifications")
    List<Notification> notifications;
    
    public EndpointReportByApp() {
        
    }
    public int getEpid() {
        return epid;
    }

    public int getEndpointNo() {
        return endpointNo;
    }

    public void setEndpointNo(int endpointNo) {
        this.endpointNo = endpointNo;
    }

    public void setEpid(int epid) {
        this.epid = epid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getEpStatus() {
        return epStatus;
    }

    public void setEpStatus(int epStatus) {
        this.epStatus = epStatus;
    }

    /**
     * @return the functionCode
     */
    public String getFunctionCode() {
        return functionCode;
    }
    /**
     * @param functionCode the functionCode to set
     */
    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }
    /**
     * @return the notifications
     */
    public List<Notification> getNotifications() {
        return notifications;
    }
    /**
     * @param notifications the notifications to set
     */
    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
    /**
     * @return the device_type_code
     */
    public String getDeviceTypeCode() {
        return deviceTypeCode;
    }
    /**
     * @param device_type_code the device_type_code to set
     */
    public void setDeviceTypeCode(String deviceTypeCode) {
        this.deviceTypeCode = deviceTypeCode;
    }
    /**
     * @return the deviceTypeName
     */
    public String getDeviceTypeName() {
        return deviceTypeName;
    }
    /**
     * @param deviceTypeName the deviceTypeName to set
     */
    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EndpointReportByApp [endpointNo=" + endpointNo + ", epid=" + epid + ", nickname=" + nickname + ", epStatus=" + epStatus + ", functionCode=" + functionCode + ", device_type_code="
                + deviceTypeCode + ", deviceTypeName=" + deviceTypeName + ", notifications=" + notifications + "]";
    }
  }
