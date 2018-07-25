package com.ht.connected.home.backend.gateway;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "gateway")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Gateway {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    @JsonProperty("no")
    private int no;

    @Column(name = "nickname")  
    @JsonProperty("nickname")
    private String nickname;

    @Column(name = "id")
    @JsonProperty("id")
    private String id;

    @Column(name = "ip")
    @JsonProperty("ip")
    private String ip;

    @Column(name = "ssid")
    @JsonProperty("ssid")
    private String ssid;

    @Column(name = "bssid")
    @JsonProperty("bssid")
    private String bssid;

    @Column(name = "serial")
    @JsonProperty("serial")
    private String serial;

    @Column(name = "version")
    @JsonProperty("version")
    private String version;

    @Column(name = "model")
    @JsonProperty("model")
    private String model;

    @Column(name = "status")
    @JsonProperty("status")
    private String status;
    
    @Column(name = "homeid")
    @JsonProperty("homeid")
    private String homeid;

    @Column(name = "created_user_id")
    @JsonProperty("created_user_id")
    private String createdUserId;

    @Column(name = "created_time")
    @JsonProperty("created_time")
    private Date createdTime;

    @Column(name = "lastmodified_time")
    @JsonProperty("lastmodified_time")
    private Date lastModifiedTime;

    @Column(name = "target_type")
    @JsonProperty("target_type")
    private String targetType;

    @Transient
    @JsonProperty("user_nick_name")
    private String userNickname;

    @Transient
    @JsonProperty("user_email")
    private String userEmail;

    @Transient
    @JsonProperty("type")
    private String type;
    
    public Gateway() {
    }
    
    public Gateway(String model, String serial) {
        this.model = model;
        this.serial = serial;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreated_user_id(String createdUserId) {
        this.createdUserId = createdUserId;
    }

    public Date getCreated_time() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastModified_time() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUser_email() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }



    public String getHomeid() {
        return homeid;
    }

    public void setHomeid(String homeid) {
        this.homeid = homeid;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    @Override
    public String toString() {
        return "Gateway [no=" + no + ", nickname=" + nickname + ", id=" + id + ", ip=" + ip + ", ssid=" + ssid + ", bssid=" + bssid + ", serial=" + serial + ", version=" + version + ", model=" + model
                + ", status=" + status + ", homeid=" + homeid + ", createdUserId=" + createdUserId + ", createdTime=" + createdTime + ", lastModifiedTime=" + lastModifiedTime + ", targetType="
                + targetType + ", userNickname=" + userNickname + ", userEmail=" + userEmail + ", type=" + type + "]";
    }

}
