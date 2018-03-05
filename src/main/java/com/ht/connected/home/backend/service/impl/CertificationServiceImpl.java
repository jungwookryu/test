package com.ht.connected.home.backend.service.impl;

import com.ht.connected.home.backend.model.entity.Certification;
import com.ht.connected.home.backend.repository.CertificationRepository;
import com.ht.connected.home.backend.service.CertificationService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificationServiceImpl extends CrudServiceImpl<Certification, Integer> implements CertificationService{

	private CertificationRepository certificationRepository;

	@Autowired
	public CertificationServiceImpl(@NotNull CertificationRepository certificationRepository) {
		super(certificationRepository);
		this.certificationRepository = certificationRepository;
	}

}
