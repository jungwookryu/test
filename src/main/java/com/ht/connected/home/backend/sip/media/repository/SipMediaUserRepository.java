package com.ht.connected.home.backend.sip.media.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ht.connected.home.backend.sip.media.model.entity.SipMediaUser;

@Repository
public interface SipMediaUserRepository extends JpaRepository<SipMediaUser, Integer> {

    public SipMediaUser findByUserId(String userId);

    public SipMediaUser findByUserIdAndUserPassword(String userID, String userPW);

    @Query(nativeQuery = true)
    public List<SipMediaUser> getPushTokens(@Param("serialNumber") String serialNumber,
            @Param("phoneType") String phoneType, @Param("deviceType1") String deviceType1,
            @Param("deviceType2") String deviceType2);

}
