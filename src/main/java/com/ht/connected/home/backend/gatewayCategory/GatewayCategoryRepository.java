package com.ht.connected.home.backend.gatewayCategory;

<<<<<<< HEAD
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GatewayCategoryRepository extends JpaRepository<GatewayCategory, Integer>{
    int deleteByGatewayNoAndCategoryNo(int gatewayNo, int categoryNo);
    int deleteByGatewayNo(int gatewayNo);
    List<GatewayCategory> findByGatewayNo(int gatewayNo);
=======
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatewayCategoryRepository extends JpaRepository<GatewayCategory, Integer>{

>>>>>>> branch 'dev' of https://github.com/injeong/ht-iot-connected-home-backend-server.git
}
