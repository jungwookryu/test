package com.ht.connected.home.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ht.connected.home.backend.model.entity.GatewayCategory;
import com.ht.connected.home.backend.model.entity.Zwave;

public interface GatewayCategoryRepository extends JpaRepository<GatewayCategory, Integer>{

}
