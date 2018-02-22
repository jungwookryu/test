package com.ht.connected.home.backend.service.impl;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.repository.UsersRepository;
import com.ht.connected.home.backend.service.impl.base.CrudImplService;

@Service
public class UserImplService extends CrudImplService<Users, Integer> {

	private UsersRepository userRepository;

	@Autowired
	public UserImplService(@NotNull UsersRepository usersRepository) {
		super(usersRepository);
		this.userRepository = usersRepository;
	}

}
