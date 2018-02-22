package com.ht.connected.home.backend.service.impl;

import com.ht.connected.home.backend.model.entity.ConfigInfo;
import com.ht.connected.home.backend.repository.ConfigInfoRepository;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IRServiceImpl extends CrudServiceImpl<ConfigInfo , Integer> {
	private ConfigInfoRepository configInfoRepository;

	@Autowired
	public IRServiceImpl(@NotNull ConfigInfoRepository configInfoRepository) {
		super(configInfoRepository);
		this.configInfoRepository = configInfoRepository;
	}
}
