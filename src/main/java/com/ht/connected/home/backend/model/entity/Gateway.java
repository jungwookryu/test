package com.ht.connected.home.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gateway")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Gateway {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "no")
	private int no;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "id")
	private String id;

	@Column(name = "connected_control_code")
	private String connectedControlCode;

	@Column(name = "ssid")
	private String ssid;

	@Column(name = "bssid")
	private String bssid;

	@Column(name = "serial")
	private String serial;

	@Column(name = "active")
	private String active;

	@Column(name = "update_no")
	private String updateNo;

	@Column(name = "security_mode")
	private String securityMode;

	@Column(name = "security_event")
	private String securityEvent;

	@Column(name = "security_user_no")
	private String securityUserNo;

	@Column(name = "security_in_time")
	private String securityInTime;

	@Column(name = "security_out_time")
	private String securityOutTime;

	@Column(name = "unread_push_message_count")
	private String unreadPushMessageCount;

	@Column(name = "alive_wakeup")
	private String aliveWakeup;

	@Column(name = "created_user_id")
	private String createdUserId;

	@Column(name = "created_time")
	private String createdTime;

	@Column(name = "lastmodified_time")
	private String lastModifiedTime;

	@Column(name = "description")
	private String description;

	public Gateway() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConnectedControlCode() {
		return connectedControlCode;
	}

	public void setConnectedControlCode(String connectedControlCode) {
		this.connectedControlCode = connectedControlCode;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getBssid() {
		return bssid;
	}

	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getUpdateNo() {
		return updateNo;
	}

	public void setUpdateNo(String updateNo) {
		this.updateNo = updateNo;
	}

	public String getSecurityMode() {
		return securityMode;
	}

	public void setSecurityMode(String securityMode) {
		this.securityMode = securityMode;
	}

	public String getSecurityEvent() {
		return securityEvent;
	}

	public void setSecurityEvent(String securityEvent) {
		this.securityEvent = securityEvent;
	}

	public String getSecurityUserNo() {
		return securityUserNo;
	}

	public void setSecurityUserNo(String securityUserNo) {
		this.securityUserNo = securityUserNo;
	}

	public String getSecurityInTime() {
		return securityInTime;
	}

	public void setSecurityInTime(String securityInTime) {
		this.securityInTime = securityInTime;
	}

	public String getSecurityOutTime() {
		return securityOutTime;
	}

	public void setSecurityOutTime(String securityOutTime) {
		this.securityOutTime = securityOutTime;
	}

	public String getUnreadPushMessageCount() {
		return unreadPushMessageCount;
	}

	public void setUnreadPushMessageCount(String unreadPushMessageCount) {
		this.unreadPushMessageCount = unreadPushMessageCount;
	}

	public String getAliveWakeup() {
		return aliveWakeup;
	}

	public void setAliveWakeup(String aliveWakeup) {
		this.aliveWakeup = aliveWakeup;
	}

	public String getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(String createdUserId) {
		this.createdUserId = createdUserId;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(String lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
