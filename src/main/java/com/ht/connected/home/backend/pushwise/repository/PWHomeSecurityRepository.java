package com.ht.connected.home.backend.pushwise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ht.connected.home.backend.pushwise.model.entity.PWHomeSecurity;

/**
 * 홈보안 테이블 리파지토리
 * 
 * @author 구정화
 *
 */
@Repository
public interface PWHomeSecurityRepository extends JpaRepository<PWHomeSecurity, Integer> {

    @Query(nativeQuery = true)
    PWHomeSecurity getHomeSecurityGatewaySerial(@Param(value = "zwaveNo") int zwaveNo);

    @Query(nativeQuery = true)
    PWHomeSecurity getHomeByHomeNo(@Param(value = "homeNo") int homeNo);

    PWHomeSecurity findByHomeNo(int homeNo);

}
