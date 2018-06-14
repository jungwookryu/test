package com.ht.connected.home.backend.category.zwave;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ZWaveRepository extends JpaRepository<ZWave, Integer>{

    List<ZWave> findByNoAndGatewayNoIn(int no, List gatewayNos);
    
    void deleteByNodeId(int nodeId);
    
    List<ZWave> findByGatewayNo(int gatewayNos);
    List<ZWave> findByNodeId(int nodeId);
    List<ZWave> findByGatewayNoAndStatus(int gatewayNos, String status);
    
    @Modifying
    @Query("update ZWave z set z.status = ?1  where z.no = ?2")
    int setFixedStatusForNo(String status, int no);
    
    
    int deleteByGatewayNo(int gatewayNo);
    
    @Modifying
    @Query("update ZWave z set z.status = ?1  where z.gatewayNo = ?2")
    int updateStatusByGatewayNo(String status, int gatewayNo);


}
