package com.ht.connected.home.backend.sip.message.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ht.connected.home.backend.sip.message.model.entity.SipDevice;

@Repository
public interface SipDeviceRepository extends JpaRepository<SipDevice, Integer>{

    public List<SipDevice> findByOwnerAccountAndOwnership(String ownerAccount, String ownership);

    public SipDevice findBySerialNumber(String strDeviceSerial);

    public SipDevice findBySerialNumberAndUserPassword(String userID, String userPW);
    
    @Query(nativeQuery = true)
    public List<SipDevice> getSharedDevices(String sharedAccount, String ownerAccount);

    public List<SipDevice> findByOwnerAccount(String ownerAccount);

    public List<SipDevice> findBySerialNumberAndOwnerAccount(String strSerial, String strOwnerAccount);
}
