package com.ht.connected.home.backend.service.impl;

import com.ht.connected.home.backend.model.entity.CommonCode;
import com.ht.connected.home.backend.repository.CommonCodeRepository;
import com.ht.connected.home.backend.service.CommonCodeService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonCodeServiceImpl extends CrudServiceImpl<CommonCode , Integer> implements CommonCodeService {
	private CommonCodeRepository commonCodeRepository;

	@Autowired
	public CommonCodeServiceImpl(CommonCodeRepository commonCodeRepository) {
		super(commonCodeRepository);
		this.commonCodeRepository = commonCodeRepository;
	}
}
