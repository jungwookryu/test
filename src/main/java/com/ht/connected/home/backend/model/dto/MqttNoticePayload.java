package com.ht.connected.home.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MqttNoticePayload implements MqttPayload {

	public static final String register = "register";
	public static final String boot = "boot";

	private String type;

	@JsonProperty("user_email")
	private String userEmail;

	@JsonProperty("macaddress")
	private String macAddress;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

}
