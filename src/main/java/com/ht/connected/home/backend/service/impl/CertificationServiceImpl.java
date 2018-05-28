package com.ht.connected.home.backend.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.common.ByteUtil;
import com.ht.connected.home.backend.model.dto.CategoryActive;
import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.model.entity.Certification;
import com.ht.connected.home.backend.repository.CertificationRepository;
import com.ht.connected.home.backend.service.CertificationService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

@Service
public class CertificationServiceImpl extends CrudServiceImpl<Certification, Integer> implements CertificationService {

    private CertificationRepository certificationRepository;

    @Autowired
    public CertificationServiceImpl(CertificationRepository certificationRepository) {
        super(certificationRepository);
        this.certificationRepository = certificationRepository;
    }

    /**
     * 인증프로토콜의 경우 디비에 JSON을 저장하는 기능
     * @param certPayload
     */
    public void updateCertification(ZwaveRequest zwaveRequest, String payload) {
        Certification certification = new Certification();
        certification.setPayload(payload);
        certification.setController(CategoryActive.gateway.zwave.name());
        certification.setSerial(zwaveRequest.getSerialNo());
        certification.setModel(zwaveRequest.getModel());
        certification.setVersion(zwaveRequest.getVersion());
        certification.setMethod(ByteUtil.getHexString(zwaveRequest.getClassKey()));
        certification.setContext(ByteUtil.getHexString(zwaveRequest.getCommandKey()));
        List<Certification> requestCertification = certificationRepository.findBySerialAndMethodAndContext(certification.getSerial(), certification.getMethod(), certification.getCertificationType());
        if(requestCertification.size() > 0) {
            Certification diffCertificaton = requestCertification.get(requestCertification.size()-1);
            if(!diffCertificaton.getPayload().equals(certification.getPayload())){
                certification.setUptime(new Date());
                certificationRepository.save(certification);    
            }
        }else {
            certification.setUptime(new Date());
            certificationRepository.save(certification);
        }
    }
}
