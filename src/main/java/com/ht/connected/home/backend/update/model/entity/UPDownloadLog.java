package com.ht.connected.home.backend.update.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "up_download_log")
public class UPDownloadLog {

    @Id
    @GeneratedValue
    @Column(name = "seq")
    private int seq;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "serial_no")
    private String serialNo;

    @Column(name = "success")
    private String success;

    @Column(name = "created_at")
    private String createdAt;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

}
