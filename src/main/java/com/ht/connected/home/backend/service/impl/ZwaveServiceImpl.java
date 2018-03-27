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


/**
 * Rest API Zwave 요청/응답 처리 서비스 구현
 * @author 구정화
 *
 */
@Service
public class ZwaveServiceImpl extends CrudServiceImpl<Certification, Integer> implements ZwaveService {
	private CertificationRepository certificationRepository;

	/**
	 * 코딩 패턴 준수, 맴버변수에 repository 대입
	 * @param certificationRepository
	 */
	@Autowired
	public ZwaveServiceImpl( CertificationRepository certificationRepository) {
		super(certificationRepository);
		this.certificationRepository = certificationRepository;
	}
	
	@Autowired
	private BeanFactory beanFactory;

	/**
	 * zwave 핸들러 맵팽 및 해당 핸들러 실행
	 * 인증과 서비스 앱 공통 사용
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object execute(HashMap<String, Object> req, ZwaveRequest zwaveRequest, boolean isCert) {
		return null;
	}
}
