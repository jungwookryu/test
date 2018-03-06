package com.ht.connected.home.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Users {

	@Id
	@Column(name = "no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int no;
	
	@Column(name = "user_id", nullable = false)
	private String userId; 
	
	@Column(name = "username", nullable = false)
	private String username;
	
	@Column(name = "user_email")
	private String userMail;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "password_set")
	private String passwordSet;
	
	@Column(name = "nick_name")
	private String nickName;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "created_time")
	private long createdTime;
	
	@Column(name = "lastmodified_time")
	private long lastmodifiedTime;
	
	@Column(name = "active")
	private boolean active;
	
	@Column(name = "admin")
	private boolean admin;
	
	@Column(name = "authority")
	private String authority;
	
	@Column(name = "authority_code")
	private String authorityCode;
	
	@Column(name = "authorities")
	private String authorities;
	
	@Column(name = "locale")
	private String locale;
		
	@Column(name = "redirectied_code")
	private String redirectiedCode;
	
	public Users(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	public Users(String userId, String username, String userMail) {
		this.userId = userId;
		this.username = username;
		this.userMail = userMail;
	}

	public Users() {
		// TODO Auto-generated constructor stub
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
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the userMail
	 */
	public String getUserMail() {
		return userMail;
	}

	/**
	 * @param userMail the userMail to set
	 */
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the passwordSet
	 */
	public String getPasswordSet() {
		return passwordSet;
	}

	/**
	 * @param passwordSet the passwordSet to set
	 */
	public void setPasswordSet(String passwordSet) {
		this.passwordSet = passwordSet;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
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
	 * @return the createdTime
	 */
	public String getCreatedTime() {
		Date date = new Date(createdTime);
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy MM dd : hh");
		return df1.format(date);
	}

	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the lastmodifiedTime
	 */
	public String getLastmodifiedTime() {
		Date date = new Date(lastmodifiedTime);
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy MM dd : hh");
		return df1.format(date);
	}

	/**
	 * @param lastmodifiedTime the lastmodifiedTime to set
	 */
	public void setLastmodifiedTime(long lastmodifiedTime) {
		this.lastmodifiedTime = lastmodifiedTime;
	}

	/**
	 * @return the active
	 */
	public boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the admin
	 */
	public boolean getAdmin() {
		return admin;
	}

	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	/**
	 * @return the authority
	 */
	public String getAuthority() {
		return authority;
	}

	/**
	 * @param authority the authority to set
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
	}

	/**
	 * @return the authorityCode
	 */
	public String getAuthorityCode() {
		return authorityCode;
	}

	/**
	 * @param authorityCode the authorityCode to set
	 */
	public void setAuthorityCode(String authorityCode) {
		this.authorityCode = authorityCode;
	}

	/**
	 * @return the authorities
	 */
	public String getAuthorities() {
		return authorities;
	}

	/**
	 * @param authorities the authorities to set
	 */
	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
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
	 * @param redirectiedCode the redirectiedCode to set
	 */
	public void setRedirectiedCode(String redirectiedCode) {
		this.redirectiedCode = redirectiedCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Users [no=" + no + ", userId=" + userId + ", username=" + username + ", userMail=" + userMail
				+ ", password=" + password + ", passwordSet=" + passwordSet + ", nickName=" + nickName + ", token="
				+ token + ", status=" + status + ", createdTime=" + createdTime + ", lastmodifiedTime="
				+ lastmodifiedTime + ", active=" + active + ", admin=" + admin + ", authority=" + authority
				+ ", authorityCode=" + authorityCode + ", authorities=" + authorities + ", locale=" + locale
				+ ", redirectiedCode=" + redirectiedCode + "]";
	}
	
	
	
	

}