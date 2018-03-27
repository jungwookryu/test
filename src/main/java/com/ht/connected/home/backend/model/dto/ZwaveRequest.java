package com.ht.connected.home.backend.model.dto;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:mqtt.properties")
public class ZwaveRequest {
	
	@Value("${spring.mqtt.channel.gateway}")
	String springMqttChannelGateway;

	private static final Log logging = LogFactory.getLog(ZwaveRequest.class);

	private String email;

	private String serialNo;

	private Integer nodeId;

	private Integer endpointId;

	private String securityOption;

	private String classKey;

	private String commandKey;

	private String version;

	public ZwaveRequest(String[] topic) {
		this.serialNo = topic[2];
		this.classKey = topic[6];
		this.commandKey = topic[7];
	}

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

	public String getMqttPublishTopic() {
		String[] segments = new String[] { "device", "zwave", "certi", classKey, commandKey, version,
				getHexString(nodeId), getHexString(endpointId), securityOption };
		String topic = springMqttChannelGateway.replace("+", serialNo).replace("#", String.join("/", segments));
		logging.info("====================== ZWAVE PROTO MQTT PUBLISH TOPIC ======================");
		logging.info(topic);
		return topic;
	}

	private String getHexString(Integer number) {
		return "0x" + String.format("%2s", Integer.toHexString(number)).replace(' ', '0');
	}

}