package com.ht.connected.home.backend.pushwise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ht.connected.home.backend.pushwise.model.entity.PWPushHistory;

/**
 * 푸시 발송 기록 테이블 리파지토리
 * 
 * @author 구정화
 *
 */
@Repository
public interface PWPushHistoryRepository extends JpaRepository<PWPushHistory, Integer>{
    
}
