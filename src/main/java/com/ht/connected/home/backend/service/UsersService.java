package com.ht.connected.home.backend.service;

import com.ht.connected.home.backend.model.entity.UserDetail;
import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.service.base.CrudService;


public interface UsersService extends CrudService<Users, Integer> {
	Users getUser(String userEmail);
	Boolean getExistUser(String userEmail);
	Users modify(int no,Users user);
	Users register(Users users);
	
}
