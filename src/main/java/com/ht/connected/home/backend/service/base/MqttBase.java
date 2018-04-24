package com.ht.connected.home.backend.service.base;

public interface MqttBase<T, P>{

    T execute(T req, T zwaveRequest, P isCert);

    void subscribe(T zwaveRequest, P payload);
    
    T publish(T req, T zwaveRequest);
    
}
