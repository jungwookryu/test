package com.ht.connected.home.backend.sip.message.model.dto;

public class SipSharedDeviceDto {

	private String devSerial;
	private String devType;
	private String ownerAccount;
	private String devAlias;
	private String sharedStatus;

	public String getDevSerial() {
		return devSerial;
	}

	public void setDevSerial(String devSerial) {
		this.devSerial = devSerial;
	}

	public String getDevType() {
		return devType;
	}

	public void setDevType(String devType) {
		this.devType = devType;
	}

	public String getOwnerAccount() {
		return ownerAccount;
	}

	public void setOwnerAccount(String ownerAccount) {
		this.ownerAccount = ownerAccount;
	}

	public String getDevAlias() {
		return devAlias;
	}

	public void setDevAlias(String devAlias) {
		this.devAlias = devAlias;
	}

	public String getSharedStatus() {
		return sharedStatus;
	}

	public void setSharedStatus(String sharedStatus) {
		this.sharedStatus = sharedStatus;
	}

}
