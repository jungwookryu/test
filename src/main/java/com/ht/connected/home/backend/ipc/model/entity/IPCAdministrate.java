package com.ht.connected.home.backend.ipc.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 마스터 계정 관리
 * 
 * @author 구정화
 *
 */
@Entity
@Table(name = "ipc_administrate")
public class IPCAdministrate {

    @Id
    @Column(name = "seq")
    @GeneratedValue
    private int seq;

    @Column(name = "app_key")
    private String appKey;

    @Column(name = "app_secret")
    private String appSecret;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "token_expire_at")
    private String tokenExpireAt;

    @Column(name = "area_domain")
    private String areaDomain;

    @Column(name = "updated_at")
    private String updatedAt;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAreaDomain() {
        return areaDomain;
    }

    public void setAreaDomain(String areaDomain) {
        this.areaDomain = areaDomain;
    }

}
