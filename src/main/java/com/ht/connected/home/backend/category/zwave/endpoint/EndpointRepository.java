package com.ht.connected.home.backend.category.zwave.endpoint;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.DeleteMapping;

public interface EndpointRepository extends JpaRepository<Endpoint, Integer>{
    List<Endpoint> findByZwaveNo(int zwaveNo);
    void deleteByZwaveNo(int zwaveNo);
    
}
