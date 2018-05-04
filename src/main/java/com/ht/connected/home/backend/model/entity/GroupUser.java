package com.ht.connected.home.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "group_user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupUser {
	
	@Id
	@Column(name = "no")
	private String no;

	@Column(name = "group_id")
	private String groupId;

	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "role_id")
	private String roleId;
	
	@Column(name = "created_time")
	private String createdTime;
	
	@Column(name = "lastmodified_time")
	private String lastmodifiedTime;
	
	@Column(name = "group_owner_user_id")
	private String groupOwnerUserId;
	
	@Column(name = "group_gorup_id")
	private String groupGorupId;
	
	@Column(name = "description")
	private String description;

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
	 * @return the roleId
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GroupsUsers [no=" + no + ", groupId=" + groupId + ", userId=" + userId + ", roleId=" + roleId
				+ ", createdTime=" + createdTime + ", lastmodifiedTime=" + lastmodifiedTime + ", groupOwnerUserId="
				+ groupOwnerUserId + ", groupGorupId=" + groupGorupId + ", description=" + description + "]";
	}

}
