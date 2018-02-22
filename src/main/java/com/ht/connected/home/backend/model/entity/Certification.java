package com.ht.connected.home.backend.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;


@Entity
@Table(name = "certification")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Certification {

	@Id
	@NotNull
	@Column(name = "no")
	private String no;
	
	@Column(name = "certification_type")
	private String certificationType;
	
	@Column(name = "model")
	private String model;
	
	@Column(name = "serial")
	private String serial;
	
	@Column(name = "direction")
	private String direction;
	
	@Column(name = "controller")
	private String controller;
	
	@Column(name = "method")
	private String method;
	
	@Column(name = "context")
	private String context;
	
	@Column(name = "version")
	private String version;
	
	@Column(name = "synctime")
	private String synctime;
	
	@Column(name = "payload")
	private String payload;
	
	@Column(name = "uptime")
	private String uptime;
}
