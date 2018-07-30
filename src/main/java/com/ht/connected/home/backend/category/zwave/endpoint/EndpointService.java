package com.ht.connected.home.backend.category.zwave.endpoint;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ht.connected.home.backend.category.zwave.ZWave;
import com.ht.connected.home.backend.category.zwave.ZWaveControl;

/**
 * zwave 서비스 인터페이스
 * 
 * @author 구정화
 *
 */
public interface EndpointService{

    ZWave modify(int no, Endpoint endpoint);

    void deleteEndpoint(ZWave zwave);

    List<EndpointReportByApp> getEndpoint(ZWave zwave);

    void zwaveControl(ZWaveControl zWaveControl) throws JsonProcessingException, InterruptedException, JsonGenerationException, JsonMappingException, IOException;

    Endpoint saveEndpoint(Endpoint endpoint);
    
}
