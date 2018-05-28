package com.ht.connected.home.backend.emergency;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(name = "magnetic_detector")
    @JsonProperty("magnetic_detector")
    String magneticDetector;
    
    @Column(name = "movement_sensor")
    @JsonProperty("movement_sensor")
    String movementSensor;
    
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getGatewayNo() {
        return gatewayNo;
    }

    public void setGatewayNo(int gatewayNo) {
        this.gatewayNo = gatewayNo;
    }

    public String getMagneticDetector() {
        return magneticDetector;
    }

    public void setMagneticDetector(String magneticDetector) {
        this.magneticDetector = magneticDetector;
    }

    public String getMovementSensor() {
        return movementSensor;
    }

    public void setMovementSensor(String movementSensor) {
        this.movementSensor = movementSensor;
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

    public String getEmergencyType() {
        return emergencyType;
    }

    @Override
    public String toString() {
        return "UserEmergency [no=" + no + ", userNo=" + userNo + ", userEmail=" + userEmail + ", gatewayNo=" + gatewayNo + ", emergencyType=" + emergencyType + ", description=" + description
                + ", magneticDetector=" + magneticDetector + ", movementSensor=" + movementSensor + ", createdTime=" + createdTime + ", lastModifiedTime=" + lastModifiedTime + "]";
    }

}
