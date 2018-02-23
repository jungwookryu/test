package com.ht.connected.home.backend.service.impl.base;

import com.ht.connected.home.backend.service.base.CrudService;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CrudServiceImpl<T, P extends Serializable> implements CrudService<T, P> {
	
	@Autowired
	private final JpaRepository<T, P> jpaRepository;

	public CrudServiceImpl(@NotNull JpaRepository<T, P> jpaRepository) {
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
		jpaRepository.getOne(p);
		return jpaRepository.getOne(p);
	}

}
