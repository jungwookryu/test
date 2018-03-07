package com.ht.connected.home.backend.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface CrudService<T, P> {
	
	T insert( T t);
	
	List<T> getAll();
	
	T getOne(P p); 
	
	Page<T> getAll(PageRequest pageRequest);
	
    void delete(P p);

	T findOne(P p);
	
}