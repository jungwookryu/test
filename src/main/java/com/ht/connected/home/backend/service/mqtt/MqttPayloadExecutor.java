package com.ht.connected.home.backend.service.mqtt;

import com.ht.connected.home.backend.model.entity.Gateway;
import com.ht.connected.home.backend.service.mqtt.MessageArrivedComponent;

/**
 * Executor들의 인터페이스
 * 
 * @author 구정화
 *
 */
public interface MqttPayloadExecutor {

	/**
	 * 수신된 메세지별 executor 정의 요구
	 * @param mqttTopicHandler
	 * @param gateway
	 * @return
	 * @throws Exception
	 */
	Object execute(MessageArrivedComponent mqttTopicHandler, Gateway gateway) throws Exception;
}
