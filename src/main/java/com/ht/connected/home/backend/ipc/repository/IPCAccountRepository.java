package com.ht.connected.home.backend.ipc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ht.connected.home.backend.ipc.model.entity.IPCAccount;

@Repository
public interface IPCAccountRepository extends JpaRepository<IPCAccount, Integer>{

    IPCAccount findByIotAccount(String iotAccount);    
    
}
