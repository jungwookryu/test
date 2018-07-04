package com.ht.connected.home.backend.ipc.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 서브 계정 관리
 * 
 * @author 구정화
 *
 */
@Entity
@Table(name = "ipc_account")
public class IPCAccount {

    @Id
    @Column(name = "seq")
    @GeneratedValue
    private int seq;

    @Column(name = "iot_account")
    private String iotAccount;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "token_expire_at")
    private String tokenExpireAt;

    @Column(name = "area_domain")
    private String areaDomain;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getIotAccount() {
        return iotAccount;
    }

    public void setIotAccount(String iotAccount) {
        this.iotAccount = iotAccount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenExpireAt() {
        return tokenExpireAt;
    }

    public void setTokenExpireAt(String tokenExpireAt) {
        this.tokenExpireAt = tokenExpireAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAreaDomain() {
        return areaDomain;
    }

    public void setAreaDomain(String areaDomain) {
        this.areaDomain = areaDomain;
    }

}
