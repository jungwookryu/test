package com.ht.connected.home.backend.device.category.zwave.certi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.common.MqttCommon;
import com.ht.connected.home.backend.controller.mqtt.Message;
import com.ht.connected.home.backend.controller.mqtt.ProducerComponent;
import com.ht.connected.home.backend.device.category.zwave.ZWaveRequest;
import com.ht.connected.home.backend.device.category.zwave.certi.commandclass.AlarmCommandClass;
import com.ht.connected.home.backend.device.category.zwave.certi.commandclass.BasicCommandClass;
import com.ht.connected.home.backend.device.category.zwave.certi.commandclass.BinarySwitchCommandClass;
import com.ht.connected.home.backend.device.category.zwave.certi.commandclass.NetworkManagementBasicCommandClass;
import com.ht.connected.home.backend.device.category.zwave.certi.commandclass.NetworkManagementInclusionCommandClass;
import com.ht.connected.home.backend.device.category.zwave.certi.commandclass.NetworkManagementProxyCommandClass;
import com.ht.connected.home.backend.device.category.zwave.notification.ZwaveCertiNotificationService;
import com.ht.connected.home.backend.service.mqtt.MqttRequest;

@Service
public class ZWaveCertiServiceImpl implements ZWaveCertiService {

	private static final Log logging = LogFactory.getLog(ZWaveCertiServiceImpl.class);
	private ZwaveCertiNotificationService zWaveCertiNotificationService;
	private ZWaveCertiNetworkManagementInclusionService zWaveCertiNetworkManagementInclusionService;
	private ZWaveCertiNetworkManagementBasicService zWaveCertiNetworkManagementBasicService;
	private ZWaveCertiNetworkManagementProxyService zWaveCertiNetworkManagementProxyService;
	private ZWaveCertiBinarySwitchService zWaveCertiBinarySwitchService;

	@Autowired
	@Qualifier(value = "callbackAckProperties")
	Properties callbackAckProperties;

	@Autowired
	ProducerComponent producerRestComponent;

	@Autowired
	public ZWaveCertiServiceImpl(ZwaveCertiNotificationService zWaveCertiNotificationService,
			ZWaveCertiNetworkManagementInclusionService zWaveCertiNetworkManagementInclusionService,
			ZWaveCertiNetworkManagementBasicService zWaveCertiNetworkManagementBasicService,
			ZWaveCertiNetworkManagementProxyService zWaveCertiNetworkManagementProxyService,
			ZWaveCertiBinarySwitchService zWaveCertiBinarySwitchService) {
		this.zWaveCertiNotificationService = zWaveCertiNotificationService;
		this.zWaveCertiNetworkManagementInclusionService = zWaveCertiNetworkManagementInclusionService;
		this.zWaveCertiNetworkManagementBasicService = zWaveCertiNetworkManagementBasicService;
		this.zWaveCertiNetworkManagementProxyService = zWaveCertiNetworkManagementProxyService;
		this.zWaveCertiBinarySwitchService = zWaveCertiBinarySwitchService;
	}

	@Autowired
	@Qualifier("zWaveFunctionProperties")
	Properties zWaveFunctionProperties;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	@Transactional
	public void subscribe(ZWaveRequest zwaveRequest, String payload)
			throws JsonParseException, JsonMappingException, IOException, Exception {

		// binary switch report
		if (zwaveRequest.getClassKey() == BasicCommandClass.INT_ID
				&& zwaveRequest.getCommandKey() == BasicCommandClass.INT_BASIC_REPORT) {
			zWaveCertiNotificationService.subscribe(zwaveRequest, payload);
		}

		// 기기정보
		else if (zwaveRequest.getClassKey() == NetworkManagementProxyCommandClass.INT_ID) {
			zWaveCertiNetworkManagementProxyService.subscribe(zwaveRequest, payload);

		}
		// 기기 모드 0x34결과
		else if (zwaveRequest.getClassKey() == NetworkManagementInclusionCommandClass.INT_ID) {
			zWaveCertiNetworkManagementInclusionService.subscribe(zwaveRequest, payload);
		}
		// 기기 초기화 결과 0x4D/0x07 기기상태값모드 받은 경우
		else if (zwaveRequest.getClassKey() == NetworkManagementBasicCommandClass.INT_ID) {
			zWaveCertiNetworkManagementBasicService.subscribe(zwaveRequest, payload);

		}
		else if (BinarySwitchCommandClass.INT_ID == zwaveRequest.getClassKey()) {
			zWaveCertiBinarySwitchService.subscribe(zwaveRequest, payload);
		}
		// 기기 상태 결과
		else if (zwaveRequest.getClassKey() == AlarmCommandClass.INT_ID) {
			zWaveCertiNotificationService.subscribe(zwaveRequest, payload);
		}
	}

	// 삭제 토픽
	@Override
	@Transactional
	public void publishDelete(MqttRequest mqttRequest) throws JsonProcessingException, InterruptedException {

		HashMap map = new HashMap<>();
		map.put("mode", 1);
		HashMap map1 = new HashMap<>();
		map1.put("set_data", map);
		mqttRequest.setSetData(map1);
		publish(mqttRequest);

	}

	private void publish(MqttRequest mqttRequest) throws JsonProcessingException, InterruptedException {

		publish(MqttCommon.getMqttPublishTopic(mqttRequest), mqttRequest.getSetData());
	}

	private void publish(String topic, HashMap<String, Object> publishPayload)
			throws JsonProcessingException, InterruptedException {
		String payload = objectMapper.writeValueAsString(publishPayload);
		publish(topic, payload);
	}

	private void publish(String topic, String payload) throws InterruptedException {
		Message message = new Message(topic, payload);
		MqttCommon.publish(producerRestComponent, message);
	}

}
