package com.ht.connected.home.backend.service;

import java.util.List;

import com.ht.connected.home.backend.service.base.MqttBase;

public interface GateWayService extends MqttBase<Object, Object>{
    List getGatewayList(String authUserEmail);
    void delete(int no);
}
