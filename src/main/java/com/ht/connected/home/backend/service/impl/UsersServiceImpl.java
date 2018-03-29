package com.ht.connected.home.backend.service.impl;

import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.config.service.EmailConfig;
import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.repository.UsersRepository;
import com.ht.connected.home.backend.service.UsersService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl extends CrudServiceImpl<Users, Integer> implements UsersService{


	@Autowired
	public EmailConfig emailConfig;
	
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
	public Users register(Users users) {
		users.setPassword(Common.encryptHash("SHA-256", users.getPassword()));
		users.setRedirectiedCode(randomCode());
		users.setActive(0);
		Users rtnUsers = insert(users);
		authSendEmail(rtnUsers);
//		authSendEmail(users);
		return rtnUsers;
//		return users;
	}
 
	public boolean authSendEmail(Users rtnUsers) {
		HashMap map = new HashMap<>();
		if (null!=rtnUsers) {
			map.put("rtnUsers", rtnUsers);
			map.put("authUrl", "");
			return Common.sendEmail(map, emailConfig);
		}
		return false;
	}
}
