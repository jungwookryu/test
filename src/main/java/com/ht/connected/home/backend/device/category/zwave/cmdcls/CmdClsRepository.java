package com.ht.connected.home.backend.device.category.zwave.cmdcls;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CmdClsRepository extends JpaRepository<CmdCls, Integer>{
    void deleteByEndpointNo(int endpointNo);
}
