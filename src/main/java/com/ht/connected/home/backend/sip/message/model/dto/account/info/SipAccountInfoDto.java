package com.ht.connected.home.backend.sip.message.model.dto.account.info;


/**
 * 사용자와 기기 정보를 취합한 결과 저장 데이터형 클래스 정의
 * 
 * @author 구정화
 *
 */
public class SipAccountInfoDto {
    private String index;
    private String devSerial;
    private String devType;
    private String devAlias;
    private String devLoc;
    private String devStatus;
    private String sharedStatus;
    private String ownerAccount;
    private String ownerAlias;
    private String sharedAccount;
    private String sharedAlias;
    private String isTemp;
    private String isSetting;
    private String isMonitoring;
    private String isBell;
    private String latitude;
    private String longitude;

    public SipAccountInfoDto() {
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
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

    public String getDevLoc() {
        return devLoc;
    }

    public void setDevLoc(String devLoc) {
        this.devLoc = devLoc;
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
