/**
 * Project : ht-connected-home-backend-server
 * Package : com.ht.connected.home.backend.repository
 * File : UsersRepository.java
 * Description : 
 * @auther : COM
 * @version : 1.0
 * @since : 2018. 2. 19.
 */
package com.ht.connected.home.backend.emergency;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ijlee
 *
 */
@Repository
public interface UserEmergencyRepository extends JpaRepository<UserEmergency, Integer> {
    List<UserEmergency> findByUserEmailAndGatewayNo(String userEmail, int gatewayNo);
    List<UserEmergency> findByUserEmail(String userEmail);
    int deleteByUserEmailAndGatewayNo(String userEmail, int gatewayNo);
}
