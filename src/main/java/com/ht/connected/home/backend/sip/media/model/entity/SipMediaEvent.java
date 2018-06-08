package com.ht.connected.home.backend.sip.media.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 미디어 패키지에서 상용되는 이벤트 엔터티 모델 클래스
 * 
 * @author 구정화
 *
 */
@Entity
// @Table(name = "recent_activity_tb")
@Table(name = "sip_event")
@JsonIgnoreProperties({ "locationId", "fileExtension", "ownerName", "locationId", "devNickName" })
public class SipMediaEvent {

    @Id
    @Column(name = "idx")
    @GeneratedValue
    @JsonProperty(value = "index")
    private int idx;

    @JsonProperty(value = "devSerial")
    @Column(name = "sdb_sn")
    private String serialNumber;

    @JsonProperty(value = "devType")
    @Column(name = "sdb_device_type")
    private String deviceType;

    @Column(name = "sdb_datetime")
    private String dateTime;

    @Column(name = "sdb_event_type")
    private String eventType;

    @JsonProperty(value = "eventID")
    @Column(name = "sdb_event_id")
    private String eventId;

    @JsonProperty(value = "isVideo")
    @Column(name = "sdb_video_exist")
    private String videoExist;

    @Column(name = "sdb_file_ext")
    private String fileExtension;

    @Column(name = "sdb_owner_name")
    private String ownerName;

    @Column(name = "sdb_location_id")
    private String locationId;

    @Transient
    private String devNickName;

    public SipMediaEvent() {

    }

    public SipMediaEvent(int index, String devSerial, String deviceType, String dateTime, String eventType, String eventID, 
            String isVideo, String fileExtension, String ownerName, String locationId) {
        this.idx = index;
        this.serialNumber = devSerial;
        this.deviceType = deviceType;
        this.dateTime = dateTime;
        this.eventType = eventType;
        this.eventId = eventID;
        this.videoExist = isVideo;
        this.fileExtension = fileExtension;
        this.ownerName = ownerName;
        this.locationId = locationId;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getVideoExist() {
        return videoExist;
    }

    public void setVideoExist(String videoExist) {
        this.videoExist = videoExist;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getDevNickName() {
        return devNickName;
    }

    public void setDevNickName(String devNickName) {
        this.devNickName = devNickName;
    }

}
