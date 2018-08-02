package com.ht.connected.home.backend.ipc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ht.connected.home.backend.ipc.model.entity.IPCAccount;

/**
 * IPC 서브계정 정보 리파지토리
 * 
 * @author 구정화
 *
 */
@Repository
public interface IPCAccountRepository extends JpaRepository<IPCAccount, Integer>{

    IPCAccount findByIotAccount(String iotAccount);    
    
}
