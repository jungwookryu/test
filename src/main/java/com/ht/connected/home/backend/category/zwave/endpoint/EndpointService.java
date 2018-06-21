package com.ht.connected.home.backend.category.zwave.endpoint;

import com.ht.connected.home.backend.category.zwave.ZWave;
import com.ht.connected.home.backend.service.base.CrudService;
import com.ht.connected.home.backend.service.base.MqttBase;

/**
 * zwave 서비스 인터페이스
 * 
 * @author 구정화
 *
 */
public interface EndpointService extends CrudService<Endpoint, Integer>, MqttBase<Object, Object>{

    ZWave modify(int no, Endpoint endpoint);
    
}
