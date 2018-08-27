package com.ht.connected.home.backend.update.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 업데이트 파일 등록 엔터티
 * 
 * @author 구정화
 *
 */
@Entity
@Table(name = "up_file_version")
@JsonIgnoreProperties({ "seq", "updatedAt", "versionOS", "versionAPI", "versionAPP" })
public class UPFileVersion {

    public static String UPDATE_TYPE_OS = "os";
    public static String UPDATE_TYPE_API = "api";
    public static String UPDATE_TYPE_APP = "app";
    
    public static String SPECIFIC_SERIAL_SINGLE = "1";
    public static String SPECIFIC_SERIAL_MULTIPLE = "2";

    @Id
    @GeneratedValue
    @Column(name = "seq")
    private int seq;

    @Column(name = "model_name")
    @JsonProperty(value = "model_name")
    private String modelName;

    @Column(name = "version_os")
    @JsonProperty(value = "os_version")
    private String versionOS;

    @Column(name = "version_api")
    @JsonProperty(value = "api_version")
    private String versionAPI;

    @Column(name = "version_app")
    @JsonProperty(value = "app_version")
    private String versionAPP;

    @Column(name = "updated_at")
    private String updatedAt;

    @Transient
    @JsonProperty(value = "update_type")
    private String updateType;

    @Transient
    @JsonProperty(value = "serial_type")
    private String serialType;

    @Transient
    @JsonProperty(value = "serial_single")
    private String serialSingle;

    @Transient
    @JsonProperty(value = "serial_from")
    private String serialFrom;

    @Transient
    @JsonProperty(value = "serial_to")
    private String serialTo;

    @Transient
    @JsonProperty(value = "url")
    private String url;

    @Transient
    @JsonProperty(value = "md5")
    private String md5;

    @Transient
    @JsonProperty(value = "force")
    private String force;

    @Transient
    @JsonProperty(value = "device_type")
    private String deviceType;

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

    public String getVersionOS() {
        return versionOS;
    }

    public void setVersionOS(String versionOS) {
        this.versionOS = versionOS;
    }

    public String getVersionAPI() {
        return versionAPI;
    }

    public void setVersionAPI(String versionAPI) {
        this.versionAPI = versionAPI;
    }

    public String getVersionAPP() {
        return versionAPP;
    }

    public void setVersionAPP(String versionAPP) {
        this.versionAPP = versionAPP;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getSerialType() {
        return serialType;
    }

    public void setSerialType(String serialType) {
        this.serialType = serialType;
    }

    public String getSerialSingle() {
        return serialSingle;
    }

    public void setSerialSingle(String serialSingle) {
        this.serialSingle = serialSingle;
    }

    public String getSerialFrom() {
        return serialFrom;
    }

    public void setSerialFrom(String serialFrom) {
        this.serialFrom = serialFrom;
    }

    public String getSerialTo() {
        return serialTo;
    }

    public void setSerialTo(String serialTo) {
        this.serialTo = serialTo;
    }

}
