package com.ht.connected.home.backend.sip.message.model.dto.account.info;

import com.ht.connected.home.backend.sip.message.model.entity.SipDevice;

/**
 * 유저의 소유한 기기 정보 조회 결과 데이터형 클래스 정의
 * 
 * @author 구정화
 *
 */
public class SipOwnerDeviceDto {

    private String devSerial;
    private String devType;
    private String devAlias;
    private String latitude;
    private String longitude;
    private String devStatus;
    private String ownerAccount;
    private String ownerAlias;

    public SipOwnerDeviceDto(SipDevice device) {
        setDevSerial(device.getSerialNumber());
        setDevType(device.getDeviceType());
        setDevAlias(device.getDeviceNickname());
        setLatitude(device.getLocLatitude());
        setLongitude(device.getLocLongitude());
        setDevStatus(device.getDeviceStatus());
        setOwnerAccount(device.getOwnerAccount());
        setOwnerAlias(device.getOwnerNickname());
    }

    public String getDevSerial() {
        return devSerial;
    }

    public void setDevSerial(String devSerial) {
        this.devSerial = devSerial;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getDevAlias() {
        return devAlias;
    }

    public void setDevAlias(String devAlias) {
        this.devAlias = devAlias;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(String devStatus) {
        this.devStatus = devStatus;
    }

    public String getOwnerAccount() {
        return ownerAccount;
    }

    public void setOwnerAccount(String ownerAccount) {
        this.ownerAccount = ownerAccount;
    }

    public String getOwnerAlias() {
        return ownerAlias;
    }

    public void setOwnerAlias(String ownerAlias) {
        this.ownerAlias = ownerAlias;
    }

}
