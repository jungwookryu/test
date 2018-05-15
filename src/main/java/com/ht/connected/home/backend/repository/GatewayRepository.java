/**
 * Project : ht-connected-home-backend-server
 * Package : com.ht.connected.home.backend.repository
 * File : UsersRepository.java
 * Description : 
 * @auther : COM
 * @version : 1.0
 * @since : 2018. 2. 19.
 */
package com.ht.connected.home.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ht.connected.home.backend.model.entity.Gateway;

/**
 * @author ijlee
 *
 */
@Repository
public interface GatewayRepository extends JpaRepository<Gateway, Integer> {
	Gateway findBySerial(String serial);
	List<Gateway> findByNoIn(List<Integer> nos);
}
