package com.ht.connected.home.backend.device.category.zwave.certi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ht.connected.home.backend.device.category.gateway.Gateway;
import com.ht.connected.home.backend.device.category.zwave.ZWaveRequest;

/**
 * zwave certi 서비스 인터페이스
 * @author ijlee
 */
public interface ZWaveCertiNetworkManagementBasicService {

    void subscribe(ZWaveRequest zwaveRequest, String payload) throws Exception;

	void setLearnMode(Gateway gateway, int mode) throws JsonProcessingException, InterruptedException;

}
