package com.ht.connected.home.backend.gateway;

import java.util.List;

import com.ht.connected.home.backend.gatewayCategory.GatewayCategory;
import com.ht.connected.home.backend.service.base.MqttBase;

public interface GatewayService extends MqttBase<Object, Object>{
    List getGatewayList(String status,String authUserEmail);
    void delete(int no);
    void deleteCategory(GatewayCategory gatewayCategory);
    Gateway modifyGateway(Gateway originGateway, Gateway gateway);
    Gateway findOne(int no);
}
