package com.ht.connected.home.backend.pushwise.service;

import org.springframework.stereotype.Service;

/**
 * 이벤트의 발생기기 확인
 * 
 * @author 구정화
 *
 */
@Service
public class PWNotifyCatchService {

    private static final Integer MAGNETIC_DEVICE_CODE = 6;
    private static final Integer MAGNETIC_EVENT_OPEN = 22;
    private static final Integer MAGNETIC_EVENT_CLOSE = 23;
    private static final Integer MOTION_DEVICE_CODE = 7;
    private static final Integer MOTION_EVENT_DETECTION = 8;
    private static final Integer SECURITY_STATUS_HOME = 2;
    private static final Integer SECURITY_STATUS_OUT = 1;

    /**
     * 자석 감지 여부
     * 
     * @param eventCode
     * @param deviceType
     * @return
     */
    public boolean isMotionDetected(Integer eventCode, Integer deviceType, Integer securityStatus) {
        boolean detected = false;
        if (securityStatus.equals(SECURITY_STATUS_OUT)) {
            if (deviceType.equals(MOTION_DEVICE_CODE) && eventCode.equals(MOTION_EVENT_DETECTION)) {
                detected = true;
            }
        }
        return detected;
    }

    /**
     * 동체 감지 여부
     * 
     * @param eventCode
     * @param deviceType
     * @return
     */
    public boolean isMagneticDetected(Integer eventCode, Integer deviceType, Integer securityStatus) {
        boolean detected = false;
        if (securityStatus.equals(SECURITY_STATUS_HOME)) {
            if (deviceType.equals(MAGNETIC_DEVICE_CODE)
                    && (eventCode.equals(MAGNETIC_EVENT_OPEN) || eventCode.equals(MAGNETIC_EVENT_CLOSE))) {
                detected = true;
            }
        }
        return detected;
    }

    /**
     * 푸시할 기기 이벤트인지
     * 
     * @param eventName
     * @return
     */
    public boolean isControllableDeviceNotify(String eventName) {
        return eventName.toLowerCase().equals("open") || eventName.toLowerCase().equals("close")
                || eventName.toLowerCase().equals("on") || eventName.toLowerCase().equals("off");
    }

}
