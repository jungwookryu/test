package com.ht.connected.home.backend.sip.message.model.dto.account.info;

import com.ht.connected.home.backend.sip.message.model.entity.SipShare;


/**
 * 공유된 기기 정보의 메세지 출력용 데이터형 클래스 정의 
 * 
 * @author 구정화
 *
 */
public class SipSharedDeviceDto {

    private String devSerial;
    private String devType;
    private String devAlias;
    private String latitude;
    private String longitude;
    private String devStatus;
    private String ownerAccount;
    private String ownerAlias;
    private String sharedAccount;
    private String sharedAlias;

    public SipSharedDeviceDto() {

    }

    public SipSharedDeviceDto(SipShare share) {
        setDevSerial(share.getSerialNumber());
        setDevType(share.getDeviceType());
        setDevAlias(share.getDeviceNickname());
        setLatitude(share.getLocLatitude());
        setLongitude(share.getLocLongitude());
        setDevStatus(share.getDeviceStatus());
        setOwnerAccount(share.getOwnerAccount());
        setOwnerAlias(share.getOwnerNickname());
        setSharedAccount(share.getSharedAccount());
        setSharedAlias(share.getSharedNickname());
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

    public String getSharedAccount() {
        return sharedAccount;
    }

    public void setSharedAccount(String sharedAccount) {
        this.sharedAccount = sharedAccount;
    }

    public String getSharedAlias() {
        return sharedAlias;
    }

    public void setSharedAlias(String sharedAlias) {
        this.sharedAlias = sharedAlias;
    }

}
