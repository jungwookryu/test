package com.ht.connected.home.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ir")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IR {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "no")
	private String no;
	
	@Column(name = "schema_no")
	private String schemaNo;
	
	@Column(name = "ir_name")
	private String irName;
	
	@Column(name = "ir_type")
	private String irType;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "created_time")
	private String createdTime;
	
	@Column(name = "lastmodified_time")
	private String lastmodifiedTime;
	
	@Column(name = "action")
	private String action;
	
	@Column(name = "format")
	private String format;
}
