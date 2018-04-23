package com.ht.connected.home.backend.service;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.model.entity.Zwave;
import com.ht.connected.home.backend.service.base.ZwaveBase;

/**
 * zwave 서비스 인터페이스
 * 
 * @author 구정화
 *
 */
public interface ZwaveService extends ZwaveBase<Zwave, Integer>{
    
    ResponseEntity execute(HashMap<String, Object> req, ZwaveRequest zwaveRequest, boolean isCert) throws JsonProcessingException;

    void subscribe(ZwaveRequest zwaveRequest, String payload);
    
    ResponseEntity publish(HashMap<String, Object> req, ZwaveRequest zwaveRequest) throws JsonProcessingException;
    
}
