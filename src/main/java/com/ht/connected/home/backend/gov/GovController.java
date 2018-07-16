package com.ht.connected.home.backend.gov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/gov")
public class GovController {
    
    Logger logger = LoggerFactory.getLogger(GovController.class);
    
    @Autowired
    GovService govService;
    
    @PutMapping("/{device_type}")
    public ResponseEntity controlGovDevice(@PathVariable("device_type") String deviceType, @RequestBody GovDeviceRequest govDeviceRequest) throws JsonProcessingException, InterruptedException {
        govDeviceRequest.setDevice(deviceType);
        govService.publish(govDeviceRequest);
        return new ResponseEntity(HttpStatus.OK);
    }
}
