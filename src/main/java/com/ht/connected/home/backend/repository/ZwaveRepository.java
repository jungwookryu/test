package com.ht.connected.home.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ht.connected.home.backend.model.entity.Zwave;

public interface ZwaveRepository extends JpaRepository<Zwave, Integer>{

    List<Zwave> findByGatewayNoAndCmdAndStatus(int no, String cmd, String status);

}
