package com.ht.connected.home.backend.service.impl.zwave.handler;

import static java.util.Objects.isNull;

import java.util.HashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ht.connected.home.backend.model.dto.MqttPayload;
import com.ht.connected.home.backend.model.dto.ZwaveNodeAdd;
import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.service.ZwaveService;
import com.ht.connected.home.backend.service.impl.zwave.ZwaveCommandKey;
import com.ht.connected.home.backend.service.impl.zwave.ZwaveDefault;

@Service
@Scope(value = "prototype")
public class NetworkManagementInclution extends ZwaveDefault implements ZwaveService {

	@Override
	public Object execute(HashMap<String, Object> req, ZwaveRequest zwaveRequest, boolean isCert) throws JsonProcessingException {
		this.isCert = isCert;
		if (zwaveRequest.getCommandKey().equals(ZwaveCommandKey.NODE_ADD_STATUS)
				|| zwaveRequest.getCommandKey().equals(ZwaveCommandKey.NODE_REMOVE_STATUS)
				|| zwaveRequest.getCommandKey().equals(ZwaveCommandKey.FAILED_NODE_REMOVE_STATUS)
				|| zwaveRequest.getCommandKey().equals(ZwaveCommandKey.FAILED_NODE_REPLACE_STATUS)
				|| zwaveRequest.getCommandKey().equals(ZwaveCommandKey.NODE_NEIGHBOR_UPDATE_STATUS)
				|| zwaveRequest.getCommandKey().equals(ZwaveCommandKey.RETURN_ROUTE_ASSIGN_COMPLETE)
				|| zwaveRequest.getCommandKey().equals(ZwaveCommandKey.RETURN_ROUTE_DELETE_COMPLETE)) {
			return getPayload(req, zwaveRequest);
		} else {
			return publish(req, zwaveRequest);
		}
	}

	@Override
	public void subscribe(ZwaveRequest zwaveRequest, String payload) {
		try {
			MqttPayload mqttPayload = objectMapper.readValue(payload, MqttPayload.class);
			if(zwaveRequest.getCommandKey().equals(ZwaveCommandKey.NODE_ADD_STATUS)) {
				/**
				 * newNodeId 가 있을경우 등록 성공이고 없을경우 등록완료 전으로 상태 메세지를 확인한다.
				 */
				if(!isNull(mqttPayload.getResultData().get("newNodeId"))) {
					zwaveRequest.setClassKey("0x52");
					zwaveRequest.setCommandKey("0x01");
					zwaveRequest.setNodeId(0);
					zwaveRequest.setEndpointId(0);
					zwaveRequest.setVersion("v1");
					String topic = getMqttPublishTopic(zwaveRequest);
					publish(topic);
				}else {
					String status = mqttPayload.getResultData().get("status").toString();
					logging.info(String.format("<< SERIAL NO : %s, NODE_ADD_STATUS : %s >>", zwaveRequest.getSerialNo(), status));
				}				
			}
			updateCertification(zwaveRequest, payload);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	@Override
	public ResponseEntity publish(HashMap<String, Object> req, ZwaveRequest zwaveRequest) throws JsonProcessingException {
		String topic = getMqttPublishTopic(zwaveRequest);
		if (!isCert) {
			if (zwaveRequest.getCommandKey().equals(ZwaveCommandKey.NODE_ADD)) {				
				HashMap<String, Object> payload = new HashMap<>();
				ZwaveNodeAdd zwaveNodeAdd = new ZwaveNodeAdd();
				zwaveNodeAdd.setMode(req.get("mode").toString());
				payload.put("set_data", zwaveNodeAdd);
				publish(topic, payload);
				logging.info("====================== ZWAVE PROTO MQTT NODE_ADD PUBLISH TOPIC ======================");
				logging.info(topic);
			}
		} else {
			publish(topic, getPublishPayload(req));
		}
		return new ResponseEntity(HttpStatus.OK);
	}

}
