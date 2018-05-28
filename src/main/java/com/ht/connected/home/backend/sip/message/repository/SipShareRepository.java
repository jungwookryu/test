package com.ht.connected.home.backend.sip.message.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ht.connected.home.backend.sip.message.model.entity.SipShare;

@Repository
public interface SipShareRepository extends JpaRepository<SipShare, Integer> {

	public List<SipShare> findBySharedAccountAndOwnershipAndSharedStatus(String sharedAccount, String ownership,
			String shareStatus);
	
	public List<SipShare> findByOwnerAccountAndOwnership(String ownerAccount, String ownership);
	
	public List<SipShare> findByOwnershipAndSharedAccount(String ownership, String sharedAccount);

	public List<SipShare> findBySharedAccount(String sharedRequestUserID);

    public SipShare findBySerialNumberAndOwnerAccountAndSharedAccount(String strDevSerial, String strOwnerAccount,
            String strSharedID);

    public SipShare findBySerialNumber(String strSN);


}
