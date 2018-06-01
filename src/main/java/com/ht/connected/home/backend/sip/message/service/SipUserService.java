package com.ht.connected.home.backend.sip.message.service;

import static java.util.Objects.isNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.sip.message.model.dto.SipMqttRequestMessageDto;
import com.ht.connected.home.backend.sip.message.model.entity.SipUser;
import com.ht.connected.home.backend.sip.message.repository.SipUserRepository;

@Service
public class SipUserService {

    @Autowired
    private SipUserRepository userRepository;


    /**
     * 사용자 등록
     * 
     * @param userId
     * @param userPassword
     * @param userNickname
     */
    public void addUser(String userId, String userPassword, String userNickname) {
        SipUser user = new SipUser();
        user.setUserId(userId);
        user.setUserPassword(userPassword);
        user.setUserNickname(userNickname);
        user.setDeviceType("smartphone");
        user.setUserAor(userId.replace("@", "^"));
        userRepository.save(user);
    }
    
    /**
     * 푸시토큰 업데이트
     * 
     * @param request
     * @return
     */
    public boolean updateUserToken(SipMqttRequestMessageDto request) {
        String userId = request.getBody().get("userID").toString();
        SipUser user = userRepository.findByUserId(userId);
        String token = "";
        user.setPushToken("");
        if(request.getCrudType().equals("set")) {
            String deviceType = "smartphone";
            token = request.getBody().get("distPushToken").toString();            
            String phoneType = request.getBody().get("phoneType").toString();
            if (phoneType.equalsIgnoreCase("iphone") && !isNull(request.getBody().get("sipPushToken"))) {
                token = request.getBody().get("sipPushToken").toString();
            }             
            user.setPushToken(token);
            user.setPhoneType(phoneType);
            user.setDeviceType(deviceType);
        }   
        user = userRepository.save(user);    
        return user.getPushToken().equals(token);
    }


}
