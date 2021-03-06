package com.ht.connected.home.backend.sip.message.service;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.sip.message.model.dto.SipMqttRequestMessageDto;
import com.ht.connected.home.backend.sip.message.model.dto.SipSharedDeviceDto;
import com.ht.connected.home.backend.sip.message.model.entity.SipEvent;

/**
 * SIP MQTT 메시지 Subscirber 메세지의 Method 필드를 참고하여 수신된 메세지를 처리할 메소드를 동적으로 호출한다
 * 
 * @author 구정화
 *
 */
@Service
public class SipMqttSubscribeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SipMqttSubscribeService.class);

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

    @Autowired
    private SipEventService eventService;

    /**
     * 회원가입
     * 
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
     * 
     * @param request
     */
    public void device(SipMqttRequestMessageDto request) {
        if (request.getCrudType().equals("add")) {
            boolean isSuccess = deviceService.addDevice(request);
            request.setResult(isSuccess);
            mqttPublishService.publish(request, null);
        } else if (request.getCrudType().equals("delete")) {
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
     * 푸시토큰 업데이트
     * 
     * @param request
     */
    public void pushToken(SipMqttRequestMessageDto request) {
        userService.updateUserToken(request);
        mqttPublishService.publish(request, null);
    }

    /**
     * 기기 등록 여부 검사
     * 
     * @param request
     */
    public void getDeviceRegisterValidCheck(SipMqttRequestMessageDto request) {
        String serialNumber = request.getBody().get("deviceSN").toString();
        String errCode = deviceService.checkRegisteredDevice(serialNumber);
        HashMap<String, String> body = new HashMap<>();
        if (errCode.equals("00") == false) {
            ERROR_CORD errorCode = ERROR_CORD.getMsg(errCode);
            String errMsg = errorCode.getMsg();
            body.put("reason", errMsg);
            request.setResult(false);
        }
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

    /**
     * 이벤트 기록 조회
     * 
     * @param request
     */
    public void getEventList(SipMqttRequestMessageDto request) {
        String userId = request.getBody().get("userID").toString();
        List<SipEvent> events = eventService.getEvents(userId);
        HashMap<String, List<SipEvent>> body = new HashMap<>();
        body.put("list", events);
        mqttPublishService.publish(request, body);
    }

    public void subscribe(Message<?> message) {
        SipMqttRequestMessageDto request = null;
        String topic = String.valueOf(message.getHeaders().get("mqtt_topic"));
        String payload = String.valueOf(message.getPayload());        
        try {
            request = objectMapper.readValue(payload, SipMqttRequestMessageDto.class);
            request.setTopic(topic);
            if ((request.getTopic()[2].equals("request") || request.getTopic()[3].equals("request")) && !request.getMethod().equals("Z-Wave G/W")) {
                LOGGER.info("messageArrived-SIP-Message: Topic=" + topic + ", Payload=" + payload);
                String method = Introspector.decapitalize(request.getMethod());
                SipMqttSubscribeService.class.getMethod(method, SipMqttRequestMessageDto.class).invoke(this, request);
            }
        } catch(IndexOutOfBoundsException e) {
            /**
             * 미디어 서버로 갈 토픽이라면 처리되지 않고 넘김          
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 기기 등록 여부 예외 메세지
     * 
     * @author 구정화
     *
     */
    private enum ERROR_CORD {
        UNKNOWN("알 수 없는 에러입니다.", "99"), OK_Device("등록 가능한 기기입니다.", "00"), Invalid_serial("잘못된 시리얼 번호입니다.",
                "01"), Registered_serial("이미 등록된 시리얼 번호입니다.", "02"), Shared_serial("공유 중인 시리얼 번호입니다.", "03");

        private String msg;
        private String errorCode;

        private ERROR_CORD(String msg, String errorCode) {
            this.msg = msg;
            this.errorCode = errorCode;
        }

        public static ERROR_CORD getMsg(String errCode) {
            for (ERROR_CORD errorData : ERROR_CORD.values()) {
                if (errorData.getErrorCode().equals(errCode))
                    return errorData;
            }
            return UNKNOWN;
        }

        public String getMsg() {
            return msg;
        }

        public String getErrorCode() {
            return errorCode;
        }

    }
}
