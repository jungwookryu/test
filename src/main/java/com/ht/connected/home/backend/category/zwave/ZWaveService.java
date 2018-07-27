package com.ht.connected.home.backend.category.zwave;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ht.connected.home.backend.gateway.Gateway;
import com.ht.connected.home.backend.service.base.CrudService;

/**
 * zwave 서비스 인터페이스
 * 
 * @author 구정화
 *
 */
public interface ZWaveService {
    
    void subscribe(ZWaveRequest zwaveRequest, String payload) throws JsonParseException, JsonMappingException, IOException, Exception;
    
    ResponseEntity publish(HashMap<String, Object> req, ZWaveRequest zwaveRequest) throws JsonProcessingException, InterruptedException;

    void execute(Map map, boolean isCert) throws JsonProcessingException, InterruptedException;
    
    public int getByUserEmailAndNo(String userEmail, int no);

    void subscribeInit(Gateway gateway) throws JsonProcessingException, InterruptedException;

    ZWaveReport getZWaveList(int gatewayNo);

    Map getZWaveListApp(int gatewayNo);

    int deleteByNo(int no) throws JsonProcessingException, InterruptedException;

    void zwaveBasicControl(ZWaveControl zWaveControl) throws JsonProcessingException, InterruptedException;
}
