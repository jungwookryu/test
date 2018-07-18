package com.ht.connected.home.backend.category.zwave.notification;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "zwave_endpoint_notification")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notification {


    @Id
    @Column(name = "no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("no")
    int no;

    @Column(name = "notification_type")
    @JsonProperty("notification_type")
    String notificationType;

    @Column(name = "notification_code")
    @JsonProperty("notification_code")
    int notificationCode;
    
    @Column(name = "event_code")
    @JsonProperty("event_code")
    int eventCode;
    
    @Column(name = "event_name")
    @JsonProperty("event_name")
    String eventName;
    
    @Column(name = "sequence")
    @JsonProperty("sequence")
    int sequence;
    
    @Column(name = "device_type_code")
    @JsonProperty("device_type_code")
    String deviceTypeCode;
    
    @Column(name = "device_type_name")
    @JsonProperty("device_type_name")
    String deviceTypeName;
    
    @Column(name = "endpoint_no")
    @JsonProperty("endpoint_no")
    int endpointNo;
    
    @Column(name = "zwave_no")
    @JsonProperty("zwave_no")
    int zwaveNo;
  
    public Notification() {
        super();
    };
    
    public Notification(int notificationCode, int eventCode, int sequence, String deviceTypeCode, int zwaveNo, int endpointNo) {
        this.deviceTypeCode = deviceTypeCode;
        this.notificationCode = notificationCode;
        this.eventCode = eventCode;
        this.sequence = sequence;
        this.zwaveNo = zwaveNo;
        this.endpointNo = endpointNo;
        setEventNameByCode();
        setNotificationTypeByCode();

    }

    /**
     * @return the no
     */
    public int getNo() {
        return no;
    }

    /**
     * @param no the no to set
     */
    public void setNo(int no) {
        this.no = no;
    }

    /**
     * @return the notificationType
     */
    public String getNotificationType() {
        return notificationType;
    }

    /**
     * @param notificationType the notificationType to set
     */
    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    /**
     * @return the notificationCode
     */
    public int getNotificationCode() {
        return notificationCode;
    }

    /**
     * @param notificationCode the notificationCode to set
     */
    public void setNotificationCode(int notificationCode) {
        this.notificationCode = notificationCode;
    }

    /**
     * @return the eventCode
     */
    public int getEventCode() {
        return eventCode;
    }

    /**
     * @param eventCode the eventCode to set
     */
    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    /**
     * @return the eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * @param eventName the eventName to set
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
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
     * @return the deviceTypeCode
     */
    public String getDeviceTypeCode() {
        return deviceTypeCode;
    }

    /**
     * @param deviceTypeCode the deviceTypeCode to set
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
        return "Endpoint [no=" + no + ", notificationType=" + notificationType + ", notificationCode=" + notificationCode + ", eventCode=" + eventCode + ", eventName=" + eventName + ", sequence="
                + sequence + ", deviceTypeCode=" + deviceTypeCode + ", deviceTypeName=" + deviceTypeName + ", getNo()=" + getNo() + ", getNotificationType()=" + getNotificationType()
                + ", getNotificationCode()=" + getNotificationCode() + ", getEventCode()=" + getEventCode() + ", getEventName()=" + getEventName() + ", getSequence()=" + getSequence()
                + ", getDeviceTypeCode()=" + getDeviceTypeCode() + ", getDeviceTypeName()=" + getDeviceTypeName() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
                + super.toString() + "]";
    }


    public void setEventNameByCode() {
        this.eventName = NotificationEvent.getEventName(this.getNotificationCode(),this.getEventCode());
    }
    
    public void setNotificationTypeByCode() {
        this.notificationType = NotificationType.getZwaveNotificationName(this.notificationCode);
    }
    
    public void setDeviceTypeNameByCode(String deviceTypeCode) {
        this.deviceTypeName = "";
    }

    /**
     * @return the endpointNo
     */
    public int getEndpointNo() {
        return endpointNo;
    }

    /**
     * @param endpointNo the endpointNo to set
     */
    public void setEndpointNo(int endpointNo) {
        this.endpointNo = endpointNo;
    }

    /**
     * @return the zwaveNo
     */
    public int getZwaveNo() {
        return zwaveNo;
    }

    /**
     * @param zwaveNo the zwaveNo to set
     */
    public void setZwaveNo(int zwaveNo) {
        this.zwaveNo = zwaveNo;
    }

}
