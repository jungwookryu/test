package com.ht.connected.home.backend.sip.message.model.dto.account.info;

import com.ht.connected.home.backend.sip.message.model.entity.SipShare;

/**
 * 유저와 공유된 기기 정보의 취합된 데이터형의 클래스 정의
 * 
 * @author 구정화
 *
 */
public class SipOwnerSharedDeviceDto {

    private String devSerial;
    private String devType;
    private String devAlias;
    private String devStatus;
    private String sharedStatus;
    private String sharedAccount;
    private String sharedAlias;
    private String latitude;
    private String longitude;
    
    

    public SipOwnerSharedDeviceDto(SipShare share) {        
        setDevSerial(share.getSerialNumber());
        setDevType(share.getDeviceType());
        setDevAlias(share.getDeviceNickname());
        setDevStatus(share.getDeviceStatus());
        setSharedStatus(share.getSharedStatus());
        setSharedAccount(share.getSharedAccount());
        setSharedAlias(share.getSharedNickname());                
        setLatitude(share.getLocLatitude());
        setLongitude(share.getLocLongitude());
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

    public String getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(String devStatus) {
        this.devStatus = devStatus;
    }

    public String getSharedStatus() {
        return sharedStatus;
    }

    public void setSharedStatus(String sharedStatus) {
        this.sharedStatus = sharedStatus;
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

}
