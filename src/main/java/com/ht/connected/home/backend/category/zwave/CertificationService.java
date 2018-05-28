package com.ht.connected.home.backend.category.zwave;

import com.ht.connected.home.backend.service.base.CrudService;

public interface CertificationService extends CrudService<Certification, Integer> {
    void updateCertification(ZwaveRequest zwaveRequest, String payload);
}
