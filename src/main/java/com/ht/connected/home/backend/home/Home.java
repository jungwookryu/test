package com.ht.connected.home.backend.home;

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
public class Home {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    @JsonProperty("no")
    private Integer no;

    @Column(name = "owner_user_no")
    @JsonProperty("owner_user_no")
    private Integer ownerUserNo;

    @Column(name = "owner_user_email")
    @JsonProperty("owner_user_email")
    private String ownerUserEmail;

    @Column(name = "owner_user_aor")
    @JsonProperty("owner_user_aor")
    private String ownerUserAor;

    @Column(name = "nickname")
    @JsonProperty("nickname")
    private String nickname;

    @Column(name = "created_time")
    @JsonProperty("created_time")
    private Date createdTime;

    @Column(name = "lastmodified_time")
    @JsonProperty("lastmodified_time")
    private Date lastModifiedTime;

    public Home(int ownerUserNo, String ownerUserEmail, String ownerUserAor, String nickname, Date createdTime) {
        this.ownerUserNo = ownerUserNo;
        this.ownerUserEmail = ownerUserEmail;
        this.ownerUserAor = ownerUserAor;
        this.nickname = nickname;
        this.createdTime = createdTime;
    }

    /**
     * @return the no
     */
    public Integer getNo() {
        return no;
    }

    /**
     * @param no the no to set
     */
    public void setNo(Integer no) {
        this.no = no;
    }

    /**
     * @return the ownerUserNo
     */
    public Integer getOwnerUserNo() {
        return ownerUserNo;
    }

    /**
     * @param ownerUserNo the ownerUserNo to set
     */
    public void setOwnerUserNo(Integer ownerUserNo) {
        this.ownerUserNo = ownerUserNo;
    }

    /**
     * @return the ownerUserEmail
     */
    public String getOwnerUserEmail() {
        return ownerUserEmail;
    }

    /**
     * @param ownerUserEmail the ownerUserEmail to set
     */
    public void setOwnerUserEmail(String ownerUserEmail) {
        this.ownerUserEmail = ownerUserEmail;
    }

    /**
     * @return the ownerUserAor
     */
    public String getOwnerUserAor() {
        return ownerUserAor;
    }

    /**
     * @param ownerUserAor the ownerUserAor to set
     */
    public void setOwnerUserAor(String ownerUserAor) {
        this.ownerUserAor = ownerUserAor;
    }

    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
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
     * @return the lastModifiedTime
     */
    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    /**
     * @param lastModifiedTime the lastModifiedTime to set
     */
    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

}
