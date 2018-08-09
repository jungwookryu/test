package com.ht.connected.home.backend.device.category.zwave;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ht.connected.home.backend.device.category.gateway.Gateway;
import com.ht.connected.home.backend.service.mqtt.MqttRequest;

/**
 * zwave 서비스 인터페이스
 * @author ijlee
 */

public interface ZWaveCommonService {

    ZWaveReport getZWaveList(int gatewayNo);

    Map getZWaveListApp(int gatewayNo);

    int deleteByNo(int no) throws JsonProcessingException, InterruptedException;

    void zwaveBasicControl(ZWaveControl zWaveControl) throws JsonProcessingException, InterruptedException;

    ZWave saveZWaveList(ZWaveRequest zwaveRequest, ZWave nodeItem, Gateway gateway);

    void saveGatewayCategory(ZWaveRequest zwaveRequest, int nodeId);

    List deleteZwave(int gatewayNo, int nodeId);

    void reportZWaveList(ZWaveRequest zwaveRequest, String data) throws JsonParseException, JsonMappingException, IOException, JSONException, InterruptedException;

    void zwaveBasicControl(MqttRequest mqttRequest) throws JsonProcessingException, InterruptedException;

	void reportStatus(ZWaveRequest zwaveRequest, String data)
			throws JsonParseException, JsonMappingException, IOException, JSONException, InterruptedException;
}
