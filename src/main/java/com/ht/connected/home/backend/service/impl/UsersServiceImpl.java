package com.ht.connected.home.backend.service.impl;

import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.repository.UsersRepository;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl extends CrudServiceImpl<Users, Integer> {

	private UsersRepository userRepository;

	@Autowired
	public UsersServiceImpl(@NotNull UsersRepository usersRepository) {
		super(usersRepository);
		this.userRepository = usersRepository;
	}
	
	public List<Users> getUser(String userId){
		return userRepository.findByUserId(userId);
	}

}
