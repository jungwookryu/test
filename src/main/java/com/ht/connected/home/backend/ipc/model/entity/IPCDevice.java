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
@Table(name = "ipc_device")
public class IPCDevice {

    @Id
    @Column(name = "seq")
    @GeneratedValue
    private int seq;

    @Column(name = "device_serial")
    private String deviceSerial;

    @Column(name = "account_seq")
    private int accountSeq;

    @Column(name = "is_owner")
    private String isOwner;

    @Column(name = "created_at")
    private String createdAt;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getAccountSeq() {
        return accountSeq;
    }

    public void setAccountSeq(int accountSeq) {
        this.accountSeq = accountSeq;
    }

    public String getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(String isOwner) {
        this.isOwner = isOwner;
    }

}
