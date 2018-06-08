package com.ht.connected.home.backend.sip.media.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.sip.media.model.entity.SipMediaUser;
import com.ht.connected.home.backend.sip.media.repository.SipMediaUserRepository;


/**
 * 미디어 서버 유저 정보 서비스
 * 
 * @author 구정화
 *
 */
@Service
public class SipMediaUserService {

    @Autowired
    private SipMediaUserRepository userRepository;

    
    /**
     * 유저 푸시 토큰 조회
     * 
     * @param serialNumber
     * @param phoneType
     * @param deviceType
     * @return
     */
    public List<String> getPushTokens(String serialNumber, String phoneType, String deviceType) {
        ArrayList<String> tokens = new ArrayList<String>();
        String deviceType1 = null;
        String deviceType2 = null;
        if (deviceType.equalsIgnoreCase("debug")) {
            deviceType1 = "debug";
            deviceType2 = "dev";
        } else {
            deviceType1 = "release";
            deviceType2 = "smartphone";
        }
        List<SipMediaUser> users = userRepository.getPushTokens(serialNumber, phoneType, deviceType1, deviceType2);
        users.stream().forEach(user -> {
            if(user.getPushToken().length() > 10) {
                tokens.add(user.getPushToken());
            }
        });
        return tokens;
    }
}
