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
@Table(name = "home")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShareHome {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    @JsonProperty("no")
    private int no;

    @Column(name = "home_no")  
    @JsonProperty("home_no")
    private int home_no;
    
    @Column(name = "owner_user_no")  
    @JsonProperty("owner_user_no")
    private int ownerUserNo;

    @Column(name = "share_user_email")
    @JsonProperty("share_user_email")
    private String shareUserEmail;

    @Column(name = "share_user_aor")
    @JsonProperty("share_user_aor")
    private String shareUserAor;

    @Column(name = "share_nickname")
    @JsonProperty("share_nickname")
    private String shareNickname;
    
    @Column(name = "shared_status")
    @JsonProperty("shared_status")
    private String sharedStatus;
    
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
     * @return the home_no
     */
    public int getHome_no() {
        return home_no;
    }

    /**
     * @param home_no the home_no to set
     */
    public void setHome_no(int home_no) {
        this.home_no = home_no;
    }

    /**
     * @return the ownerUserNo
     */
    public int getOwnerUserNo() {
        return ownerUserNo;
    }

    /**
     * @param ownerUserNo the ownerUserNo to set
     */
    public void setOwnerUserNo(int ownerUserNo) {
        this.ownerUserNo = ownerUserNo;
    }

    /**
     * @return the shareUserEmail
     */
    public String getShareUserEmail() {
        return shareUserEmail;
    }

    /**
     * @param shareUserEmail the shareUserEmail to set
     */
    public void setShareUserEmail(String shareUserEmail) {
        this.shareUserEmail = shareUserEmail;
    }

    /**
     * @return the shareUserAor
     */
    public String getShareUserAor() {
        return shareUserAor;
    }

    /**
     * @param shareUserAor the shareUserAor to set
     */
    public void setShareUserAor(String shareUserAor) {
        this.shareUserAor = shareUserAor;
    }

    /**
     * @return the shareNickname
     */
    public String getShareNickname() {
        return shareNickname;
    }

    /**
     * @param shareNickname the shareNickname to set
     */
    public void setShareNickname(String shareNickname) {
        this.shareNickname = shareNickname;
    }

    /**
     * @return the sharedStatus
     */
    public String getSharedStatus() {
        return sharedStatus;
    }

    /**
     * @param sharedStatus the sharedStatus to set
     */
    public void setSharedStatus(String sharedStatus) {
        this.sharedStatus = sharedStatus;
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
    public Date getCreated_time() {
        return createdTime;
    }

    /**
     * @param createdTime the createdTime to set
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return the lastModifiedTime
     */
    public Date getLastmodified_time() {
        return lastmodifiedTime;
    }

    /**
     * @param lastModifiedTime the lastModifiedTime to set
     */
    public void setLastmodifiedTime(Date lastmodifiedTime) {
        this.lastmodifiedTime = lastmodifiedTime;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ShareHome [no=" + no + ", home_no=" + home_no + ", ownerUserNo=" + ownerUserNo + ", shareUserEmail=" + shareUserEmail + ", shareUserAor=" + shareUserAor + ", shareNickname="
                + shareNickname + ", sharedStatus=" + sharedStatus + ", acceptDate=" + acceptDate + ", createdTime=" + createdTime + ", lastModifiedTime=" + lastmodifiedTime + "]";
    }

        
}
