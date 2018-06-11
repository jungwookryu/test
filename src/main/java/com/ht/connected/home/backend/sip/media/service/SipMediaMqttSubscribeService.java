package com.ht.connected.home.backend.sip.media.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.sip.media.model.dto.SipMediaMqttRequestMessageDto;

import javapns.notification.PushNotificationPayload;

@Service
public class SipMediaMqttSubscribeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SipMediaMqttSubscribeService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SipMediaUploadService uploadService;

    @Autowired
    private SipMediaUserService userService;

    @Autowired
    private SipMediaAndroidPushNotificationsService fcmPushService;
    
    @Autowired
    private SipMediaAPNsPushNotificationsService apnsPushService;
    
    @Autowired
    private SipMediaEventService eventService;
    

    /**
     * 이벤트 보고 정보 디비 저정
     * 이미지, 비디오 파일 업로드
     * 업로드 성공시 FCM, APNs 푸시 메세지 발송 요청
     * 
     * @param request
     * @throws IOException
     */
    public void upload(SipMediaMqttRequestMessageDto request) throws IOException {
        Boolean fileSaved = true;
        try {
            eventService.addEvent(request);
            fileSaved = uploadService.saveFile(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fileSaved) {
            HashMap<String, String> message = null;
            List<String> androidTokens = userService.getPushTokens(request.getSerialNumber(), "Android", "release");
            if(androidTokens.size() > 0) {
                message = fcmPushService.getPushMessage("SendEvent", request);
                HashMap<String, Object> notification = fcmPushService.getPushNotification(androidTokens, message);
                fcmPushService.send(notification, "release");
            }
            
            List<String> iphoneTokens = userService.getPushTokens(request.getSerialNumber(), "IPhone", "release");
            if(iphoneTokens.size() > 0) {
                PushNotificationPayload payload = apnsPushService.getPushMessage("SendEvent", request);
                apnsPushService.send(iphoneTokens, payload);    
            }            
        }
    }

    /**
     * MQTT subscribe 메세지 처리
     * 
     * @param message
     */
    public void subscribe(Message<?> message) {
        String topic = String.valueOf(message.getHeaders().get("mqtt_topic"));
        String payload = String.valueOf(message.getPayload());
        LOGGER.info("messageArrived: Topic=" + topic + ", Payload=" + payload);
        SipMediaMqttRequestMessageDto request;
        try {
            request = objectMapper.readValue(payload, SipMediaMqttRequestMessageDto.class);
            request.setTopic(topic);
            request.setTimestamp(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
            upload(request);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
