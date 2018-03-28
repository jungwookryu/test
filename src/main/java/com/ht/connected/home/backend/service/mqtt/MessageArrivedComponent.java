package com.ht.connected.home.backend.service.mqtt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.model.dto.MqttNoticePayload;
import com.ht.connected.home.backend.model.dto.MqttPayload;

/**
 * 호스트 Manager 컨트롤러 메세지 MQTT 수신 분기 처리
 * 
 * @author 구정화
 *
 */
@Component
@Scope(value = "prototype")
public class MessageArrivedComponent {

	/**
	 * executor들을 토픽과 맵핑하기 위한 해쉬맵 변수에 정의
	 */
	@SuppressWarnings("rawtypes")
	static private HashMap<String, Class[]> executors = new HashMap<>();

	static {

		executors.put("app/manager/noti",
				new Class[] { MqttNoticePayload.class, MqttNoticeExecutor.class });

	}

	@Autowired
	private BeanFactory beanFactory;

	@Autowired
	private ObjectMapper objectMapper;
	
	
	/**
	 * === 토픽과 클래스 맵버변수에 대한 설명 ===
	 * /wcs-w1001/제품번호/app/manager/noti
	 * /모델명/제품번호(serial)/[direction]/[controller(호스트 칩셋)]/[method]/[context]
	 */

	private String[] topic;
	private String direction;
	private String controller;
	private String serial;
	private String method;
	private String context;
	private String timestamp;
	private String strPayload;
	private MqttPayload payload = null;
	@SuppressWarnings("rawtypes")
	private Class[] executor;

	/**
	 * 토픽과 메세지를 인자로 초기화
	 * 
	 * @param topic
	 * @param strPayload
	 */
	public void init(String topic, String strPayload) {
		this.topic = topic.split("/");
		this.serial = this.topic[2];
		this.direction = this.topic[3];
		this.controller = this.topic[4];
		this.method = this.topic[5];
		if (this.topic.length > 6) {
			this.context = this.topic[6];
		}
		if (this.topic.length > 7) {
			this.timestamp = this.topic[7];
		}
		this.strPayload = strPayload;
	}

	/**
	 * MQTT 메세지 payload를 맵핑된 객체로 생성하여 반환
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public MqttPayload getPayload() {
		if (payload == null) {
			try {
				payload = (MqttPayload) objectMapper.readValue(strPayload, executor[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return payload;
	}

	public void setPayload(MqttPayload payload) {
		this.payload = payload;
	}

	/**
	 * Executor 선택하여 반환
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public MqttPayloadExecutor getExecutor() {
		MqttPayloadExecutor serviceExecutor = null;
		executor = executors.get(getControllerMethodContext());

		if (executor == null) {
			executor = executors.get(getControllerMethod());
		}
		if (executor != null) {

			serviceExecutor = (MqttPayloadExecutor) beanFactory.getBean(executor[1]);
		}
		return serviceExecutor;
	}

	/**
	 * 수신된 토픽 후반부 만 생성하여 반환
	 * 
	 * @return
	 */
	private String getControllerMethod() {
		List<String> segments = new ArrayList<>();
		segments.add(direction);
		segments.add(controller);
		segments.add(method);
		return String.join("/", segments);
	}

	/**
	 * 수신된 토픽 후반부 만 생성하여 반환
	 * 
	 * @return
	 */
	private String getControllerMethodContext() {
		List<String> segments = new ArrayList<>();
		segments.add(direction);
		segments.add(controller);
		segments.add(method);
		if (context != null) {
			segments.add(context);
		}
		return String.join("/", segments);
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getContext() {
		return context;
	}

	public String getSerial() {
		return serial;
	}
}
