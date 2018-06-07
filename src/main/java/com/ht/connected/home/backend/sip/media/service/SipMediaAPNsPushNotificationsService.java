package com.ht.connected.home.backend.sip.media.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.service.push.ApnsPushClient;
import com.ht.connected.home.backend.sip.media.model.dto.SipMediaMqttRequestMessageDto;

import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
 
@Service
public class SipMediaAPNsPushNotificationsService {
    
    @Autowired
    private ApnsPushClient apnsPushClient;
    
    
    public void send(List<String> tokens, PushNotificationPayload payload) {
        List<PushedNotification> notifications = apnsPushClient.send(tokens, payload);
    }
 	
	public PushNotificationPayload getPushMessage(String eventType, SipMediaMqttRequestMessageDto request) {
	    String fileVideo;
	    PushNotificationPayload payload = new PushNotificationPayload();
	    try {	                    
            payload.addAlert(request.getEvent());
            payload.addCustomDictionary("TYPE", eventType);
            payload.addCustomDictionary("NAME", request.getEvent());
            payload.addCustomDictionary("SNO", request.getSerialNumber());
            payload.addCustomDictionary("ID", request.getEventId());
            payload.addCustomDictionary("TIME", request.getTimestamp());
            if (request.getFileInfo().isVideoExist()) {
                fileVideo = "exist";
            } else {
                fileVideo = "notsupport";
            }
            payload.addCustomDictionary("VIDEO", fileVideo);  
            payload.addBadge(0);
            payload.addSound("default");
        } catch (Exception e) {
            System.out.println("sendReleaseApns Err: " + e);
        } 
	    return payload; 
	}
}