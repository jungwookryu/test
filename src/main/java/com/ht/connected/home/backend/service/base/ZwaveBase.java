package com.ht.connected.home.backend.service.base;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ht.connected.home.backend.model.entity.Zwave;

public interface ZwaveBase extends CrudService<Zwave, Integer>, MqttBase<Object, Object> {

    void execute(Map map, boolean isCert) throws JsonProcessingException;

}
