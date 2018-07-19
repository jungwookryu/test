package com.ht.connected.home.backend.category.zwave.notification;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.ht.connected.home.backend.category.zwave.ZWave;
import com.ht.connected.home.backend.category.zwave.ZWaveRequest;

/**
 * zwave 서비스 인터페이스
 * 
 * @author 구정화
 *
 */
public interface NotificationService {

    void subscribe(ZWaveRequest zwaveRequest, String payload) throws JsonGenerationException, JsonMappingException, IOException, InterruptedException;
    void delete(ZWave zwave);
   
}
