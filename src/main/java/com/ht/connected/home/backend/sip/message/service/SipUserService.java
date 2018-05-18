package com.ht.connected.home.backend.sip.message.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.sip.message.model.entity.SipUser;
import com.ht.connected.home.backend.sip.message.repository.SipUserRepository;

@Service
public class SipUserService {

    @Autowired
    private SipUserRepository userRepository;


    public void addUser(String userId, String userPassword, String userNickname) {
        SipUser user = new SipUser();
        user.setUserId(userId);
        user.setUserPassword(userPassword);
        user.setUserNickname(userNickname);
        user.setDeviceType("smartphone");
        user.setUserAor(userId.replace("@", "^"));
        userRepository.save(user);
    }


}
