package com.ht.connected.home.backend.service.impl.base;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.service.base.CrudService;

public class CrudServiceImpl<T, P extends Serializable> extends Common implements CrudService<T, P> {
	
	private final JpaRepository<T, P> jpaRepository;

	public CrudServiceImpl(JpaRepository<T, P> jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public T insert(T t) {
		return jpaRepository.saveAndFlush(t);
	}

	@Override
	public List<T> getAll() {
		return jpaRepository.findAll();
	}

	@Override
	public void delete(P p) {
		jpaRepository.delete(p);
	}

	@Override
	public Page<T> getAll(PageRequest pageRequest) {
		return jpaRepository.findAll(pageRequest);
	}

	@Override
	public T getOne(P p) {
		return jpaRepository.getOne(p);
	}
	
	@Override
	public T findOne(P p) {
		return jpaRepository.findOne(p);
	}
	@Override
	public T save(T t) {
		return jpaRepository.save(t);
	}

	
}
