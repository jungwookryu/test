package com.ht.connected.home.backend.service.impl;

import com.ht.connected.home.backend.model.entity.Gateway;
import com.ht.connected.home.backend.repository.GateWayRepository;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GateWayServiceImpl extends CrudServiceImpl<Gateway , Integer> {
	private GateWayRepository gateWayRepository;

	@Autowired
	public GateWayServiceImpl(@NotNull GateWayRepository gateWayRepository) {
		super(gateWayRepository);
		this.gateWayRepository = gateWayRepository;
	}
}
