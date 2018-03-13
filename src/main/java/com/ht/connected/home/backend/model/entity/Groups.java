package com.ht.connected.home.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "groups")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Groups {
	
	@Id
	@Column(name = "no")
	private String no;
	
	@Column(name = "group_id")
	private String groupId;
	
	@Column(name = "group_name")
	private String groupName;
	
	@Column(name = "created_time")
	private String created_time;
	
	@Column(name = "lastmodified_time")
	private String lastmodifiedTime;
	
	@Column(name = "group_owner_user_id")
	private String groupOwnerUserId;
	
	@Column(name = "group_gorup_id")
	private String groupGorupId;
	
	@Column(name = "gateway_no")
	private String gatewayNo;
	
	@Column(name = "description")
	private String description;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Groups [no=" + no + ", groupId=" + groupId + ", groupName=" + groupName + ", created_time="
				+ created_time + ", lastmodifiedTime=" + lastmodifiedTime + ", groupOwnerUserId=" + groupOwnerUserId
				+ ", groupGorupId=" + groupGorupId + ", gatewayNo=" + gatewayNo + ", description=" + description + "]";
	}

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
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the created_time
	 */
	public String getCreated_time() {
		return created_time;
	}

	/**
	 * @param created_time the created_time to set
	 */
	public void setCreated_time(String created_time) {
		this.created_time = created_time;
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

	/**
	 * @return the groupOwnerUserId
	 */
	public String getGroupOwnerUserId() {
		return groupOwnerUserId;
	}

	/**
	 * @param groupOwnerUserId the groupOwnerUserId to set
	 */
	public void setGroupOwnerUserId(String groupOwnerUserId) {
		this.groupOwnerUserId = groupOwnerUserId;
	}

	/**
	 * @return the groupGorupId
	 */
	public String getGroupGorupId() {
		return groupGorupId;
	}

	/**
	 * @param groupGorupId the groupGorupId to set
	 */
	public void setGroupGorupId(String groupGorupId) {
		this.groupGorupId = groupGorupId;
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
}
