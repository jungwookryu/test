package com.ht.connected.home.backend.service;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;

import com.ht.connected.home.backend.model.dto.ZwaveRequest;

/**
 * zwave 서비스 인터페이스
 * 
 * @author 구정화
 *
 */
public interface ZwaveService {

    public ResponseEntity execute(HashMap<String, Object> req, ZwaveRequest zwaveRequest, boolean isCert);

    public void subscribe(ZwaveRequest zwaveRequest, String payload);
    
    public ResponseEntity publish(HashMap<String, Object> req, ZwaveRequest zwaveRequest);

}
