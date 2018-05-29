package com.ht.connected.home.backend.category.zwave.Certification;

import com.ht.connected.home.backend.category.zwave.ZwaveRequest;
import com.ht.connected.home.backend.service.base.CrudService;

public interface CertificationService extends CrudService<Certification, Integer> {
    void updateCertification(ZwaveRequest zwaveRequest, String payload);
}
