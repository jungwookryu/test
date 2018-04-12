package com.ht.connected.home.backend.service.impl.zwave.handler;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ht.connected.home.backend.model.dto.MqttPayload;
import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.service.ZwaveService;
import com.ht.connected.home.backend.service.impl.zwave.ZwaveCommandKey;
import com.ht.connected.home.backend.service.impl.zwave.ZwaveDefault;

@Service
@Scope(value = "prototype")
public class NetworkManagementProxy extends ZwaveDefault implements ZwaveService {

	@Override
	public Object execute(HashMap<String, Object> req, ZwaveRequest zwaveRequest, boolean isCert) throws JsonProcessingException {
		this.isCert = isCert;
		if (zwaveRequest.getCommandKey().equals(ZwaveCommandKey.NODE_LIST_REPORT)
				|| zwaveRequest.getCommandKey().equals(ZwaveCommandKey.NODE_INFO_CACHED_REPORT)) {
			return getPayload(req, zwaveRequest);
		} else {
			return publish(req, zwaveRequest);
		}
	}

	@Override
	public void subscribe(ZwaveRequest zwaveRequest, String payload) throws JsonParseException, JsonMappingException, IOException {
		MqttPayload mqttPayload = objectMapper.readValue(payload, MqttPayload.class);
		HashMap<String, Object> resultData = mqttPayload.getResultData();
		updateCertification(zwaveRequest, objectMapper.writeValueAsString(resultData));
	}

}
