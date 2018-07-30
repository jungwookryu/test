package com.ht.connected.home.backend.sip.media.service;

import static java.util.Objects.isNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.sip.media.model.dto.SipMediaMqttRequestMessageDto;
import com.ht.connected.home.backend.sip.media.model.entity.SipMediaEvent;
import com.ht.connected.home.backend.sip.media.repository.SipMediaEventRepository;
import com.ht.connected.home.backend.sip.message.model.entity.SipDevice;
import com.ht.connected.home.backend.sip.message.repository.SipDeviceRepository;

/**
 * 이벤트 보고 디비 지정 서비스
 * 
 * @author 구정화
 *
 */
@Service
public class SipMediaEventService {

    @Autowired
    private SipMediaEventRepository eventRepository;

    @Autowired
    private SipDeviceRepository sipDeviceRepository;

    /**
     * 이벤트 보고 정보 디비 저장
     * 
     * @param request
     * @return
     */
    public boolean addEvent(SipMediaMqttRequestMessageDto request) {
        String fileVideo;
        SipMediaEvent event = new SipMediaEvent();
        event.setSerialNumber(request.getSerialNumber());
        event.setDateTime(request.getTimestamp());
        event.setEventType("SendEvent");
        event.setEventId(request.getEventId());

        SipDevice sipDevice = sipDeviceRepository.findBySerialNumber(request.getSerialNumber());
        if (!isNull(sipDevice.getOwnerNickname())) {
            event.setOwnerName(sipDevice.getOwnerNickname());
        } else {
            event.setOwnerName(sipDevice.getOwnerAccount());
        }

        if (!isNull(sipDevice.getLocation())) {
            event.setLocationId(sipDevice.getLocation());
        }

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
