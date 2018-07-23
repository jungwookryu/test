package com.ht.connected.home.backend.update.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ht.connected.home.backend.update.model.entity.UPFileVersion;

public interface UPFileVersionRepository extends JpaRepository<UPFileVersion, Integer> {

    UPFileVersion findFirstByOrderBySeqDesc();

    UPFileVersion findByModelName(String modelName);

    

}
