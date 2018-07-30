package com.ht.connected.home.backend.category.zwave;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ht.connected.home.backend.gateway.Gateway;

/**
 * zwave 서비스 인터페이스
 * @author ijlee
 */

public interface ZWaveService {

    void subscribe(ZWaveRequest zwaveRequest, String payload) throws Exception;

    void publish(HashMap<String, Object> req, ZWaveRequest zwaveRequest) throws JsonProcessingException, InterruptedException;

    int getByUserEmailAndNo(String userEmail, int no);

    ZWaveReport getZWaveList(int gatewayNo);

    Map getZWaveListApp(int gatewayNo);

    int deleteByNo(int no) throws JsonProcessingException, InterruptedException;

    void zwaveBasicControl(ZWaveControl zWaveControl) throws JsonProcessingException, InterruptedException;

    ZWave saveZWaveList(ZWaveRequest zwaveRequest, ZWave nodeItem, Gateway gateway);

    void saveGatewayCategory(ZWaveRequest zwaveRequest, int nodeId);

    List deleteZwave(int gatewayNo, int nodeId);

    void reportZWaveList(ZWaveRequest zwaveRequest, String data) throws JsonParseException, JsonMappingException, IOException, JSONException, InterruptedException;
}
