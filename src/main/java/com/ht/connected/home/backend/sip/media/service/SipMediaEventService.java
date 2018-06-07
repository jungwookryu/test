package com.ht.connected.home.backend.sip.media.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.sip.media.model.dto.SipMediaMqttRequestMessageDto;
import com.ht.connected.home.backend.sip.media.model.entity.SipMediaEvent;
import com.ht.connected.home.backend.sip.media.repository.SipMediaEventRepository;

@Service
public class SipMediaEventService {

    @Autowired
    private SipMediaEventRepository eventRepository;
       
    
    public boolean addEvent(SipMediaMqttRequestMessageDto request) {
        String fileVideo;
        SipMediaEvent event = new SipMediaEvent();
        event.setSerialNumber(request.getSerialNumber());
        event.setDateTime(request.getTimestamp());
        event.setEventType("SendEvent");
        event.setEventId(request.getEventId());
        if (request.getFileInfo().isVideoExist()) {
            fileVideo = "exist";
        } else {
            fileVideo = "notsupport";
        }
        event.setVideoExist(fileVideo);
        event.setFileExtension(request.getFileInfo().getFileExt());
        SipMediaEvent ev = eventRepository.save(event);
        return ev.getSerialNumber().equals(request.getSerialNumber());
    }
}
