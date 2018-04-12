package com.ht.connected.home.backend.model.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 토픽을 세그먼트로 나눠 멤버변수에 저장하여 사용하기 쉽게
 * 
 * @author 구정화
 *
 */
public class MqttMessageArrived {

	/**
	 * === 토픽과 클래스 맵버변수에 대한 설명 === /wcs-w1001/제품번호/app/manager/noti
	 * /모델명/제품번호(serial)/[direction]/[controller(호스트 칩셋)]/[method]/[context]
	 */
	private String[] topic;
	private String model;
	private String direction;
	private String controller;
	private String serial;
	private String method;
	private String context;
	private String timestamp;
	private String strPayload;

	public MqttMessageArrived(String topic, String strPayload) {
		this.topic = topic.split("/");
		if (this.topic.length > 6) {
			this.model = this.topic[3];
			this.serial = this.topic[4];
			this.direction = this.topic[2];
			this.controller = this.topic[5];
			this.method = this.topic[6];
			if (this.topic.length > 7) {
				this.context = this.topic[7];
			}
			if (this.topic.length > 8) {
				this.timestamp = this.topic[8];
			}
			this.strPayload = strPayload;
		}
	}

	/**
	 * 수신된 토픽 후반부 만 생성하여 반환
	 * 
	 * @return
	 */
	public String getControllerMethod() {
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
	public String getControllerMethodContext() {
		List<String> segments = new ArrayList<>();
		segments.add(direction);
		segments.add(controller);
		segments.add(method);
		if (context != null) {
			segments.add(context);
		}
		return String.join("/", segments);
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getStrPayload() {
		return strPayload;
	}

	public void setStrPayload(String strPayload) {
		this.strPayload = strPayload;
	}

}
