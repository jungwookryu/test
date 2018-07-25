package com.ht.connected.home.backend.service.mqtt;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MqttPayload {

	@JsonProperty(value = "result_code")
	private int resultCode;

	@JsonProperty(value = "result_msg")
	private String resultMessage;

	@JsonProperty(value = "result_data")
	private HashMap<String, Object> resultData;
	
	@JsonProperty(value = "set_data")
    private HashMap<String, Object> setData;

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

    /**
     * @return the setData
     */
    public HashMap<String, Object> getSetData() {
        return setData;
    }

    /**
     * @param setData the setData to set
     */
    public void setSetData(HashMap<String, Object> setData) {
        this.setData = setData;
    }

}
