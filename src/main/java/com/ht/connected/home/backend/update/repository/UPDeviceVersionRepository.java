package com.ht.connected.home.backend.update.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ht.connected.home.backend.update.model.entity.UPDeviceVersion;

public interface UPDeviceVersionRepository extends JpaRepository<UPDeviceVersion, Integer> {

    UPDeviceVersion findFirstByOrderBySeqDesc();

    UPDeviceVersion findBySerialNo(String serialNo);
    
    @Query(nativeQuery = true)
    List<UPDeviceVersion> getByModelName(@Param(value="modelName") String modelName);

    

}
