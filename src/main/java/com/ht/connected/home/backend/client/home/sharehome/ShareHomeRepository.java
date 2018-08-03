package com.ht.connected.home.backend.client.home.sharehome;

/**
 * Project : ht-connected-home-backend-server
 * Package : com.ht.connected.home.backend.repository
 * File : UsersRepository.java
 * Description : 
 * @auther : COM
 * @version : 1.0
 * @since : 2018. 2. 19.
 */

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ijlee
 *
 */
@Repository
public interface ShareHomeRepository extends JpaRepository<ShareHome, Integer> {
    List<ShareHome> findByUserNo(int userNo);
    void deleteByHomeNoAndUserNo(int homeNo, int userNo);
    ShareHome findByUserNoAndHomeNo(int userNo, int homeNo);
}
