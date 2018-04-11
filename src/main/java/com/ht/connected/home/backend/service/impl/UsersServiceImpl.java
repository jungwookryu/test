package com.ht.connected.home.backend.service.impl;

import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.config.service.EmailConfig;
import com.ht.connected.home.backend.model.dto.UserRole;
import com.ht.connected.home.backend.model.dto.UserActive;
import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.repository.UsersRepository;
import com.ht.connected.home.backend.service.UsersService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

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
			throw new UsernameNotFoundException(String.format("userEmail[%s] not found", userEmail));
		}
		
	}
	
	@Override
	public Users modify(int no, Users user) {
		Users passwordUser = getUser(user.getUserEmail());
		user.setLastmodifiedTime(new Date());
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
		users.setActive(UserActive.NOT_EMAIL_AUTH.ordinal());
		users.setAuthority(UserRole.ROLE_USER.name());
		users.setPushType(9);
		users.setCreatedTime(new Date());
		Users rtnUsers = insert(users);
		authSendEmail(rtnUsers);
		return rtnUsers;

	}
 
	public boolean authSendEmail(Users rtnUsers) {
		HashMap map = new HashMap<>();
		Properties properties = emailConfig.properties();
		if (null!=rtnUsers) {
			map.put("rtnUsers", rtnUsers);
			map.put("authUrl", "");
			String sFile = properties.get("mail.smtp.sFile").toString();
			InputStream filePath =getClass().getClassLoader().getResourceAsStream(sFile);
			return Common.sendEmail(map, emailConfig,filePath );
		}
		return false;
	}
}
