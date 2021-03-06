package com.ht.connected.home.backend.update.model.entity;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SqlResultSetMapping(name = "ResultDeviceVersionByModel", classes = {
        @ConstructorResult(targetClass = UPDeviceVersion.class, columns = {
                @ColumnResult(name = "serial", type = String.class),
                @ColumnResult(name = "version_os", type = String.class),
                @ColumnResult(name = "version_api", type = String.class),
                @ColumnResult(name = "version_app", type = String.class),
                @ColumnResult(name = "version_fw", type = String.class) }) })
@NamedNativeQuery(name = "UPDeviceVersion.getByModelName", query = "select g.serial, d.version_os, d.version_api, d.version_app, d.version_fw "
        + "from gateway g " + "inner join up_device_version d on g.serial=d.serial_no "
        + "where g.model = :modelName", resultSetMapping = "ResultDeviceVersionByModel")

@Entity
@Table(name = "up_device_version")
@JsonIgnoreProperties({ "seq", "updatedAt", "iotAccount" })
public class UPDeviceVersion {

    @Id
    @GeneratedValue
    @Column(name = "seq")
    private int seq;

    @Column(name = "model_name")
    @JsonProperty(value = "model_name")
    private String modelName;

    @Column(name = "serial_no")
    @JsonProperty(value = "serial_no")
    private String serialNo;

    @Column(name = "version_os")
    @JsonProperty(value = "os_ver")
    private String versionOS;

    @Column(name = "version_api")
    @JsonProperty(value = "api_ver")
    private String versionAPI;

    @Column(name = "version_app")
    @JsonProperty(value = "app_ver")
    private String versionAPP;

    @Column(name = "version_fw")
    @JsonProperty(value = "fw_ver")
    private String versionFW;

    @Column(name = "updated_at")
    private String updatedAt;

    @Transient
    private String iotAccount;

    public UPDeviceVersion() {
        super();
        // TODO Auto-generated constructor stub
    }

    public UPDeviceVersion(String serialNo, String versionOS, String versionAPI, String versionAPP, String versionFW) {
        super();        
        this.serialNo = serialNo;
        if(isNull(versionOS)) versionOS = "0";
        this.versionOS = versionOS;
        if(isNull(versionAPI)) versionAPI = "0";
        this.versionAPI = versionAPI;
        if(isNull(versionAPP)) versionAPP = "0";
        this.versionAPP = versionAPP;
        if(isNull(versionFW)) versionFW = "0";
        this.versionFW = versionFW;
    }

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

    public String getVersionFW() {
        return versionFW;
    }

    public void setVersionFW(String versionFW) {
        this.versionFW = versionFW;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getIotAccount() {
        return iotAccount;
    }

    public void setIotAccount(String iotAccount) {
        this.iotAccount = iotAccount;
    }

}
