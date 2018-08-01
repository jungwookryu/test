package com.ht.connected.home.backend.device.category.zwave.endpoint;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EndpointRepository extends JpaRepository<Endpoint, Integer>{
    List<Endpoint> findByZwaveNo(int zwaveNo);
    void deleteByZwaveNo(int zwaveNo);
    
    Endpoint findByZwaveNoAndEpid(int zwaveNo, int epid);
    
    @Modifying
    @Query("update Endpoint z set z.nickname = ?1  where z.no = ?2")
    int setModifyNicknameForNo(String nickname, int no);
    
}
