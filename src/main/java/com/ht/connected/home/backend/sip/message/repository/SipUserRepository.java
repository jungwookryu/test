package com.ht.connected.home.backend.sip.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ht.connected.home.backend.sip.message.model.entity.SipUser;

@Repository
public interface SipUserRepository extends JpaRepository<SipUser, Integer> {

    public SipUser findByUserId(String userId);

    public SipUser findByUserIdAndUserPassword(String userID, String userPW);

}
