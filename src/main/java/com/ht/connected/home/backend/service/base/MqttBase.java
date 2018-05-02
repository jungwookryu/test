package com.ht.connected.home.backend.service.base;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface MqttBase<T, P>{

    void subscribe(T zwaveRequest, P payload) throws JsonParseException, JsonMappingException, IOException, Exception;
    
    void publish(T req, T zwaveRequest);


}
