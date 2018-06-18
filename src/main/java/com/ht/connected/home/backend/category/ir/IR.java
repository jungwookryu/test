package com.ht.connected.home.backend.category.ir;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "ir")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IR {
    
    public enum Command {
        start, stop, add, del
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("no")
    @Column(name = "no")
    private int no;

    @JsonProperty("serial")
    @Column(name = "serial")
    private String serial;

    @JsonProperty("model")
    @Column(name = "model")
    private String model;

    @JsonProperty("irnickname")
    @Column(name = "ir_name")
    private String irName;

    @JsonProperty("irindex")
    @Column(name = "ir_type")
    private int irType;

    @JsonProperty("command")
    @Column(name = "dev_type")
    private String devType;

    @JsonProperty("description")
    @Column(name = "description")
    private String description;

    @JsonProperty("created_time")
    @Column(name = "created_time")
    private Date createdTime;

    @JsonProperty("lastmodified_time")
    @Column(name = "lastmodified_time")
    private Date lastmodifiedTime;

    @JsonProperty("command")
    @Column(name = "action")
    private String action;

    @JsonProperty("command")
    @Column(name = "status")
    private String status;

    @JsonProperty("format")
    @Column(name = "format")
    private String format;
    
    @JsonProperty("rptcnt")
    @Column(name = "rptcnt")
    private int rptcnt;
    
    @JsonProperty("gap")
    @Column(name = "gap")
    private int gap;
    
    @JsonProperty("length")
    @Column(name = "length")
    private int length;

    @JsonProperty("data")
    @Column(name = "data")
    private String data;

    
    @JsonProperty("sub_number")
    @Column(name = "sub_number")
    private int subNumber;

    @JsonProperty("user_email")
    @Column(name = "user_email")
    private String userEmail;

    @JsonProperty("gateway_no")
    @Column(name = "gateway_no")
    private int gatewayNo;
    
    public IR() {
        this.createdTime = new Date();
    }
    
    public IR(String devType, String action, int irType) {
        this.devType = devType;
        this.action = action;
        this.irType = irType;
    }

    /**
     * @return the no
     */
    public int getNo() {
        return no;
    }

    /**
     * @param no the no to set
     */
    public void setNo(int no) {
        this.no = no;
    }

    /**
     * @return the serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return the irName
     */
    public String getIrName() {
        return irName;
    }

    /**
     * @param irName the irName to set
     */
    public void setIrName(String irName) {
        this.irName = irName;
    }

    /**
     * @return the irType
     */
    public int getIrType() {
        return irType;
    }

    /**
     * @param irType the irType to set
     */
    public void setIrType(int irType) {
        this.irType = irType;
    }

    /**
     * @return the devType
     */
    public String getDevType() {
        return devType;
    }

    /**
     * @param devType the devType to set
     */
    public void setDevType(String devType) {
        this.devType = devType;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the createdTime
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime the createdTime to set
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return the lastmodifiedTime
     */
    public Date getLastmodifiedTime() {
        return lastmodifiedTime;
    }

    /**
     * @param lastmodifiedTime the lastmodifiedTime to set
     */
    public void setLastmodifiedTime(Date lastmodifiedTime) {
        this.lastmodifiedTime = lastmodifiedTime;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return the subNumber
     */
    public int getSubNumber() {
        return subNumber;
    }

    /**
     * @param subNumber the subNumber to set
     */
    public void setSubNumber(int subNumber) {
        this.subNumber = subNumber;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "IR [no=" + no + ", serial=" + serial + ", model=" + model + ", irName=" + irName + ", irType=" + irType + ", devType=" + devType + ", description=" + description + ", createdTime="
                + createdTime + ", lastmodifiedTime=" + lastmodifiedTime + ", action=" + action + ", status=" + status + ", format=" + format + ", subNumber=" + subNumber + ", toString()="
                + super.toString() + "]";
    }

    /**
     * @return the userEmail
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * @param userEmail the userEmail to set
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * @return the rptcnt
     */
    public int getRptcnt() {
        return rptcnt;
    }

    /**
     * @param rptcnt the rptcnt to set
     */
    public void setRptcnt(int rptcnt) {
        this.rptcnt = rptcnt;
    }

    /**
     * @return the gap
     */
    public int getGap() {
        return gap;
    }

    /**
     * @param gap the gap to set
     */
    public void setGap(int gap) {
        this.gap = gap;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    public int getGatewayNo() {
        return gatewayNo;
    }

    public void setGatewayNo(int gatewayNo) {
        this.gatewayNo = gatewayNo;
    }
}
