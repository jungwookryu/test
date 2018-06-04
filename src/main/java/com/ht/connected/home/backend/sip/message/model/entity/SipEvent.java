package com.ht.connected.home.backend.sip.message.model.entity;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SqlResultSetMappings({ @SqlResultSetMapping(name = "EventSharedDeviceMapping", classes = {
        @ConstructorResult(targetClass = SipEvent.class, columns = { @ColumnResult(name = "idx", type = Integer.class),
                @ColumnResult(name = "sdb_sn", type = String.class),
                @ColumnResult(name = "dateTime", type = String.class),
                @ColumnResult(name = "sdb_event_type", type = String.class),
                @ColumnResult(name = "sdb_event_id", type = String.class),
                @ColumnResult(name = "sdb_video_exist", type = String.class),
                @ColumnResult(name = "device_type", type = String.class),
                @ColumnResult(name = "device_nickname", type = String.class) }) }),
@SqlResultSetMapping(name = "EventOwnerDeviceMapping", classes = {
        @ConstructorResult(targetClass = SipEvent.class, columns = { @ColumnResult(name = "idx", type = Integer.class),
                @ColumnResult(name = "sdb_sn", type = String.class),
                @ColumnResult(name = "dateTime", type = String.class),
                @ColumnResult(name = "sdb_event_type", type = String.class),
                @ColumnResult(name = "sdb_event_id", type = String.class),
                @ColumnResult(name = "sdb_video_exist", type = String.class),
                @ColumnResult(name = "device_type", type = String.class),
                @ColumnResult(name = "device_nickname", type = String.class) }) })})
@NamedNativeQueries({
        @NamedNativeQuery(name = "SipEvent.getSharedDeviceEvents", 
                query = "SELECT re.idx, re.sdb_sn, "
                + "DATE_FORMAT(re.sdb_dateTime, '%Y-%m-%d %T') dateTime, "
                + "re.sdb_event_type, re.sdb_event_id, re.sdb_video_exist, de.device_type, de.device_nickname "
                + "FROM sip_event re JOIN sip_share sh ON sh.sn = re.sdb_sn "
                + "JOIN sip_device de ON de.sn = re.sdb_sn "
                + "WHERE re.sdb_sn IN :sharedDeviceSerialNumber " + "AND sh.accept_date IS NOT NULL "
                + "AND sh.shared_status = 'accept' " + "AND re.sdb_dateTime >= sh.accept_date "
                + "ORDER BY re.sdb_dateTime DESC", 
                resultSetMapping = "EventSharedDeviceMapping"),
        @NamedNativeQuery(name = "SipEvent.getOwnerDeviceEvents", 
                query = "SELECT re.idx, re.sdb_sn, "
                + "DATE_FORMAT(re.sdb_dateTime, '%Y-%m-%d %T') dateTime, "
                + "re.sdb_event_type, re.sdb_event_id, re.sdb_video_exist, de.device_type, de.device_nickname "
                + "FROM sip_event re " 
                + "JOIN sip_device de ON de.sn = re.sdb_sn "
                + "WHERE re.sdb_sn IN :ownerDeviceSerialNumber " + "AND de.accept_date IS NOT NULL "
                + "AND re.sdb_dateTime >= de.accept_date "
                + "ORDER BY re.sdb_dateTime DESC", 
                resultSetMapping = "EventOwnerDeviceMapping") })

@Entity
//@Table(name = "recent_activity_tb")
@Table(name = "sip_event")
@JsonIgnoreProperties({ "locationId", "fileExtension", "ownerName", "locationId" })
public class SipEvent {

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

    @Column(name = "sdb_dateTime")
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

    private String devNickName;
    

    public SipEvent(int index, String devSerial, String dateTime, String eventType, String eventID, String isVideo,
            String devType, String devNickName) {
        this.idx = index;
        this.serialNumber = devSerial;        
        this.dateTime = dateTime;
        this.eventType = eventType;
        this.eventId = eventID;
        this.videoExist = isVideo;
        this.deviceType = devType;
        this.devNickName = devNickName;
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
