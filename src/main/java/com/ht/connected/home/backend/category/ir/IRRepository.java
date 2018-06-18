/**
 * Project : ht-connected-home-backend-server
 * Package : com.ht.connected.home.backend.repository
 * File : UsersRepository.java
 * Description : 
 * @auther : COM
 * @version : 1.0
 * @since : 2018. 2. 19.
 */
package com.ht.connected.home.backend.category.ir;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ijlee
 *
 */
@Repository
public interface IRRepository extends JpaRepository<IR, Integer> {
    List<IR> findByUserEmail(String userEmail);
    
    List<IR> findByUserEmailAndStatus(String userEmail, String status);
    
    List<IR> findByUserEmailAndSubNumberAndAction(String userEmail, int subNumber, String action);
    
    List<IR> findBySerialAndStatusAndModel(String serial, String status, String model);
    
    List<IR> findBySerialAndStatusAndModelOrUserEmail(String serial, String status, String model, String userEamil);
    
    List<IR> findByIrTypeAndSerialAndActionAndModelOrUserEmail(int irType, String serial, String action, String model, String userEmail);
    
    List<IR> findBySerialAndActionAndModelAndIrTypeAndUserEmail(String serial, String action, String model, String irType, String userEmail);

    List<IR> findByIrTypeAndUserEmailAndSerialAndModel(int irType, String useEmail, String serial, String model);
    void deleteBySubNumber(int subNumber);

    List<IR> getBySubNumber(int no);
    
    void deleteByUserEmailContainingAndGatewayNo(String userEmail, int gatewayNo);
}
