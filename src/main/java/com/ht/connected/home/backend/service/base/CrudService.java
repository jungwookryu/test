package com.ht.connected.home.backend.service.base;

import javax.validation.constraints.NotNull;

import java.util.List;

public interface CrudService<T, P> {
	
	@NotNull
	T insert(@NotNull T t);
	
	@NotNull
	List<T> getAll();

    void delete(P id);
	
}
