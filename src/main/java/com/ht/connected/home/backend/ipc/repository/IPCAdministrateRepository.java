package com.ht.connected.home.backend.ipc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ht.connected.home.backend.ipc.model.entity.IPCAdministrate;

@Repository
public interface IPCAdministrateRepository extends JpaRepository<IPCAdministrate, Integer>{    
    
}
