package com.ht.connected.home.backend.ipc.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ht.connected.home.backend.ipc.model.entity.IPCDevice;

@Repository
public interface IPCDeviceRepository extends JpaRepository<IPCDevice, Integer>{

    IPCDevice findByDeviceSerial(String string);   
    
    @Transactional
    void deleteByDeviceSerial(String deviceSerial);
    
}
