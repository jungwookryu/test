/**
 * Project : ht-connected-home-backend-server
 * Package : com.ht.connected.home.backend.repository
 * File : UsersRepository.java
 * Description : 
 * @auther : COM
 * @version : 1.0
 * @since : 2018. 2. 19.
 */
package com.ht.connected.home.backend.category.zwave.certification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ijlee
 *
 */
@Repository
public interface CertificationRepository extends JpaRepository<Certification, Integer> {

	public List<Certification> findBySerialAndMethodAndContext(String serial, String method, String context);
  
}
