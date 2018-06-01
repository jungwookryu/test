package com.ht.connected.home.backend.category.zwave.certification;

import com.ht.connected.home.backend.category.zwave.ZWaveRequest;
import com.ht.connected.home.backend.service.base.CrudService;

public interface CertificationService extends CrudService<Certification, Integer> {
    void updateCertification(ZWaveRequest zwaveRequest, String payload);
}
