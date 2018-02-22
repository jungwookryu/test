package com.ht.connected.home.backend.service.impl.base;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ht.connected.home.backend.service.base.CrudService;

public class CrudServiceImpl<T, P extends Serializable> implements CrudService<T, P> {
	private final JpaRepository<T, P> jpaRepository;

	public CrudServiceImpl(@NotNull JpaRepository<T, P> jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	public T insert(T t) {
		return jpaRepository.saveAndFlush(t);
	}

	public List<T> getAll() {
		return jpaRepository.findAll();
	}

	public void delete(P p) {
		jpaRepository.delete(p);
	}

	public Page<T> getAll(PageRequest pageRequest) {
		return jpaRepository.findAll(pageRequest);
	}

	public T getOne(P p) {
		jpaRepository.getOne(p);
		return jpaRepository.getOne(p);
	}

}
