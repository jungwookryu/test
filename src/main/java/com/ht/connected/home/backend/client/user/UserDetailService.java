package com.ht.connected.home.backend.client.user;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ht.connected.home.backend.common.Common;

@Service
public class UserDetailService implements UserDetailsService {
	@Autowired
	private UserRepository usersRepository;

	@Autowired
	private UserService usersService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserDetailService.class);

	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		if (StringUtils.isEmpty(String.valueOf(userEmail))) {
			logger.error("username is empty {} ", userEmail);
		}
		User user = usersService.getUser(userEmail);
		if (usersService.getExistUser(userEmail)) {
			UserDetail userDetails = new UserDetail(user);
			return userDetails;
		} else {
			throw new UsernameNotFoundException(String.format("Username[%s] not found", userEmail));
		}
	}

	public User findUserDetailByUsername(String userEmail) throws UsernameNotFoundException {
		User userDetail = usersService.getUser(userEmail);
		if (null!=userDetail) {
			return userDetail;
		} else {
			throw new UsernameNotFoundException(String.format("Username[%s] not found", userEmail));
		}

	}

	public User register(UserDetail uerDetail) {
		User users = new User(uerDetail.getUserEmail(), uerDetail.getPassword());
		users.setPassword(Common.encryptHash(MessageDigestAlgorithms.SHA_256, users.getPassword()));
		return usersRepository.saveAndFlush(users);

	}

	@Transactional // To successfully remove the date @Transactional annotation must be added
	public void removeAuthenticatedUserDetail() throws UsernameNotFoundException {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User acct = findUserDetailByUsername(username);
		usersRepository.delete(acct.getNo());

	}

}
