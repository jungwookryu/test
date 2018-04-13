package com.ht.connected.home.backend.service.base;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;

import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.service.ZwaveService;


public class ZwaveBase<T> implements ZwaveService {

    private ZwaveService handler;

    public ZwaveBase(T handler) {
        this.handler = (ZwaveService) handler;
    }

    @Override
    public ResponseEntity execute(HashMap<String, Object> req, ZwaveRequest zwaveRequest, boolean isCert) {
        return handler.execute(req, zwaveRequest, isCert);
    }

    @Override
    public void subscribe(ZwaveRequest zwaveRequest, String payload) {      
        handler.subscribe(zwaveRequest, payload);
    }

    @Override
    public ResponseEntity publish(HashMap<String, Object> req, ZwaveRequest zwaveRequest) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
