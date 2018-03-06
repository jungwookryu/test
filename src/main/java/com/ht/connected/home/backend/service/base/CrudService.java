package com.ht.connected.home.backend.service.base;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface CrudService<T, P> {
	
	@NotNull
	T insert(@NotNull T t);
	
	@NotNull
	List<T> getAll();
	
	T getOne(P p); 
	
	@NotNull
	Page<T> getAll(PageRequest pageRequest);
	
    void delete(P p);

	T findOne(P p);
	
}