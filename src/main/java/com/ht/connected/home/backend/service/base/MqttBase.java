package com.ht.connected.home.backend.service.base;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface MqttBase<T, P>{

    void subscribe(T t, P p) throws JsonParseException, JsonMappingException, IOException, Exception;
    
    void publish(T t, T t2);
    
}
