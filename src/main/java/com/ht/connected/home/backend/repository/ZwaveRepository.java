package com.ht.connected.home.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ht.connected.home.backend.model.entity.Zwave;

public interface ZwaveRepository extends JpaRepository<Zwave, Integer>{

}
