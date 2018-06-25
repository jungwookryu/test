package com.ht.connected.home.backend.category.ir;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ht.connected.home.backend.service.base.CrudService;
import com.ht.connected.home.backend.service.base.MqttBase;

public interface IRService extends MqttBase<Object, Object>, CrudService<IR, Integer>{

    List<IR> getIRByUser(String userEmail, String serial);
    void delete(List<Integer> no);
    void delete(int no);
    void studyIR(IR ir) throws JsonProcessingException;
    void subscribe(String[] topicSplited, String payload) throws JsonParseException, JsonMappingException, IOException, JSONException;
    void controlIR(IR ir) throws JsonProcessingException, ParseException;
    void deleteIrs(int gatewayNo, String userEmail);
}
