package com.ht.connected.home.backend.sip.message.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.sip.message.model.entity.SipDevice;
import com.ht.connected.home.backend.sip.message.model.entity.SipEvent;
import com.ht.connected.home.backend.sip.message.model.entity.SipShare;
import com.ht.connected.home.backend.sip.message.repository.SipDeviceRepository;
import com.ht.connected.home.backend.sip.message.repository.SipEventRepository;
import com.ht.connected.home.backend.sip.message.repository.SipShareRepository;

@Service
public class SipEventService {

    @Autowired
    private SipEventRepository eventRepository;

    @Autowired
    private SipDeviceRepository deviceRepository;

    @Autowired
    private SipShareRepository shareRepository;

    public List<SipEvent> getEvents(String userId) {

        List<SipDevice> ownerDevices = deviceRepository.findByOwnerAccount(userId);
        ArrayList<String> ownerDeviceSerialNumber = new ArrayList<>();
        ownerDeviceSerialNumber.add("0");
        ownerDevices.stream().forEach(ownerDevice -> {
            ownerDeviceSerialNumber.add(ownerDevice.getSerialNumber());
        });
        List<SipEvent> ownerDeviceEvents = eventRepository
                .getOwnerDeviceEvents(ownerDeviceSerialNumber);

        List<SipShare> sharedDevices = shareRepository.findBySharedAccount(userId);
        ArrayList<String> sharedDeviceSerialNumber = new ArrayList<>();
        sharedDeviceSerialNumber.add("0");
        sharedDevices.stream().forEach(shareDevice -> {
            sharedDeviceSerialNumber.add(shareDevice.getSerialNumber());
        });
        List<SipEvent> sharedDeviceEvents = eventRepository
                .getSharedDeviceEvents(sharedDeviceSerialNumber);
        ownerDeviceEvents.addAll(sharedDeviceEvents);
        return ownerDeviceEvents;

    }

}
