package com.ht.connected.home.backend.service.base;

import com.ht.connected.home.backend.model.entity.Zwave;

public interface ZwaveBase extends CrudService<Zwave, Integer>, MqttBase<Object, Object> {

}
