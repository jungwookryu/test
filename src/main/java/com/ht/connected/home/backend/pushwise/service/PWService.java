package com.ht.connected.home.backend.pushwise.service;

import static java.util.Objects.isNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.client.home.sharehome.ShareHome;
import com.ht.connected.home.backend.client.home.sharehome.ShareHomeRepository;
import com.ht.connected.home.backend.client.user.User;
import com.ht.connected.home.backend.client.user.UserRepository;
import com.ht.connected.home.backend.device.category.zwave.notification.Notification;
import com.ht.connected.home.backend.pushwise.model.entity.PWHomeSecurity;
import com.ht.connected.home.backend.pushwise.model.entity.PWPushHistory;
import com.ht.connected.home.backend.pushwise.repository.PWHomeSecurityRepository;
import com.ht.connected.home.backend.pushwise.repository.PWPushHistoryRepository;
import com.ht.connected.home.backend.service.push.FirebasePushClient;

/**
 * 방범관련 비지니스 로직 및 기기 상태 알림 푸시 발송
 * 
 * @author 구정화
 *
 */
@Service
@PropertySource(value = "classpath:security_message.properties", encoding = "UTF-8")
public class PWService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PWService.class);

    private static String FCM_SERVER_KEY = "AAAA3_Lm7M0:APA91bF2k-CMsYYMwBmTsWkqSvtb5dffbDkdaF0WVSXyedhhex3PZtN0BPUljJIDEO1ZREaV8mH1l-5-xBBSI3UFrA9cCi2NZiZ9UJTphMaeKdbYQSbffepNF5XnGwZKGrwVBZrrkGVS";

    @Autowired
    private FirebasePushClient fireBasePushClient;

    @Autowired
    private PWHomeSecurityRepository homeSecurityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShareHomeRepository shareHomeRepository;

    @Autowired
    private PWNotifyCatchService notifyCatchService;

    @Autowired
    private PWPushHistoryRepository pushHistoryRepository;

    @Autowired
    private Environment env;

    private static final String MSG_TYPE_SECURITY_ALERT = "alert";
    private static final String MSG_TYPE_SECURITY_SETTING = "setting";
    private static final String MSG_TYPE_CONTROLLABLE_DEVICE_NOTIFY = "notify";

    /**
     * 이벤트가 방법알림 조건에 맞으면 푸시알림을 보낸다
     * 
     * @param gatewayNo
     */
    public void pushWiseNotification(Notification event) {
        List<User> users = null;
        PWHomeSecurity homeSecurity = homeSecurityRepository.getHomeSecurityGatewaySerial(event.getZwave_no());
        if (isNull(homeSecurity)) {
            homeSecurity = new PWHomeSecurity();
        }
        if (notifyCatchService.isMagneticDetected(event.getEvent_code(), event.getNotification_code(),
                homeSecurity.getSecurityStatus())
                || notifyCatchService.isMotionDetected(event.getEvent_code(), event.getNotification_code(),
                        homeSecurity.getSecurityStatus())) {
            users = getUsersByHomeNo(homeSecurity.getHomeNo());
            sendPushMessage(users, MSG_TYPE_SECURITY_ALERT, homeSecurity, event);
        } else if (notifyCatchService.isControllableDeviceNotify(event.getEvent_name())) {
            users = getUsersByHomeNo(homeSecurity.getHomeNo());
            sendPushMessage(users, MSG_TYPE_CONTROLLABLE_DEVICE_NOTIFY, homeSecurity, event);
        }
    }

    /**
     * 안드로이드, iOS 구분하여 푸시 알림 발송
     * 
     * @param users
     */
    public void sendPushMessage(List<User> users, String type, PWHomeSecurity homeSecurity, Notification event) {
        boolean isHistoryAdded = true;
        users.stream().forEach(user -> {
            if (user.getConnectedType().equals("0")) {
                String msgBody = getLocaleMessage(user.getLocale(), type, homeSecurity, event, user);
                JSONObject message = getFCMPushMessage(user.getPushToken(), msgBody,
                        getLocaleTitle(user.getLocale(), type), homeSecurity.getGatewaySerial(),
                        homeSecurity.getHomeNo());
                if (isHistoryAdded) {
                    addHistory(type, msgBody, homeSecurity);
                }
                LOGGER.info("====== PUSH sending ======");
                LOGGER.info(message.toString());
                fireBasePushClient.send(message.toString(), FCM_SERVER_KEY);
            } else if (user.getConnectedType().equals("1")) {
                // TODO : iOS 기기로 메세지 발송 
            }
        });
    }

    /**
     * 푸시 기록 테이블에 저장
     * 
     * @param type
     * @param message
     * @param homeSecurity
     */
    private void addHistory(String type, String message, PWHomeSecurity homeSecurity) {
        PWPushHistory history = new PWPushHistory();
        history.setCreatedAt(LocalDateTime.now().toString());
        history.setMessageCode(String.valueOf(homeSecurity.getSecurityStatus()));
        history.setMessage(message);
        history.setPushType(type);
        pushHistoryRepository.save(history);
    }

    /**
     * 푸시 메세지 생성
     * 
     * @param token
     * @param message
     * @param title
     * @param serial
     * @param homeNo
     * @return
     */
    private JSONObject getFCMPushMessage(String token, String message, String title, String serial, int homeNo) {
        JSONObject jo = new JSONObject();
        JSONObject joNotify = new JSONObject();
        JSONObject joData = new JSONObject();
        JSONObject joBody = new JSONObject();

        try {
            joBody.put("serial", serial);
            joBody.put("home_no", homeNo);
            joBody.put("timestamp", Instant.now().atZone(TimeZone.getTimeZone("GMT").toZoneId()).toEpochSecond());

            joNotify.put("body", message);
            joNotify.put("title", title);
            joNotify.put("icon", "myicon");
            joNotify.put("sound", "mySound");

            joData.put("title", title);
            joData.put("message", message);
            joData.put("action", "action");
            joData.put("body", joBody);

            jo.put("to", token);
            jo.put("priority", "high");
            jo.put("notification", joNotify);
            jo.put("data", joData);
        } catch (JSONException e) {
            LOGGER.warn("Get push message failed in getFCMPushMessage", e);
        }
        return jo;
    }

    /**
     * 사용자 언어에 따른 푸시 메세지 반환
     * 
     * @param locale
     * @param type
     * @param homeSecurity
     * @param event
     * @param user
     * @return
     */
    private String getLocaleMessage(String locale, String type, PWHomeSecurity homeSecurity, Notification event,
            User user) {
        locale = locale.split("_")[0];
        String message = env.getProperty(String.format("secuirty.pushmsg.%s.%s", locale, type));
        if (type.equals(MSG_TYPE_SECURITY_ALERT)) {
            // {event} was detected at {home} {device} sensor.
            message = String.format(message, encodeUTF8(event.getEvent_name()),
                    encodeUTF8(homeSecurity.getHomeNickname()), encodeUTF8(homeSecurity.getZwaveNickname()));
        } else if (type.equals(MSG_TYPE_CONTROLLABLE_DEVICE_NOTIFY)) {
            // {user} has controlled the {home} {deviceNickname} device to {status}.
            message = String.format(message, encodeUTF8(user.getNickName()), encodeUTF8(homeSecurity.getHomeNickname()),
                    encodeUTF8(homeSecurity.getZwaveNickname()), encodeUTF8(event.getEvent_name()));
        } else if (type.equals(MSG_TYPE_SECURITY_SETTING)) {
            // {user} has changed the {home} security status to {status}.
            String securityStatusName = env
                    .getProperty(String.format("secuirty.status.%s%s", locale, homeSecurity.getSecurityStatus()));
            message = String.format(message, encodeUTF8(user.getNickName()), encodeUTF8(homeSecurity.getHomeNickname()),
                    securityStatusName);
        }
        return message;
    }

    private String encodeUTF8(String str) {
        return str;
    }

    /**
     * 사용자 언어에 따른 푸시 제목 반환
     * 
     * @param locale
     * @param type
     * @return
     */
    private String getLocaleTitle(String locale, String type) {
        locale = locale.split("_")[0];
        String pushTitle = null;
        if (type.equals(MSG_TYPE_SECURITY_ALERT)) {
            pushTitle = env.getProperty(String.format("push.security.%s.title", locale));
        } else if (type.equals(MSG_TYPE_CONTROLLABLE_DEVICE_NOTIFY)) {
            pushTitle = env.getProperty(String.format("push.notify.%s.title", locale));
        } else if (type.equals(MSG_TYPE_SECURITY_SETTING)) {
            pushTitle = env.getProperty(String.format("push.security.%s.title", locale));
        }
        return pushTitle;
    }

    /**
     * 홈내의 사용자들을 모두 반환
     * 
     * @param homeNo
     * @return
     */
    private List<User> getUsersByHomeNo(int homeNo) {
        List<User> users = null;
        List<ShareHome> shareHome = shareHomeRepository.findByHomeNo(homeNo);
        if (shareHome.size() > 0) {
            List<Integer> usersNo = new ArrayList<Integer>();
            shareHome.stream().forEach(share -> {
                usersNo.add(share.getUserNo());
            });
            if (usersNo.size() > 0) {
                users = userRepository.findByNoIn(usersNo);
            }
        }
        return users;
    }

    /**
     * RESTful 로 요청받은 방범상태 변경 처리
     * 
     * @param iotAccount
     * @param request
     * @return
     */
    public ResponseEntity<String> updateHomeSecurity(String iotAccount, HashMap<String, Integer> request) {
        HttpStatus httpStatus = HttpStatus.OK;
        List<User> users = userRepository.findByUserEmail(iotAccount);
        if (users.size() > 0) {
            PWHomeSecurity homeSecurity = homeSecurityRepository.getHomeByHomeNo(request.get("home_no"));
            if (isNull(homeSecurity)) {
                homeSecurity = new PWHomeSecurity();
            }
            homeSecurity.setHomeNo(request.get("home_no"));
            homeSecurity.setSecurityStatus(request.get("security_status"));
            homeSecurity.setUpdatedAt(LocalDateTime.now().toString());
            homeSecurityRepository.save(homeSecurity);
            User user = users.get(0);
            String message = getLocaleMessage(user.getLocale(), MSG_TYPE_SECURITY_SETTING, homeSecurity, null, user);
            String title = getLocaleTitle(user.getLocale(), MSG_TYPE_SECURITY_SETTING);
            JSONObject payload = getFCMPushMessage(user.getPushToken(), message, title,
                    String.valueOf(request.get("home_no")), homeSecurity.getHomeNo());
            LOGGER.info("====== PUSH sending ======");
            LOGGER.info(payload.toString());
            fireBasePushClient.send(payload.toString(), FCM_SERVER_KEY);
            addHistory(MSG_TYPE_SECURITY_SETTING, message.toString(), homeSecurity);
        } else {
            httpStatus = HttpStatus.FORBIDDEN;
        }
        return new ResponseEntity<String>("", httpStatus);
    }

    /**
     * 방범상태값 요청에 대한 디비 조회
     * 
     * @param iotAccount
     * @param homeNo
     * @return
     */
    public ResponseEntity<String> getHomeSecurityStatus(String iotAccount, int homeNo) {
        PWHomeSecurity homeSecurity = homeSecurityRepository.findByHomeNo(homeNo);
        JSONObject jo = new JSONObject();
        try {
            jo.put("home_no", homeNo);
            jo.put("security_status", 0);
            if (!isNull(homeSecurity)) {
                jo.put("security_status", homeSecurity.getSecurityStatus());
            }
        } catch (JSONException e) {
            LOGGER.warn("JSONException at getHomeSecurityStatus", e);
        }
        return new ResponseEntity<String>(jo.toString(), HttpStatus.OK);
    }

}
