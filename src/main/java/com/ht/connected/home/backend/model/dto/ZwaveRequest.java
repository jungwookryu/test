package com.ht.connected.home.backend.model.dto;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * zwave 요청시 토픽 URI 경로 구분자(/)로 분할하여 getter통해 쉽게 사용하기 위한 클래스
 * @author 구정화
 *
 */
public class ZwaveRequest {

	private static final Log logging = LogFactory.getLog(ZwaveRequest.class);

	private String email;

	private String serialNo;

	private Integer nodeId;

	private Integer endpointId;

	private String securityOption;

	private String classKey;

	private String commandKey;

	private String version;

	/**
	 * 경로를 배열로 받을경우 생성자
	 * @param topic
	 */
	public ZwaveRequest(String[] topic) {
		this.serialNo = topic[2];
		this.classKey = topic[6];
		this.commandKey = topic[7];
	}

	/**
	 * ZwaveController에서 생성할 경우 생성자
	 * @param req
	 * @param classKey
	 * @param commandKey
	 * @param version
	 */
	public ZwaveRequest(HashMap<String, Object> req, String classKey, String commandKey, String version) {
		this.email = req.get("user_email").toString();
		this.serialNo = req.get("serialNo").toString();
		this.nodeId = Integer.valueOf(req.get("nodeId").toString());
		this.endpointId = Integer.valueOf(req.get("endpointId").toString());
		this.securityOption = req.get("option").toString();
		this.classKey = classKey;
		this.commandKey = commandKey;
		this.version = version;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public String getClassKey() {
		return classKey;
	}

	public String getCommandKey() {
		return commandKey;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * mqtt publish 토픽 생성
	 * @param topicLeadingPath
	 * @return
	 */
	public String getMqttPublishTopic(String topicLeadingPath) {
		String[] segments = new String[] { "device", "zwave", "certi", classKey, commandKey, version,
				getHexString(nodeId), getHexString(endpointId), securityOption };
		String topic = topicLeadingPath + String.join("/", segments); 
		logging.info("====================== ZWAVE PROTO MQTT PUBLISH TOPIC ======================");
		logging.info(topic);
		return topic;
	}

	/**
	 * 호스트에 전달하는 토픽에 Node ID와 Endpoint ID는 헥사값 
	 * @param number
	 * @return
	 */
	private String getHexString(Integer number) {
		return "0x" + String.format("%2s", Integer.toHexString(number)).replace(' ', '0');
	}

}