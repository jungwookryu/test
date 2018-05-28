package com.ht.connected.home.backend.category.zwave;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ZwaveRepository extends JpaRepository<Zwave, Integer>{

    List<Zwave> findByGatewayNoAndCmdAndStatus(int no, String cmd, String status);
    
    List<Zwave> findByNoAndGatewayNoIn(int no, List gatewayNos);
    
    @Modifying
    @Query("update Zwave z set z.event = ?1, z.status = ?2  where z.no = ?3")
    int setFixedEventAndStatusForNo(String event, String status, int no);

}
