package com.ht.connected.home.backend.service.impl;

import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.model.entity.UserDetail;
import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.repository.UsersRepository;
import com.ht.connected.home.backend.service.UsersService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl extends CrudServiceImpl<Users, Integer> implements UsersService{

	public UsersServiceImpl(JpaRepository<Users, Integer> jpaRepository) {
		super(jpaRepository);
		// TODO Auto-generated constructor stub
	}

	Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);
	@Autowired
	private UsersRepository userRepository;
	
	
	@Override
	public Users getUser(String userEmail) {
		List<Users> user = userRepository.findByUserEmail(userEmail);
		if (user.size()>0) {
			return user.get(0);
		} else {
			throw new UsernameNotFoundException(String.format("username[%s] not found", userEmail));
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
	public Boolean getExistUser(String userEmail) {
		if( userRepository.findByUserEmail(userEmail).size()>0){
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
