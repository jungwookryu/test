package com.ht.connected.home.backend.service.impl;

import com.ht.connected.home.backend.model.entity.ConfigInfo;
import com.ht.connected.home.backend.repository.ConfigInfoRepository;
import com.ht.connected.home.backend.service.ConfigInfoService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigInfoServiceImpl extends CrudServiceImpl<ConfigInfo , Integer> implements ConfigInfoService {
	private ConfigInfoRepository configInfoRepository;

	@Autowired
	public ConfigInfoServiceImpl( ConfigInfoRepository configInfoRepository) {
		super(configInfoRepository);
		this.configInfoRepository = configInfoRepository;
	}
}
