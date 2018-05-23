package com.ht.connected.home.backend.sip.message.model.entity;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SqlResultSetMapping(name = "DeviceShareMapping", classes = { @ConstructorResult(targetClass = SipDevice.class, columns = {
        @ColumnResult(name = "serialNumber", type = String.class),
        @ColumnResult(name = "deviceType", type = String.class),
        @ColumnResult(name = "deviceNickname", type = String.class),
        @ColumnResult(name = "deviceStatus", type = String.class),
        @ColumnResult(name = "ownerAccount", type = String.class),
        @ColumnResult(name = "ownerNickname", type = String.class),
        @ColumnResult(name = "sharedAccount", type = String.class),
        @ColumnResult(name = "sharedStatus", type = String.class),
        @ColumnResult(name = "locLatitude", type = String.class),
        @ColumnResult(name = "locLongitude", type = String.class) }) })
@NamedNativeQuery(name = "SipDevice.getSharedDevices", query = "select TB1.sn, TB1.device_type, TB1.device_nickname, TB1.device_status, TB1.owner_account, "
        + "TB1.owner_nickname, TB2.shared_account, TB2.shared_status, TB1.loc_latitude, TB1.loc_longitude "
        + "from sip_device " + "AS TB1 LEFT OUTER join sip_share AS TB2 "
        + "ON TB1.sn = TB2.sn AND TB2.shared_account = :sharedAccount "
        + "where TB1.owner_account = :ownerAccount ", resultSetMapping = "DeviceShareMapping")
@JsonIgnoreProperties({ "idx", "ownership", "sipRole", "sharedNickname", "location", "is_temp", "is_setting",
        "is_monitoring", "is_bell", "accept_date", "comment", "user_password" })
//@Table(name="device_info_tb")
@Table(name="sip_device")
@Entity
public class SipDevice {

    @Id
    @Column(name = "idx")
    @GeneratedValue
    private int idx;

    @JsonProperty(value = "devSerial")
    @Column(name = "sn")
    private String serialNumber;

    @JsonProperty(value = "devType")
    @Column(name = "device_type", nullable = false)
    private String deviceType;

    @JsonProperty(value = "devAlias")
    @Column(name = "device_nickname")
    private String deviceNickname;

    @Column(name = "ownership")
    private String ownership;

    @Column(name = "owner_account")
    private String ownerAccount;

    @Column(name = "sip_aor")
    private String sipRole;

    @JsonProperty(value = "ownerAlias")
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

    @JsonProperty(value = "latitude")
    @Column(name = "loc_latitude")
    private String locLatitude;

    @JsonProperty(value = "longitude")
    @Column(name = "loc_longitude")
    private String locLongitude;

    @Column(name = "user_password")
    private String userPassword;
    
    

    public SipDevice() {
        
    }

    public SipDevice(String serialNumber, String deviceType, String deviceNickname, String deviceStatus,
            String ownerAccount, String ownerNickname, String sharedAccount, String sharedStatus, String locLatitude,
            String locLongitude) {
        this.serialNumber = serialNumber;
        this.deviceType = deviceType;
        this.deviceNickname = deviceNickname;
        this.deviceStatus = deviceStatus;
        this.ownerAccount = ownerAccount;
        this.ownerNickname = ownerNickname;
        this.sharedAccount = sharedAccount;
        this.sharedStatus = sharedStatus;
        this.locLatitude = locLatitude;
        this.locLongitude = locLongitude;
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

    public String getSipRole() {
        return sipRole;
    }

    public void setSipRole(String sipRole) {
        this.sipRole = sipRole;
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

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

}
