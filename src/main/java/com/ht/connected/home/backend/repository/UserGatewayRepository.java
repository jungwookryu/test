package com.ht.connected.home.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ht.connected.home.backend.model.entity.UserGateway;


/**
 * user_gateway 테이블 repository
 * @author 구정화
 *
 */
@Repository
public interface UserGatewayRepository extends JpaRepository<UserGateway, Integer>{

	List<UserGateway> findByUserNo(int no);

	UserGateway findByUserNoAndGatewayNo(int no, int no2);

	List<UserGateway> findByGatewayNoIn(List<Integer> gatewayNos);

	
}
