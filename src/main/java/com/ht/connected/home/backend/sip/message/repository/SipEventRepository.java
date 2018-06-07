package com.ht.connected.home.backend.sip.message.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ht.connected.home.backend.sip.message.model.entity.SipEvent;

@Repository
public interface SipEventRepository extends JpaRepository<SipEvent, Integer>{

    @Query(nativeQuery = true)
    public List<SipEvent> getSharedDeviceEvents(@Param("sharedDeviceSerialNumber") ArrayList<String> sharedDeviceSerialNumber);
    
    @Query(nativeQuery = true)
    public List<SipEvent> getOwnerDeviceEvents(@Param("ownerDeviceSerialNumber") ArrayList<String> ownerDeviceSerialNumber);
}
