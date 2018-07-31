package com.ht.connected.home.backend.category.zwave;

import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * zwave 서비스 인터페이스
 * @author ijlee
 */

public interface ZWaveService {

    void subscribe(ZWaveRequest zwaveRequest, String payload) throws Exception;

    void publish(HashMap<String, Object> req, ZWaveRequest zwaveRequest) throws JsonProcessingException, InterruptedException;

}
