package com.ht.connected.home.backend.service.impl.zwave.handler;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.repository.ZwaveRepository;
import com.ht.connected.home.backend.service.ZwaveService;
import com.ht.connected.home.backend.config.service.ZwaveCommandKey;
import com.ht.connected.home.backend.service.impl.zwave.ZwaveDefault;

@Service
@Scope(value = "prototype")
public class Basic extends ZwaveDefault implements ZwaveService {

    @Autowired
    ZwaveRepository zwaveRepository;

    @Override
    public ResponseEntity execute(HashMap<String, Object> req, ZwaveRequest zwaveRequest, boolean isCert) {
        this.isCert = isCert;
        if (zwaveRequest.getCommandKey()==ZwaveCommandKey.BASIC_REPORT) {
            return getPayload(req, zwaveRequest);
        } else {
            return publish(req, zwaveRequest);
        }
    }

    @Override
    public void subscribe(ZwaveRequest zwaveRequest, String payload) {
        if (zwaveRequest.getCommandKey()==ZwaveCommandKey.BASIC_REPORT) {
            updateCertification(zwaveRequest, payload);
        }
    }

    @Override
    public ResponseEntity publish(HashMap<String, Object> req, ZwaveRequest zwaveRequest) {
        ResponseEntity response = new ResponseEntity(HttpStatus.BAD_REQUEST); ;
        String topic = getMqttPublishTopic(zwaveRequest, "host");
        if(topic.length() > 0) {
            publish(topic, getPublishPayload(req));
            response =  new ResponseEntity(HttpStatus.OK);    
        }
        return response;        
    }

}
