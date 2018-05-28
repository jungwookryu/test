package com.ht.connected.home.backend.gatewayCategory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ht.connected.home.backend.category.zwave.Zwave;

public interface GatewayCategoryRepository extends JpaRepository<GatewayCategory, Integer>{

}
