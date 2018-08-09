package com.ht.connected.home.backend.device.category.zwave.certi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ht.connected.home.backend.device.category.zwave.ZWaveRequest;
import com.ht.connected.home.backend.service.mqtt.MqttRequest;

/**
 * zwave certi 서비스 인터페이스
 * 
 * @author ijlee
 */
public interface ZWaveCertiService {

	void subscribe(ZWaveRequest zwaveRequest, String payload) throws Exception;

	void publishDelete(MqttRequest mqttRequest) throws JsonProcessingException, InterruptedException;

}
