/**
 * Project : ht-connected-home-backend-server
 * Package : com.ht.connected.home.backend.repository
 * File : UsersRepository.java
 * Description : 
 * @auther : COM
 * @version : 1.0
 * @since : 2018. 2. 19.
 */
package com.ht.connected.home.backend.gateway;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author ijlee
 *
 */
@Repository
public interface GatewayRepository extends JpaRepository<Gateway, Integer> {
    
/*    List<Gateway> findByUsersAndStatusContaining(User user, String status);
    
    List<Gateway> findByUsers(User user);*/
    
	Gateway findBySerial(String serial);
	List<Gateway> findByNoIn(List<Integer> nos);
	List<Gateway> findByNoInAndStatusContaining(List<Integer> nos, String status);
	
	@Modifying
	@Query("update Gateway set status = ?1 where no = ?2")
	int setModifyStatusForNo(String status, int no);
	
	void deleteBySerial(String serial);
    List<Gateway> findByModel(String modelName);
}
