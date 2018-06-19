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

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author ijlee
 *
 */
@Repository
public interface IRRepository extends JpaRepository<IR, Integer> {
    
    List<IR> findByUserEmailContainingAndStatusAndSerial(String userEmail, String status, String serial);
    
    List<IR> findByUserEmailContainingAndSubNumberAndAction(String userEmail, int subNumber, String action);
    
    List<IR> findBySerialAndStatusAndModel(String serial, String status, String model);
    
    List<IR> findBySubNumberAndSerialAndActionAndModelAndUserEmailContaining(int subNumber, String serial, String action, String model, String userEmail);
    
    List<IR> findByIrTypeAndUserEmailContainingAndSerialAndModel(int irType, String useEmail, String serial, String model);
    void deleteBySubNumber(int subNumber);

    List<IR> getBySubNumber(int no);
    
    void deleteByUserEmailContainingAndGatewayNo(String userEmail, int gatewayNo);
    
    void deleteByStatusAndSerial(String status, String serial);
    
    @Modifying
    @Transactional
    @Query("update IR set status = ?1 where serial = ?2")
    int setModifyStatusForSerial(String status, String serial);
    
    @Modifying
    @Transactional
    @Query("update IR set status = ?1 where serial = ?2 and status= ?3")
    int setModifyStatusForSerialAndStatus(String status, String serial, String status1);
    
}
