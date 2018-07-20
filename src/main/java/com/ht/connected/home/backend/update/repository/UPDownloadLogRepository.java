package com.ht.connected.home.backend.update.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ht.connected.home.backend.update.model.entity.UPDownloadLog;

public interface UPDownloadLogRepository extends JpaRepository<UPDownloadLog, Integer> {

}
