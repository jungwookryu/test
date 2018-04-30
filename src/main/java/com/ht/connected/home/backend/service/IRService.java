package com.ht.connected.home.backend.service;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ht.connected.home.backend.model.entity.IR;
import com.ht.connected.home.backend.service.base.CrudService;

public interface IRService extends CrudService<IR, Integer> {

    List<IR> getIRByUser(String userEmail);
    void delete(int no);
    void studyIR(IR ir) throws JsonProcessingException;
    void subscribe(String[] topicSplited, String payload) throws JsonParseException, JsonMappingException, IOException, JSONException;
    void controlIR(IR ir) throws JsonProcessingException;

}
