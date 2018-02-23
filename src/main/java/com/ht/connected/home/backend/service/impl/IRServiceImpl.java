package com.ht.connected.home.backend.service.impl;

import com.ht.connected.home.backend.model.entity.IR;
import com.ht.connected.home.backend.repository.IRRepository;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IRServiceImpl extends CrudServiceImpl<IR , Integer> {
	private IRRepository irRepository;

	@Autowired
	public IRServiceImpl(@NotNull IRRepository irRepository) {
		super(irRepository);
		this.irRepository = irRepository;
	}
}
