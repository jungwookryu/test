package com.ht.connected.home.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "device")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Device {

	@Id
	@NotNull
	@Column(name = "no")
	private String no;
	
	@Column(name = "gateway_no")
	private String gatewayNo;
	
	@Column(name = "device_type")
	private String deviceType;
	
	@Column(name = "device_seq")
	private String deviceSeq;
	
	@Column(name = "nickname")
	private String nickname;
	
	@Column(name = "active")
	private String active;
	
	@Column(name = "created_time")
	private String createdTime;
	
	@Column(name = "lastmodified_time")
	private String lastmodifiedTime;

	/**
	 * @return the no
	 */
	public String getNo() {
		return no;
	}

	/**
	 * @param no the no to set
	 */
	public void setNo(String no) {
		this.no = no;
	}

	/**
	 * @return the gatewayNo
	 */
	public String getGatewayNo() {
		return gatewayNo;
	}

	/**
	 * @param gatewayNo the gatewayNo to set
	 */
	public void setGatewayNo(String gatewayNo) {
		this.gatewayNo = gatewayNo;
	}

	/**
	 * @return the deviceType
	 */
	public String getDeviceType() {
		return deviceType;
	}

	/**
	 * @param deviceType the deviceType to set
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	/**
	 * @return the deviceSeq
	 */
	public String getDeviceSeq() {
		return deviceSeq;
	}

	/**
	 * @param deviceSeq the deviceSeq to set
	 */
	public void setDeviceSeq(String deviceSeq) {
		this.deviceSeq = deviceSeq;
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
	 * @return the active
	 */
	public String getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}

	/**
	 * @return the createdTime
	 */
	public String getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the lastmodifiedTime
	 */
	public String getLastmodifiedTime() {
		return lastmodifiedTime;
	}

	/**
	 * @param lastmodifiedTime the lastmodifiedTime to set
	 */
	public void setLastmodifiedTime(String lastmodifiedTime) {
		this.lastmodifiedTime = lastmodifiedTime;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Device [no=" + no + ", gatewayNo=" + gatewayNo + ", deviceType=" + deviceType + ", deviceSeq="
				+ deviceSeq + ", nickname=" + nickname + ", active=" + active + ", createdTime=" + createdTime
				+ ", lastmodifiedTime=" + lastmodifiedTime + "]";
	}
	
	
}
