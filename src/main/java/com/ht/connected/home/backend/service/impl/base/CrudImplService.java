package com.ht.connected.home.backend.service.impl.base;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ht.connected.home.backend.service.base.CrudService;

public class CrudImplService<T, P extends Serializable> implements CrudService<T, P> {
	private final JpaRepository<T, P> jpaRepository;

	public CrudImplService(@NotNull JpaRepository<T, P> jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	public T insert(T t) {
		return jpaRepository.saveAndFlush(t);
	}

	public List<T> getAll() {
		return jpaRepository.findAll();
	}

	public void delete(P id) {
		jpaRepository.delete(id);
	}

}
