package com.ht.connected.home.backend.device.category.gateway.gatewayCategory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GatewayCategoryRepository extends JpaRepository<GatewayCategory, Integer>{
    int deleteByGatewayNoAndCategoryNo(int gatewayNo, int categoryNo);
    
    List<GatewayCategory> findByGatewayNoAndNodeIdAndCategory(int gatewayNo, int nodeId, String Category);
    int deleteByGatewayNo(int gatewayNo);
    List<GatewayCategory> findByGatewayNo(int gatewayNo);
    void deleteByGatewayNoAndNodeId(int GatewayNo, int nodeId);
}
