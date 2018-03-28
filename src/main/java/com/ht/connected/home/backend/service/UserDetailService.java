package com.ht.connected.home.backend.service;

import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.model.entity.UserDetail;
import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.repository.UsersRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailService implements UserDetailsService {
	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private UsersService usersService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserDetailService.class);

	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		if ((String.valueOf(userEmail) == "null") || ("".equals(userEmail))) {
			logger.error("username is empty {} ", userEmail);
		}
		Users user = usersService.getUser(userEmail);
		if (usersService.getExistUser(userEmail)) {
			UserDetail userDetails = new UserDetail(user);
			return userDetails;
		} else {
			throw new UsernameNotFoundException(String.format("Username[%s] not found", userEmail));
		}
	}

	public Users findUserDetailByUsername(String userEmail) throws UsernameNotFoundException {
		Users userDetail = usersService.getUser(userEmail);
		if (null!=userDetail) {
			return userDetail;
		} else {
			throw new UsernameNotFoundException(String.format("Username[%s] not found", userEmail));
		}

	}

	public Users register(UserDetail uerDetail) {
		Users users = new Users(uerDetail.getUserEmail(), uerDetail.getPassword());
		users.setPassword(Common.encryptHash("SHA-256", users.getPassword()));
		return usersRepository.saveAndFlush(users);

	}

	@Transactional // To successfully remove the date @Transactional annotation must be added
	public void removeAuthenticatedUserDetail() throws UsernameNotFoundException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Users acct = findUserDetailByUsername(username);
		usersRepository.delete(acct.getNo());

	}

}
