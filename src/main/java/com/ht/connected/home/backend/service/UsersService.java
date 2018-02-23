package com.ht.connected.home.backend.service;

import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.service.base.CrudService;

import java.util.List;


public interface UsersService extends CrudService<Users, Integer> {
	List<Users> getUser(String userId);
}
