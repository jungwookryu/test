package com.ht.connected.home.backend.sip.message.service;

import static java.util.Objects.isNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.sip.message.model.dto.SipMqttRequestMessageDto;
import com.ht.connected.home.backend.sip.message.model.entity.SipDevice;
import com.ht.connected.home.backend.sip.message.repository.SipDeviceRepository;

@Service
public class SipDeviceService {

    @Autowired
    private SipDeviceRepository deviceRepository;

    public void addDevice(SipMqttRequestMessageDto request) {
        SipDevice device = deviceRepository.findBySerialNumber(request.getBody().get("deviceNo").toString());
        if (isNull(device)) {
            device = new SipDevice();
            device.setOwnerAccount(request.getBody().get("userID").toString());
            device.setSerialNumber(request.getBody().get("deviceNo").toString());
            device.setDeviceNickname(request.getBody().get("deviceAlias").toString());
            device.setUserPassword(request.getBody().get("devicePassword").toString());
            device.setLocLatitude(request.getBody().get("latitude").toString());
            device.setLocLongitude(request.getBody().get("longitude").toString());
            device.setOwnership(request.getBody().get("ownership").toString());
            device.setDeviceStatus(request.getBody().get("registerStatus").toString());
            device.setSipRole(request.getBody().get("userID").toString().replace("@", "^"));
            device.setDeviceType("sdb");
            deviceRepository.save(device);
        }
    }
    

}
