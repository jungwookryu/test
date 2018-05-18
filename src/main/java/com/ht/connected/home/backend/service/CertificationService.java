package com.ht.connected.home.backend.service;

import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.model.entity.Certification;
import com.ht.connected.home.backend.service.base.CrudService;

public interface CertificationService extends CrudService<Certification, Integer> {
    void updateCertification(ZwaveRequest zwaveRequest, String payload);
}
