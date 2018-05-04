package com.ht.connected.home.backend.service.mqtt;

import static java.util.Objects.isNull;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.model.dto.MqttMessageArrived;
import com.ht.connected.home.backend.model.entity.Gateway;
import com.ht.connected.home.backend.model.entity.UserGateway;
import com.ht.connected.home.backend.model.entity.User;
import com.ht.connected.home.backend.repository.GateWayRepository;
import com.ht.connected.home.backend.repository.UserGatewayRepository;
import com.ht.connected.home.backend.repository.UsersRepository;

/**
 * 호스트 Manager 컨트롤러 Notice Payload 메세지 바디 처리 로직
 * 
 * @author 구정화
 *
 */
@Service
@Scope(value = "prototype")
public class MqttNoticeExecutor implements MqttPayloadExecutor {

	private final static String REGISTER = "register";
	private static String BOOT = "boot";

	@Autowired
	BeanFactory beanFactory;

	@Autowired
	UserGatewayRepository userGatewayRepository;

	@Autowired
	UsersRepository userRepository;

	@Autowired
	GateWayRepository gatewayRepository;

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * 호스트 등록/부팅 메세지 executor type 이 register 일 경우만 처리 type 이 boot 일 경우에 대한 디비 저정은
	 * 추가될수 있음
	 * 
	 * @param mqttTopicHandler
	 * @param gateway
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(MqttMessageArrived mqttMessageArrived, Gateway gateway) throws Exception {
		if (mqttMessageArrived.getStrPayload().length() > 0) {
			HashMap<String, String> map = objectMapper.readValue(mqttMessageArrived.getStrPayload(), HashMap.class);
			List<User> users = userRepository.findByUserEmail(map.get("user_email"));
			User user = users.get(0);
			if (map.get("type").equals(REGISTER) && isNull(gateway)) {
				gateway = updateGateway(mqttMessageArrived, gateway, map);
				updateUserGateway(gateway, user.getNo());
				String topic = String.format("/server/app/%s/%s/manager/noti", gateway.getModel(),
						gateway.getSerial());
				MqttPahoMessageHandler messageHandler = (MqttPahoMessageHandler) beanFactory.getBean("MqttOutbound");
//				messageHandler.setDefaultTopic(topic);
//				MqttGateway mqttGateway = beanFactory.getBean(MqttGateway.class);
//				mqttGateway.sendToMqtt("");
			} else if (map.get("type").equals(BOOT) && !isNull(gateway)) {

			}
		}
		return null;
	}

	/**
	 * 호스트 정보 업데이트
	 * 
	 * @param messageArrived
	 * @param gateway
	 * @return
	 */
	private Gateway updateGateway(MqttMessageArrived mqttMessageArrived, Gateway gateway, HashMap<String, String> map) {
		if (isNull(gateway)) {
			gateway = new Gateway();
		}
		gateway.setSerial(mqttMessageArrived.getSerial());
		gateway.setModel(mqttMessageArrived.getModel());
		gateway.setBssid(map.get("macaddress"));
		gatewayRepository.save(gateway);
		return gateway;
	}

	/**
	 * 호스트를 초기 등록할경우 마스터 유저와 호스트 맵핑
	 * 
	 * @param jsonObject
	 * @param gateway
	 * @param userNo
	 */
	private void updateUserGateway(Gateway gateway, int userNo) {
		UserGateway userGateway = userGatewayRepository.findByUserNoAndGatewayNo(userNo, gateway.getNo());
		if (isNull(userGateway)) {
			userGateway = new UserGateway();
		}
		userGateway.setGatewayNo(gateway.getNo());
		userGateway.setUserNo(userNo);
		userGateway.setGroupRole("master");
		userGatewayRepository.save(userGateway);
	}
}
