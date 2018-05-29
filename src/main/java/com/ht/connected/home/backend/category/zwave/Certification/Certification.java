package com.ht.connected.home.backend.category.zwave.Certification;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "certification")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Certification {

	@Id
	@Column(name = "no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int no;

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
	private Date uptime;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Certification [no=" + no + ", certificationType=" + certificationType + ", model=" + model + ", serial="
				+ serial + ", direction=" + direction + ", controller=" + controller + ", method=" + method
				+ ", context=" + context + ", version=" + version + ", synctime=" + synctime + ", payload=" + payload
				+ ", uptime=" + uptime + "]";
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
	 * @return the certificationType
	 */
	public String getCertificationType() {
		return certificationType;
	}

	/**
	 * @param certificationType
	 *            the certificationType to set
	 */
	public void setCertificationType(String certificationType) {
		this.certificationType = certificationType;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the serial
	 */
	public String getSerial() {
		return serial;
	}

	/**
	 * @param serial
	 *            the serial to set
	 */
	public void setSerial(String serial) {
		this.serial = serial;
	}

	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}

	/**
	 * @return the controller
	 */
	public String getController() {
		return controller;
	}

	/**
	 * @param controller
	 *            the controller to set
	 */
	public void setController(String controller) {
		this.controller = controller;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the synctime
	 */
	public String getSynctime() {
		return synctime;
	}

	/**
	 * @param synctime
	 *            the synctime to set
	 */
	public void setSynctime(String synctime) {
		this.synctime = synctime;
	}

	/**
	 * @return the payload
	 */
	public String getPayload() {
		return payload;
	}

	/**
	 * @param payload
	 *            the payload to set
	 */
	public void setPayload(String payload) {
		this.payload = payload;
	}

	/**
	 * @return the uptime
	 */
	public Date getUptime() {
		return uptime;
	}

	/**
	 * @param uptime
	 *            the uptime to set
	 */
	public void setUptime(Date uptime) {
		this.uptime = uptime;
	}

}
