package com.ht.connected.home.backend.category.zwave;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

/**
 * zwave 서비스 인터페이스
 * 
 * @author 구정화
 *
 */
public interface ZWaveStatusService {

    void subscribe(ZWaveRequest zwaveRequest, String payload) throws JsonGenerationException, JsonMappingException, IOException, InterruptedException;
    
   
}
