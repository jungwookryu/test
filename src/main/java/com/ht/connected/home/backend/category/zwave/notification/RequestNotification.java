package com.ht.connected.home.backend.category.zwave.notification;
/**
 * Accept Response By Host
 * @author COM
 *
 */

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestNotification {
    
    @JsonProperty("v1AlarmType")
    private int v1AlarmType;
    
    @JsonProperty("v1AlarmLevel")
    private int v1AlarmLevel;
    
    @JsonProperty("notificationStatus")
    private int notificationStatus;
    
    @JsonProperty("notificationType")
    private int notificationType;
    
    @JsonProperty("mevent")
    private int mevent;
    
    @JsonProperty("eventParamLen")
    private int eventParamLen;
    
    @JsonProperty("sequence")
    private int sequence;
    
    @JsonProperty("eventParameter")
    private int eventParameter;
    
    @JsonProperty("sequenceNumber")
    private int sequenceNumber;
    
//    {"result_data": {"value": 0}}
    @JsonProperty(value = "result_data")
    private HashMap<String, Object> resultData;
    
    public RequestNotification() {}
    
    /**
     * @return the v1AlarmType
     */
    public int getV1AlarmType() {
        return v1AlarmType;
    }

    /**
     * @param v1AlarmType the v1AlarmType to set
     */
    public void setV1AlarmType(int v1AlarmType) {
        this.v1AlarmType = v1AlarmType;
    }

    /**
     * @return the v1AlarmLevel
     */
    public int getV1AlarmLevel() {
        return v1AlarmLevel;
    }

    /**
     * @param v1AlarmLevel the v1AlarmLevel to set
     */
    public void setV1AlarmLevel(int v1AlarmLevel) {
        this.v1AlarmLevel = v1AlarmLevel;
    }

    /**
     * @return the notificationStatus
     */
    public int getNotificationStatus() {
        return notificationStatus;
    }

    /**
     * @param notificationStatus the notificationStatus to set
     */
    public void setNotificationStatus(int notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    /**
     * @return the notificationType
     */
    public int getNotificationType() {
        return notificationType;
    }

    /**
     * @param notificationType the notificationType to set
     */
    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    /**
     * @return the mevent
     */
    public int getMevent() {
        return mevent;
    }

    /**
     * @param mevent the mevent to set
     */
    public void setMevent(int mevent) {
        this.mevent = mevent;
    }

    /**
     * @return the eventParamLen
     */
    public int getEventParamLen() {
        return eventParamLen;
    }

    /**
     * @param eventParamLen the eventParamLen to set
     */
    public void setEventParamLen(int eventParamLen) {
        this.eventParamLen = eventParamLen;
    }

    /**
     * @return the sequence
     */
    public int getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the eventParameter
     */
    public int getEventParameter() {
        return eventParameter;
    }

    /**
     * @param eventParameter the eventParameter to set
     */
    public void setEventParameter(int eventParameter) {
        this.eventParameter = eventParameter;
    }

    /**
     * @return the sequenceNumber
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * @param sequenceNumber the sequenceNumber to set
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RequestNotification [v1AlarmType=" + v1AlarmType + ", v1AlarmLevel=" + v1AlarmLevel + ", notificationStatus=" + notificationStatus + ", notificationType=" + notificationType
                + ", mevent=" + mevent + ", eventParamLen=" + eventParamLen + ", sequence=" + sequence + ", eventParameter=" + eventParameter + ", sequenceNumber=" + sequenceNumber + "]";
    }

    /**
     * @return the result_data
     */
    public HashMap<String, Object> getResult_data() {
        return resultData;
    }

    /**
     * @param result_data the result_data to set
     */
    public void setResult_data(HashMap<String, Object> resultData) {
        this.resultData = resultData;
    }
    
}
