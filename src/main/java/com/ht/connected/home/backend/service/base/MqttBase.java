package com.ht.connected.home.backend.service.base;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface MqttBase<T, P>{

    T execute(T req, T zwaveRequest, P isCert);

    void subscribe(T zwaveRequest, P payload) throws JsonParseException, JsonMappingException, IOException;
    
    T publish(T req, T zwaveRequest);
    
}
