package com.ht.connected.home.backend.category.zwave.endpoint;

import java.util.List;

import com.ht.connected.home.backend.category.zwave.ZWave;
import com.ht.connected.home.backend.service.base.CrudService;

/**
 * zwave 서비스 인터페이스
 * 
 * @author 구정화
 *
 */
public interface EndpointService extends CrudService<Endpoint, Integer>{

    ZWave modify(int no, Endpoint endpoint);

    void deleteEndpoint(ZWave zwave);

    List<EndpointReportByApp> getEndpoint(ZWave zwave);
    
}
