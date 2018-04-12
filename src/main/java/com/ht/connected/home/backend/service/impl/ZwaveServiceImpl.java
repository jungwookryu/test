package com.ht.connected.home.backend.service.impl;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.service.ZwaveService;
import com.ht.connected.home.backend.service.impl.zwave.ZwaveDefinedHandler;

/**
 * Rest API Zwave 요청/응답 처리 서비스 구현
 * 
 * @author 구정화
 *
 */
@Service
public class ZwaveServiceImpl {

	private static final Log logging = LogFactory.getLog(ZwaveServiceImpl.class);

	@Autowired
	BeanFactory beanFactory;

	public Object execute(HashMap<String, Object> req, ZwaveRequest zwaveRequest, boolean isCert) throws JsonProcessingException {
		ZwaveService handler = (ZwaveService) beanFactory
				.getBean(ZwaveDefinedHandler.handlers.get(zwaveRequest.getClassKey()));
		return handler.execute(req, zwaveRequest, isCert);
	}

}
