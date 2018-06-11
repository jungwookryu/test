package com.ht.connected.home.backend.sip.media.model.entity;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;


@SqlResultSetMapping(name = "PushTokenUnionAllMapping", classes = {
        @ConstructorResult(targetClass = SipMediaUser.class, columns = {
                @ColumnResult(name = "account", type = String.class),
                @ColumnResult(name = "token", type = String.class)}) })
@NamedNativeQuery(name = "SipMediaUser.getPushTokens", query = "SELECT owner_account as account, B.push_token as token "
        + "FROM sip_device AS A "
        + "LEFT OUTER JOIN sip_member AS B ON A.owner_account = B.user_id "
        + "WHERE sn = :serialNumber AND phone_type = :phoneType "
        + "AND ( B.device_type = :deviceType1 OR B.device_type = :deviceType2 ) "
        + "AND device_status = 'registered' "
        + "UNION ALL "
        + "SELECT shared_account as account, B.push_token as token "
        + "FROM sip_share AS A "
        + "LEFT OUTER JOIN sip_member AS B ON A.shared_account = B.user_id "
        + "WHERE sn = :serialNumber AND phone_type = :phoneType "
        + "AND ( B.device_type = :deviceType1 OR B.device_type = :deviceType2 ) "
        + "AND shared_status = 'accept'", 
        resultSetMapping = "PushTokenUnionAllMapping")
// @Table(name = "member_info_tb")
/**
 * 미디어 패키지에서 상용되는 사용자 엔터티 모델 클래스
 * 
 * @author 구정화
 *
 */
@Table(name = "sip_member")
@Entity
public class SipMediaUser {

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
    

    public SipMediaUser(String account, String token) {
        this.userId = account;
        this.pushToken = token;
    }

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
