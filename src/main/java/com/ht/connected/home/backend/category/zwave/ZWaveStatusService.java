package com.ht.connected.home.backend.category.zwave;

/**
 * zwave 서비스 인터페이스
 * 
 * @author 구정화
 *
 */
public interface ZWaveStatusService {

    void subscribe(ZWaveRequest zwaveRequest, String payload);
    
   
}
