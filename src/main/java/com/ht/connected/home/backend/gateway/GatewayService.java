package com.ht.connected.home.backend.gateway;

import java.util.List;

import com.ht.connected.home.backend.service.base.MqttBase;

public interface GatewayService extends MqttBase<Object, Object>{
    List getGatewayList(String authUserEmail);
    void delete(int no);
}
