package com.ht.connected.home.backend.sip.media.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ht.connected.home.backend.sip.media.model.entity.SipMediaEvent;

@Repository
public interface SipMediaEventRepository extends JpaRepository<SipMediaEvent, Integer>{

}
