package com.ht.connected.home.backend.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.http.ResponseEntity;

import com.ht.connected.home.backend.model.dto.MqttMessageArrived;
import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.model.entity.Gateway;
import com.ht.connected.home.backend.service.base.GatewayBase;

public interface GateWayService extends GatewayBase<Gateway, Integer> {
    List getGatewayList(String authUserEmail);
    ResponseEntity execute(HashMap<String, Object> req, ZwaveRequest zwaveRequest, boolean isCert);
    void subscribe(ZwaveRequest zwaveRequest, String payload) throws JsonParseException, JsonMappingException, IOException;
    ResponseEntity publish(HashMap<String, Object> req, ZwaveRequest zwaveRequest);
    Object execute(MqttMessageArrived mqttMessageArrived) throws Exception;
}
