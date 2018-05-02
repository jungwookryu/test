package com.ht.connected.home.backend.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.http.ResponseEntity;

import com.ht.connected.home.backend.model.dto.MqttMessageArrived;
import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.service.base.MqttBase;

public interface GateWayService extends MqttBase<Object, Object>{
    List getGatewayList(String authUserEmail);
    void subscribe(ZwaveRequest zwaveRequest, String payload) throws JsonParseException, JsonMappingException, IOException;
    ResponseEntity publish(HashMap<String, Object> req, ZwaveRequest zwaveRequest);
    Object execute(MqttMessageArrived mqttMessageArrived) throws Exception;
}
