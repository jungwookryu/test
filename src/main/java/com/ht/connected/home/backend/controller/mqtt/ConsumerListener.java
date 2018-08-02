package com.ht.connected.home.backend.controller.mqtt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ht.connected.home.backend.device.category.CategoryActive;
import com.ht.connected.home.backend.device.category.gateway.GatewayService;
import com.ht.connected.home.backend.device.category.ir.IRService;
import com.ht.connected.home.backend.device.category.zwave.ZWaveRequest;
import com.ht.connected.home.backend.device.category.zwave.ZWaveService;
import com.ht.connected.home.backend.device.category.zwave.certi.ZWaveCertiService;
import com.ht.connected.home.backend.service.mqtt.Target;
 
@Component
public class ConsumerListener {


	static final Integer GATEWAY_MIN_LENGTH = 4;
	static final Integer FROM = 1;
	static final Integer TO = 2;
	static final Integer PRODUCT_NAME = 3;
	static final Integer SERIAL_ID = 4;
	static final Integer SERVICE_NAME = 5;
	static final Integer ACTIVE_CODE = 6;

	@Autowired
	private ZWaveService zwaveService;

	@Autowired
	private ZWaveCertiService zWaveCertiService;

	@Autowired
	private GatewayService gateWayService;

	@Autowired
	private IRService irService;

	private static final Logger logger = LoggerFactory.getLogger(ConsumerListener.class);


	/**
	 * mqtt received component
	 * 
	 * @param message
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws Exception
	 */

	public void receiveMessage(Message message)
			throws JsonParseException, JsonMappingException, IOException, Exception {
		String topic = String.valueOf(message.getMessageType());
		String payload = String.valueOf(message.getMessageBody());

		logger.info("messageArrived: Topic=" + topic + ", Payload=" + payload);
		String[] topicSplited = topic.trim().replace(".", ";").split(";");
		// message topic 4개이상이어야 gateway관련 메세지임.
		if (topicSplited.length <= GATEWAY_MIN_LENGTH) {
			return;
		}
		// 서버에서 보낸것이 아닐경우만 subscribe함.
		if (Target.server.name().equals(topicSplited[FROM].toString())) {
			return;
		}

		// 서버에서 보낸것이 아닐경우만 subscribe함.
		if (CategoryActive.gateway.manager.name().equals(topicSplited[SERVICE_NAME].toString())) { // manager service
			logger.info(topicSplited[SERVICE_NAME].toString() + " subStart");
			gateWayService.subscribe(topic, payload);
		} else if (CategoryActive.gateway.zwave.name().equals(topicSplited[SERVICE_NAME].toString())) { // zwave service
			ZWaveRequest zwaveRequest = new ZWaveRequest(topicSplited);
			if (CategoryActive.zwave.certi.name().equals(topicSplited[6].toString())) {
				zWaveCertiService.subscribe(zwaveRequest, payload);
			} else {
				zwaveService.subscribe(zwaveRequest, payload);
			}
		} else if (CategoryActive.gateway.ir.name().equals(topicSplited[SERVICE_NAME].toString().trim())) { // ir service
			irService.subscribe(topicSplited, payload);
		}
	}

}
