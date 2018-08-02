package com.ht.connected.home.backend.ipc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ht.connected.home.backend.ipc.model.entity.IPCAdministrate;

/**
 * IPC 마스터 계정 리파지토리
 * 
 * @author 구정화
 *
 */
@Repository
public interface IPCAdministrateRepository extends JpaRepository<IPCAdministrate, Integer>{    
    
}
