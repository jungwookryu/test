package com.ht.connected.home.backend.service;

import com.ht.connected.home.backend.model.entity.User;
import com.ht.connected.home.backend.service.base.CrudService;


public interface UsersService extends CrudService<User, Integer> {
	User getUser(String userEmail);
	Boolean getExistUser(String userEmail);
	User modify(int no,User user);
	User register(User users);
	
}
