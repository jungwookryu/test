package com.ht.connected.home.backend.user;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Project : HT-CONNECTED-HOME-SERVER Package :
 * com.ht.connected.home.front.model.entity File : Users.java Description : 사용자
 * 테이블 entity no, user_id, username, password, password_set, nick_name, token,
 * status, created_time, lastmodified_time active, admin, authority,
 * authorities, locale, redirectied_code
 * 
 * @auther : ijlee
 * @version : 1.0
 * @since : 2018.02.14
 */
@Entity
@Table(name = "user")
public class User {

	@Id
	@Column(name = "no")
	@JsonProperty("no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int no;

	@Column(name = "user_email", nullable=false, unique = true)
	@JsonProperty("user_email")
	private String userEmail;

	@Column(name = "password")
	@JsonProperty("password")
	private String password;

	@Column(name = "nick_name")
	@JsonProperty("nickname")
	private String nickName;

	@Column(name = "push_token")
	@JsonProperty("push_token")
	private String pushToken;

	@Column(name = "status")
	@JsonProperty("status")
	private String status;

	@Column(name = "created_time")
	@JsonProperty("created_time")
	private Date createdTime;

	@Column(name = "lastmodified_time")
	@JsonProperty("lastmodified_time")
	private Date lastmodifiedTime;

	@Column(name = "active")
	@JsonProperty("active")
	private int active;

	@Column(name = "authority")
	@JsonProperty("authority")
	private String authority;

	@Column(name = "locale")
	@JsonProperty("locale")
	private String locale;

	@Column(name = "redirectied_code")
	@JsonProperty("redirectied_code")
	private String redirectiedCode;

	@Column(name = "connected_type")
	@JsonProperty("connected_type")
	private String connectedType;

	@Column(name = "push_type")
	@JsonProperty("push_type")
	private int pushType;
	
	public User(String userEmail, String password) {
		this.userEmail = userEmail;
	}

	public User(String userEmail) {
		this.userEmail = userEmail;
	}

	public User() {
	}

	/**
	 * @return the no
	 */
	public int getNo() {
		return no;
	}

	/**
	 * @param no
	 *            the no to set
	 */
	public void setNo(int no) {
		this.no = no;
	}


	/**
	 * @return the password
	 */
	public String getRePassword() {
		return password;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName
	 *            the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return the token
	 */
	public String getPushToken() {
		return this.pushToken;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String pushToken) {
		this.pushToken = pushToken;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the createdTime
	 */
	public String getCreatedTime(Date createdTime) {
		// Date date = new Date(createdTime);
		SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd hh:ss");
		return df1.format(createdTime);
	}

	/**
	 * @param createdTime
	 *            the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the lastmodifiedTime
	 */
	public String getLastmodifiedTime(Date lastmodifiedTime) {
		// Date date = new Date(lastmodifiedTime);
		SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd hh:ss");
		return df1.format(lastmodifiedTime);
	}

	/**
	 * @param lastmodifiedTime
	 *            the lastmodifiedTime to set
	 */
	public void setLastmodifiedTime(Date lastmodifiedTime) {
		this.lastmodifiedTime = lastmodifiedTime;
	}

	/**
	 * @return the active
	 */
	public int getActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(int active) {
		this.active = active;
	}

	/**
	 * @return the authority
	 */
	public String getAuthority() {
		return authority;
	}

	/**
	 * @param authority
	 *            the authority to set
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
	}


	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * @return the redirectiedCode
	 */
	public String getRedirectiedCode() {
		return redirectiedCode;
	}

	/**
	 * @param redirectiedCode
	 *            the redirectiedCode to set
	 */
	public void setRedirectiedCode(String redirectiedCode) {
		this.redirectiedCode = redirectiedCode;
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
	 * @return the connectedType
	 */
	public String getConnectedType() {
		return connectedType;
	}

	/**
	 * @param connectedType the connectedType to set
	 */
	public void setConnectedType(String connectedType) {
		this.connectedType = connectedType;
	}

	/**
	 * @return the pushType
	 */
	public int getPushType() {
		return pushType;
	}

	/**
	 * @param pushType the pushType to set
	 */
	public void setPushType(int pushType) {
		this.pushType = pushType;
	} 

	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * @return the lastmodifiedTime
	 */
	public Date getLastmodifiedTime() {
		return lastmodifiedTime;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param pushToken the pushToken to set
	 */
	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}

    @Override
    public String toString() {
        return "User [no=" + no + ", userEmail=" + userEmail + ", password=" + "dummypassword" + ", nickName=" + nickName + ", pushToken=" + pushToken + ", status=" + status + ", createdTime=" + createdTime
                + ", lastmodifiedTime=" + lastmodifiedTime + ", active=" + active + ", authority=" + authority + ", locale=" + locale + ", redirectiedCode=" + redirectiedCode + ", connectedType="
                + connectedType + ", pushType=" + pushType + "]";
    }

}