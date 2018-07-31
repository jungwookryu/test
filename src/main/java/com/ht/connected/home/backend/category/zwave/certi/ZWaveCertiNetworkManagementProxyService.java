package com.ht.connected.home.backend.category.zwave.certi;

import com.ht.connected.home.backend.category.zwave.ZWaveRequest;

/**
 * zwave certi 서비스 인터페이스
 * @author ijlee
 */
public interface ZWaveCertiNetworkManagementProxyService {

    void subscribe(ZWaveRequest zwaveRequest, String payload) throws Exception;

}
