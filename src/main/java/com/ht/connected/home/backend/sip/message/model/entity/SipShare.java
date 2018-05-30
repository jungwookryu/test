package com.ht.connected.home.backend.sip.message.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

//@Table(name = "shared_info_tb")
@Table(name = "sip_share")
@Entity
public class SipShare {

    @Id
    @Column(name = "idx")
    @GeneratedValue
    private int idx;

    @Column(name = "sn")
    private String serialNumber;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "device_nickname")
    private String deviceNickname;

    @Column(name = "ownership")
    private String ownership;

    @Column(name = "owner_account")
    private String ownerAccount;

    @Column(name = "sip_aor")
    private String sipAor;

    @Column(name = "owner_nickname")
    private String ownerNickname;

    @Column(name = "shared_account")
    private String sharedAccount;

    @Column(name = "shared_nickname")
    private String sharedNickname;

    @Column(name = "device_status")
    private String deviceStatus;

    @Column(name = "shared_status")
    private String sharedStatus;

    @Column(name = "location")
    private String location;

    @Column(name = "is_temp")
    private String isTemp;

    @Column(name = "is_setting")
    private String isSetting;

    @Column(name = "is_monitoring")
    private String isMonitoring;

    @Column(name = "is_bell")
    private String isBell;

    @Column(name = "accept_date")
    private String acceptDate;

    @Column(name = "comment")
    private String comment;

    @Column(name = "loc_latitude")
    private String locLatitude;

    @Column(name = "loc_longitude")
    private String locLongitude;

    public SipShare() {
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

    public String getDeviceNickname() {
        return deviceNickname;
    }

    public void setDeviceNickname(String deviceNickname) {
        this.deviceNickname = deviceNickname;
    }

    public String getOwnership() {
        return ownership;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public String getOwnerAccount() {
        return ownerAccount;
    }

    public void setOwnerAccount(String ownerAccount) {
        this.ownerAccount = ownerAccount;
    }

    public String getSipAor() {
        return sipAor;
    }

    public void setSipAor(String sipAor) {
        this.sipAor = sipAor;
    }

    public String getOwnerNickname() {
        return ownerNickname;
    }

    public void setOwnerNickname(String ownerNickname) {
        this.ownerNickname = ownerNickname;
    }

    public String getSharedAccount() {
        return sharedAccount;
    }

    public void setSharedAccount(String sharedAccount) {
        this.sharedAccount = sharedAccount;
    }

    public String getSharedNickname() {
        return sharedNickname;
    }

    public void setSharedNickname(String sharedNickname) {
        this.sharedNickname = sharedNickname;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getSharedStatus() {
        return sharedStatus;
    }

    public void setSharedStatus(String sharedStatus) {
        this.sharedStatus = sharedStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIsTemp() {
        return isTemp;
    }

    public void setIsTemp(String isTemp) {
        this.isTemp = isTemp;
    }

    public String getIsSetting() {
        return isSetting;
    }

    public void setIsSetting(String isSetting) {
        this.isSetting = isSetting;
    }

    public String getIsMonitoring() {
        return isMonitoring;
    }

    public void setIsMonitoring(String isMonitoring) {
        this.isMonitoring = isMonitoring;
    }

    public String getIsBell() {
        return isBell;
    }

    public void setIsBell(String isBell) {
        this.isBell = isBell;
    }

    public String getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(String acceptDate) {
        this.acceptDate = acceptDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLocLatitude() {
        return locLatitude;
    }

    public void setLocLatitude(String locLatitude) {
        this.locLatitude = locLatitude;
    }

    public String getLocLongitude() {
        return locLongitude;
    }

    public void setLocLongitude(String locLongitude) {
        this.locLongitude = locLongitude;
    }

}
