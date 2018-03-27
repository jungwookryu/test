package com.ht.connected.home.backend.service;

import java.util.HashMap;

import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.model.entity.Certification;
import com.ht.connected.home.backend.service.base.CrudService;

/**
 * zwave 서비스 인터페이스 
 * @author 구정화
 *
 */
public interface ZwaveService extends CrudService<Certification, Integer>{

	/**
	 * zwave 인증과 서비스 앱 프로토몰 공통 사용
	 * @param req
	 * @param zwaveCommonRequest
	 * @param b 인증프로토몰일경우 true
	 * @return
	 */
	Object execute(HashMap<String, Object> req, ZwaveRequest zwaveCommonRequest, boolean b);

	
}
