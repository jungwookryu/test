package com.ht.connected.home.backend.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.model.entity.Certification;
import com.ht.connected.home.backend.repository.CertificationRepository;
import com.ht.connected.home.backend.service.ZwaveService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;


@Service
public class ZwaveServiceImpl extends CrudServiceImpl<Certification, Integer> implements ZwaveService {
	private CertificationRepository certificationRepository;

	@Autowired
	public ZwaveServiceImpl( CertificationRepository certificationRepository) {
		super(certificationRepository);
		this.certificationRepository = certificationRepository;
	}
	
	@Autowired
	private BeanFactory beanFactory;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object execute(HashMap<String, Object> req, ZwaveRequest zwaveRequest, boolean isCert) {
		return null;
	}
}
