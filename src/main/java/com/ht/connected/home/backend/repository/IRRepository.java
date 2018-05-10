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

import com.ht.connected.home.backend.model.entity.IR;

/**
 * @author ijlee
 *
 */
@Repository
public interface IRRepository extends JpaRepository<IR, Integer> {
    List<IR> findByUserEmail(String useremail);
    
    List<IR> findByUserEmailAndStatus(String useremail, String status);
    
    List<IR> findByUserEmailAndSubNumberAndAction(String useremail, int subNumber, String action);
    
    List<IR> findBySerialAndStatusAndModel(String serial, String status, String model);
    
    List<IR> findBySerialAndStatusAndModelOrUserEmail(String serial, String status, String model, String userEamil);
    
    List<IR> findByIrTypeAndSerialAndActionAndModelOrUserEmail(int irType, String serial, String action, String model, String userEmail);
    
    List<IR> findBySerialAndActionAndModelAndIrTypeAndUserEmail(String serial, String action, String model, String irType, String userEmail);

    List<IR> findByIrTypeAndUserEmailAndSerialAndModel(int irType, String useEmail, String serial, String model);
    void deleteBySubNumber(int subNumber);

    List<IR> getBySubNumber(int no);
}
