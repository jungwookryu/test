package com.ht.connected.home.backend.sip.media.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.service.push.FirebasePushClient;
import com.ht.connected.home.backend.sip.media.model.dto.SipMediaMqttRequestMessageDto;


/**
 * FCM 푸시 발송 요청 서비스
 * 
 * @author 구정화
 *
 */
@Service
public class SipMediaAndroidPushNotificationsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SipMediaAndroidPushNotificationsService.class);

    /**
     * 디버그용 키, 서비스용 키
     */
    private static String SERVER_KEY_DEBUG = "AIzaSyCM4eZFA1MXIHHQtGrJTkQnMn4lDvtVIgs";    
    private static String SERVER_KEY_RELEASE = "AIzaSyCM4eZFA1MXIHHQtGrJTkQnMn4lDvtVIgs";

    @Autowired
    private FirebasePushClient fireBasePushClient;

    /**
     * FCM notification 발송 요청
     * 
     * @param notification
     * @param devType
     */
    public void send(HashMap<String, Object> notification, String devType) {
        String serverKey = null;
        if (devType.equalsIgnoreCase("release")) {
            serverKey = SERVER_KEY_RELEASE;
        } else {
            serverKey = SERVER_KEY_DEBUG;
        }
        HttpEntity<Object> entity = new HttpEntity<Object>(notification);
        fireBasePushClient.send(entity, serverKey);
    }

    /**
     * 메세지 데이터 생성
     * 
     * @param strEventType
     * @param strEventName
     * @param strDevSerial
     * @param strEventID
     * @param strTimeStamp
     * @param trueOrFalse
     * @return
     */
    public HashMap<String, String> getPushMessage(String eventType, SipMediaMqttRequestMessageDto request) {
        String fileVideo;
        LOGGER.info("SendEvent Type : " + eventType);
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("TYPE", eventType);
        data.put("NAME", request.getEvent());
        data.put("SNO", request.getSerialNumber());
        data.put("ID", request.getEventId());
        data.put("TIME", request.getTimestamp());
        if (request.getFileInfo().isVideoExist()) {
            fileVideo = "exist";
        } else {
            fileVideo = "notsupport";
        }
        data.put("VIDEO", fileVideo);
        return data;
    }

    /**
     * Notification 내에 메세지를 담기위한 데이터셋을 생성 하나의 기기에 보낼때와 여러 기기에 보낼때를 구분
     * 
     * @param tokens
     * @param message
     * @return
     */
    public HashMap<String, Object> getPushNotification(List<String> tokens, HashMap<String, String> message) {
        HashMap<String, Object> fcmPushNotification = new HashMap<String, Object>();
        if (tokens.size() > 1) {
            fcmPushNotification.put("registration_ids", tokens);
        } else {
            fcmPushNotification.put("to", tokens.get(0));
        }
        fcmPushNotification.put("priority", "high");
        HashMap<String, String> notification = new HashMap<String, String>();
        // fcmPushNotification.put("notification", notification);
        fcmPushNotification.put("data", message);
        return fcmPushNotification;
    }

}