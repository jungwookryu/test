package com.ht.connected.home.backend.service.impl;

import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.model.entity.UserDetail;
import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.repository.UsersRepository;
import com.ht.connected.home.backend.service.UsersService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl extends CrudServiceImpl<Users, Integer> implements UsersService{

	private UsersRepository userRepository;

	@Autowired
	public UsersServiceImpl( UsersRepository usersRepository) {
		super(usersRepository);
	}
	
	public Users getUser(String userEmail) {
		Users user = userRepository.findByUserEmail(userEmail);
		if (null!=user) {
			return user;
		} else {
			throw new UsernameNotFoundException(String.format("Username[%s] not found", userEmail));
		}
		
	}

	@Override
	public Users modify(int no, Users user) {
		Users passwordUser = getUser(user.getUserEmail());
		user.setPassword(passwordUser.getRePassword());
		user.setNo(no);
		Users modyfyUser = (Users) save(user);
		return modyfyUser;
	}

	@Override
	public Boolean getExistUser(String userId) {
		if(getUser(userId)!=null){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean sendEmail(Map map) {
		return sendEmail(map);
	}

	@Override
	public void register(UserDetail uerDetail) {
		Users users = new Users(uerDetail.getUserEmail(), uerDetail.getPassword());
		users.setPassword(Common.encryptHash("SHA-256", users.getPassword()));
		insert(users);
		
	}
 
}
