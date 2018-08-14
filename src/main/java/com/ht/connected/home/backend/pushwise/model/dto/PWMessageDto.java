package com.ht.connected.home.backend.pushwise.model.dto;


/**
 * 1: 방범 알림
 * 2: 제품 등록 완료 (Host 재부팅 완료)
 * 3: IR 학습 완료
 * 4: 기기 등록 또는 삭제 완료
 * 5: 업데이트 완료 (Host 재부팅 완료)
 * 6: 제어기기 상태 변동 (조명)
 * 7: 제어기기 상태 변동 (플러그)
 * 8: 제어기기 상태 변동 (도어락)
 * 9: 센서류 상태 번경 (자석, 동체, 온습도 등).
 */
public class PWMessageDto {
    public static final PWMessageDto securityUrgency = new PWMessageDto("1", "방범 알림");
    public static final PWMessageDto hostBootUp = new PWMessageDto("2", "제품 등록 완료 (Host 재부팅 완료)");
    public static final PWMessageDto applianceLearn = new PWMessageDto("3", "IR 학습 완료");
    public static final PWMessageDto registerDevice = new PWMessageDto("4", "기기 등록 또는 삭제 완료");
    public static final PWMessageDto hostUpdated = new PWMessageDto("5", "업데이트 완료 (Host 재부팅 완료)");
    public static final PWMessageDto statusLight = new PWMessageDto("6", "제어기기 상태 변동 (조명)");
    public static final PWMessageDto statusPlug = new PWMessageDto("7", "제어기기 상태 변동 (플러그)");
    public static final PWMessageDto statusDoor = new PWMessageDto("8", "제어기기 상태 변동 (도어락)");
    public static final PWMessageDto sensor = new PWMessageDto("9", "센서류 상태 번경 (자석, 동체, 온습도 등)");
    public static final PWMessageDto securityOuting = new PWMessageDto("50", "방범 알림");
    public static final PWMessageDto securityHome = new PWMessageDto("51", "방범 알림");

    private String pushType;

    private String pushMessage;

    public PWMessageDto(String pushType, String pushMessage) {
        this.pushType = pushType;
        this.pushMessage = pushMessage;
    }

    public String getPushType() {
        return pushType;
    }

    public String getPushMessage() {
        return pushMessage;
    }
}
