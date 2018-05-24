package com.ht.connected.home.backend.emergency;

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
@Table(name = "user_emergency")
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEmergency {
    
    public enum emergencyType{
        nomal,
        homeOut,
        homeIn,
        homeSleep,
        homeInTempo,
        customEmergency
    }
    public UserEmergency(){
        
    }
    public UserEmergency(int userNo, String userEmail, int gatewayNo,String emergencyType){
        this.userNo = userNo;
        this.userEmail = userEmail;
        this.gatewayNo = gatewayNo;
        this.emergencyType = emergencyType;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    @JsonProperty("no")
    private int no;
    
    @Column(name = "user_no")
    @JsonProperty("user_no")
    int userNo;
    
    @Column(name = "user_email")
    @JsonProperty("user_email")
    String userEmail;

    @Column(name = "gateway_no")
    @JsonProperty("gateway_no")
    int gatewayNo;

    @Column(name = "emergency_type")
    @JsonProperty("emergency_type")
    String emergencyType;
    
    @JsonProperty("description")
    @Column(name = "description")
    String description;

    @Column(name = "fire")
    @JsonProperty("fire")
    String fire;
    
    @Column(name = "fire_no")
    @JsonProperty("fire_no")
    String fireNo;

    @Column(name = "custom_sensor_list")
    @JsonProperty("custom_sensor_list")
    String customSensorList;
    
    @Column(name = "custom_sensor_no_list")
    @JsonProperty("custom_sensor_no_list")
    String customSensorNoList;

    @Column(name = "doorlock")
    @JsonProperty("doorlock")
    String doorlock;
    
    @Column(name = "doorlock_no")
    @JsonProperty("doorlock_no")
    String doorlockNo;

    @Column(name = "living_movement")
    @JsonProperty("living_movement")
    String livingMovement;
    
    @Column(name = "living_movement_no")
    @JsonProperty("living_movement_no")
    String livingMovementNo;

    @Column(name = "balcony_movenent")
    @JsonProperty("balcony_movenent")
    String balconyMovenent;

    @Column(name = "balcony_movenent_no")
    @JsonProperty("balcony_movenent_no")
    String balconyMovenentNo;
    
    @Column(name = "created_time")
    @JsonProperty("created_time")
    private Date createdTime;
    
    @Column(name = "lastmodified_time")
    @JsonProperty("lastmodified_time")
    private Date lastModifiedTime;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getEmergencytype() {
        return emergencyType;
    }

    public void setEmergencyType(String emergencyType) {
        this.emergencyType = emergencyType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFire() {
        return fire;
    }

    public void setFire(String fire) {
        this.fire = fire;
    }

    public String getCustomSensorList() {
        return customSensorList;
    }

    public void setCustomSensorList(String customSensorList) {
        this.customSensorList = customSensorList;
    }

    public String getDoorlock() {
        return doorlock;
    }

    public void setDoorlock(String doorlock) {
        this.doorlock = doorlock;
    }

    public String getLivingMovement() {
        return livingMovement;
    }

    public void setLivingMovement(String livingMovement) {
        this.livingMovement = livingMovement;
    }

    public String getBalconyMovenent() {
        return balconyMovenent;
    }

    public void setBalconyMovenent(String balconyMovenent) {
        this.balconyMovenent = balconyMovenent;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @Override
    public String toString() {
        return "UserEmergency [no=" + no + ", userNo=" + userNo + ", userEmail=" + userEmail + ", userState=" + emergencyType + ", description=" + description + ", fire=" + fire + ", customSensorList="
                + customSensorList + ", doorlock=" + doorlock + ", livingMovement=" + livingMovement + ", balconyMovenent=" + balconyMovenent + ", createdTime=" + createdTime + ", lastModifiedTime="
                + lastModifiedTime + "]";
    }

    /**
     * @return the gatewayNo
     */
    public int getGatewayNo() {
        return gatewayNo;
    }

    /**
     * @param gatewayNo the gatewayNo to set
     */
    public void setGatewayNo(int gatewayNo) {
        this.gatewayNo = gatewayNo;
    }

    /**
     * @param userEmail the userEmail to set
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFireNo() {
        return fireNo;
    }

    public void setFireNo(String fireNo) {
        this.fireNo = fireNo;
    }

    public String getCustomSensorNoList() {
        return customSensorNoList;
    }

    public void setCustomSensorNoList(String customSensorNoList) {
        this.customSensorNoList = customSensorNoList;
    }

    public String getDoorlockNo() {
        return doorlockNo;
    }

    public void setDoorlockNo(String doorlockNo) {
        this.doorlockNo = doorlockNo;
    }

    public String getLivingMovementNo() {
        return livingMovementNo;
    }

    public void setLivingMovementNo(String livingMovementNo) {
        this.livingMovementNo = livingMovementNo;
    }

    public String getBalconyMovenentNo() {
        return balconyMovenentNo;
    }

    public void setBalconyMovenentNo(String balconyMovenentNo) {
        this.balconyMovenentNo = balconyMovenentNo;
    }

    public String getUserEmail() {
        return userEmail;
    }
    
}
