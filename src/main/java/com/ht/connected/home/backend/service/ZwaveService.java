package com.ht.connected.home.backend.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.model.entity.User;
import com.ht.connected.home.backend.model.entity.Zwave;
import com.ht.connected.home.backend.service.base.CrudService;
import com.ht.connected.home.backend.service.base.MqttBase;

/**
 * zwave 서비스 인터페이스
 * 
 * @author 구정화
 *
 */
public interface ZwaveService extends CrudService<Zwave, Integer>, MqttBase<Object, Object>{
    
    ResponseEntity execute(HashMap<String, Object> req, ZwaveRequest zwaveRequest, boolean isCert) throws JsonProcessingException;

    void subscribe(ZwaveRequest zwaveRequest, String payload) throws JsonParseException, JsonMappingException, IOException, Exception;
    
    ResponseEntity publish(HashMap<String, Object> req, ZwaveRequest zwaveRequest) throws JsonProcessingException;

    void execute(Map map, boolean isCert) throws JsonProcessingException;
    
    public int deleteByNo(int no) throws JsonProcessingException ;
    
    public int getByUserEmailAndNo(String userEmail, int no);
}
