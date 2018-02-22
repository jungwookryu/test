package com.ht.connected.home.backend.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

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
}
