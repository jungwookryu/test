package com.ht.connected.home.backend.sip.message.service;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.sip.message.model.dto.SipMqttRequestMessageDto;
import com.ht.connected.home.backend.sip.message.model.dto.SipSharedDeviceDto;


/**
 * SIP MQTT 메시지 Subscirber 
 * 메세지의 Method 필드를 참고하여 수신된 메세지를 처리할 메소드를 동적으로 호출한다
 * @author 구정화
 *
 */
@Service
public class SipMqttSubscribeService {

    private static final Log LOGGER = LogFactory.getLog(SipMqttSubscribeService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SipUserService userService;
    
    @Autowired
    private SipMqttPublishService mqttPublishService;
    
    @Autowired
    private SipDeviceService deviceService;
    
    @Autowired
    private SipShareService shareService;
    
    /**
     * 회원가입
     * @param request
     */
    public void user(SipMqttRequestMessageDto request) {
        if (request.getCrudType().equals("add")) {
            String userId = request.getBody().get("userID").toString();
            String userPassword = request.getBody().get("userPassword").toString();
            String userNickname = request.getBody().get("userAlias").toString();
            userService.addUser(userId, userPassword, userNickname);
            mqttPublishService.publish(request, null);
        }
    }
    
    /**
     * 기기 추가 삭제
     * @param request
     */
    public void device(SipMqttRequestMessageDto request) {
        if (request.getCrudType().equals("add")) {
            boolean isSuccess = deviceService.addDevice(request);
            request.setResult(isSuccess);
            mqttPublishService.publish(request, null);
        }else if(request.getCrudType().equals("delete")) {
            deviceService.deleteDevice(request);
            mqttPublishService.publish(request, null);
        }
    }
    
    /**
     * 도어벨정보요청
     * 
     * @param request
     */
    public void devices(SipMqttRequestMessageDto request) {
        HashMap<String, List> body = new HashMap<>();
        String userId = request.getBody().get("userID").toString();
        body.put("owner", deviceService.getAccountInfo(userId, "owner"));
        body.put("shared", deviceService.getAccountInfo(userId, "shared"));
        body.put("ownerShared", deviceService.getAccountInfo(userId, "ownerShared"));
        mqttPublishService.publish(request, body);
    }
    
    /**
     * 공유신청 상태 조회
     * 
     * @param request
     */
    public void sharedRequest(SipMqttRequestMessageDto request) {
        if (request.getCrudType().equals("get")) {
            /**
             * requestSharedProgresss
             */
            String sharedRequestUserId = request.getBody().get("sharedRequestUserID").toString();
            List<SipSharedDeviceDto> sharedDevicesInfo = shareService.getSharedDevices(request, sharedRequestUserId);
            HashMap<String, List<SipSharedDeviceDto>> body = new HashMap<>();
            body.put("devices", sharedDevicesInfo);
            mqttPublishService.publish(request, body);
        } else if (request.getCrudType().equals("set")) {
            String strSerial = request.getBody().get("deviceSN").toString();
            String strOwnerAccount = request.getBody().get("deviceOwnerUserID").toString();
            String strSharedAccount = request.getBody().get("sharedRequestUserID").toString();
            String strRequestType = "shared";
            String strSIPAOR = request.getBody().get("sipAOR").toString();
            deviceService.shareDevice(strSerial, strOwnerAccount, strSharedAccount, strRequestType, strSIPAOR);
            mqttPublishService.publish(request, null);
        }
    }
    
    public void ownerDeviceSharedStatus(SipMqttRequestMessageDto request) {
        if (request.getCrudType().equals("get")) {
            /**
             * requestOwnerSharedList
             */
            String userId = request.getBody().get("userID").toString();
            ArrayList arrayList = deviceService.getAccountInfo(userId, "ownerShared");
            HashMap<String, List> body = new HashMap<>();
            body.put("devices", arrayList);
            mqttPublishService.publish(request, body);
        } else if (request.getCrudType().equals("set")) {
            String strDevSerial = request.getBody().get("deviceSN").toString();
            String strSharedAccount = request.getBody().get("sharedUserID").toString();
            String strSharedState = request.getBody().get("sharedStatus").toString();
            String strOwnerAccount = request.getTopic()[2];
            shareService.updateSharedStatus(strOwnerAccount, strSharedState, strDevSerial, strSharedAccount);
            mqttPublishService.publish(request, objectMapper.valueToTree(request));
        }
    }
    
    public void subscribe(Message<?> message) {
        SipMqttRequestMessageDto request = null;
        String topic = String.valueOf(message.getHeaders().get("mqtt_topic"));
        String payload = String.valueOf(message.getPayload());
        LOGGER.info("messageArrived: Topic=" + topic + ", Payload=" + payload);
        try {
            request = objectMapper.readValue(payload, SipMqttRequestMessageDto.class);
            if (!request.getMethod().equals("Z-Wave G/W")) {
                request.setTopic(topic);
                String method = Introspector.decapitalize(request.getMethod());
                SipMqttSubscribeService.class.getMethod(method, SipMqttRequestMessageDto.class).invoke(this, request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
