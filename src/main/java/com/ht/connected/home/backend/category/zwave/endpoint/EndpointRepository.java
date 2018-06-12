package com.ht.connected.home.backend.category.zwave.endpoint;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EndpointRepository extends JpaRepository<Endpoint, Integer>{
    List<Endpoint> findByZwaveNo(int zwaveNo);
    void deleteByZwaveNo(int zwaveNo);
}
