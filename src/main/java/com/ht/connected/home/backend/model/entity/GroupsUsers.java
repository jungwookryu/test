package com.ht.connected.home.backend.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "groups_users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupsUsers {
	
	@NotNull
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

}
