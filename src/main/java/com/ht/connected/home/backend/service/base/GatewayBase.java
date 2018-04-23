package com.ht.connected.home.backend.service.base;

public interface GatewayBase<T, P> extends CrudService<T, P>, MqttBase<T, P> {

}
