package com.ht.connected.home.backend.client.home.sharehome;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "share_home")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShareHome {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    @JsonProperty("no")
    private int no;

    @Column(name = "homeNo")  
    @JsonProperty("homeNo")
    private int homeNo;
    
    @Column(name = "user_no")  
    @JsonProperty("user_no")
    private int userNo;
    
    @Column(name = "role")  
    @JsonProperty("role")
    private String role;

    @Transient
    @JsonProperty("nickname")
    private String nickname;
    
    @Column(name = "status")
    @JsonProperty("status")
    private String status;
    
    @Column(name = "accept_date")
    @JsonProperty("accept_date")
    private Date acceptDate;  

    @Column(name = "created_time")
    @JsonProperty("created_time")
    private Date createdTime;

    @Column(name = "lastmodified_time")
    @JsonProperty("lastmodified_time")
    private Date lastmodifiedTime;

    public ShareHome() {
    }
/**
 * 
 * @param homeNo
 * @param ownerUserNo
 * @param shareUserNo
 * @param ownerUserAor
 * @param status
 */
    public ShareHome(int homeNo, int userNo, String role, String status) {
        this.homeNo = homeNo;
        this.userNo = userNo;
        this.role = role;
        this.status = status;
        this.createdTime = new Date();
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
     * @return the shareNickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param shareNickname the shareNickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
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
     * @return the acceptDate
     */
    public Date getAcceptDate() {
        return acceptDate;
    }

    /**
     * @param acceptDate the acceptDate to set
     */
    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
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
     * @return the homeNo
     */
    public int getHomeNo() {
        return homeNo;
    }

    /**
     * @param homeNo the homeNo to set
     */
    public void setHomeNo(int homeNo) {
        this.homeNo = homeNo;
    }
    /**
     * @return the userNo
     */
    public int getUserNo() {
        return userNo;
    }
    /**
     * @param userNo the userNo to set
     */
    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }
    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }
    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ShareHome [no=" + no + ", homeNo=" + homeNo + ", userNo=" + userNo + ", role=" + role + ", nickname=" + nickname + ", status=" + status + ", acceptDate=" + acceptDate
                + ", createdTime=" + createdTime + ", lastmodifiedTime=" + lastmodifiedTime + "]";
    }

}
