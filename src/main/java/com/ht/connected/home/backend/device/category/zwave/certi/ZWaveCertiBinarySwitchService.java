package com.ht.connected.home.backend.device.category.zwave.certi;

import com.ht.connected.home.backend.device.category.zwave.ZWaveRequest;

/**
 * zwave certi 서비스 인터페이스
 * @author ijlee
 */
public interface ZWaveCertiBinarySwitchService {

    void subscribe(ZWaveRequest zwaveRequest, String payload) throws Exception;

}
