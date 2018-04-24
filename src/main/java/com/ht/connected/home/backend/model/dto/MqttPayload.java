package com.ht.connected.home.backend.model.dto;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MqttPayload {

	@JsonProperty(value = "result_code")
	private int resultCode;

	@JsonProperty(value = "result_msg")
	private String resultMessage;

	@JsonProperty(value = "result_data")
	private HashMap<String, Object> resultData;

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public HashMap<String, Object> getResultData() {
		return resultData;
	}

	public void setResultData(HashMap<String, Object> resultData) {
		this.resultData = resultData;
	}

}
