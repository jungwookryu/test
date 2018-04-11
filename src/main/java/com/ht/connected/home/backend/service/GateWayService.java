package com.ht.connected.home.backend.service;

import java.util.List;

import com.ht.connected.home.backend.model.entity.Gateway;
import com.ht.connected.home.backend.service.base.CrudService;

public interface GateWayService extends CrudService<Gateway, Integer> {
    List getGatewayList(String authUserEmail);
}
