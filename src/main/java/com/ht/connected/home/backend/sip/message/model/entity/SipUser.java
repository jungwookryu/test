package com.ht.connected.home.backend.sip.message.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

//@Table(name = "member_info_tb")
@Table(name = "sip_member")
@Entity
public class SipUser {

    @Id
    @Column(name = "idx")
    @GeneratedValue
    private int idx;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_aor")
    private String userAor;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "user_mail_address")
    private String userEmail;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "phone_type")
    private String phoneType;

    @Column(name = "device_sn")
    private String deviceSerialNumber;

    @Column(name = "push_token")
    private String pushToken;

    @Column(name = "ktt_id")
    private String kttId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAor() {
        return userAor;
    }

    public void setUserAor(String userAor) {
        this.userAor = userAor;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getKttId() {
        return kttId;
    }

    public void setKttId(String kttId) {
        this.kttId = kttId;
    }

}
