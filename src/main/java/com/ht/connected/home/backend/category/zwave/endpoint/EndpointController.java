package com.ht.connected.home.backend.category.zwave.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ht.connected.home.backend.category.zwave.ZWave;
import com.ht.connected.home.backend.controller.rest.CommonController;

@RestController
@RequestMapping("/endpoint")
public class EndpointController extends CommonController {

    @Autowired
    EndpointService endpointService;

    @PutMapping("/{endpoint_no}")
    public ResponseEntity modifyEndpointInfo(@PathVariable int endpoint_no, @RequestBody Endpoint endpoint) throws JsonProcessingException {
        ZWave rtnZwave = endpointService.modify(endpoint_no, endpoint);
        if(rtnZwave.getEndpoint()!=null) {
            return new ResponseEntity<>(rtnZwave,HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    
}
