package com.ht.connected.home.backend.client.user;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.client.home.Home;
import com.ht.connected.home.backend.client.home.HomeService;
import com.ht.connected.home.backend.common.AuditLogger;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.config.service.EmailConfig;

@Service
public class UserServiceImpl implements UserService{


	@Autowired
	public EmailConfig emailConfig;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private HomeService homeService;
	
	@Override
	public User getUser(String userEmail) {
		AuditLogger.serviceLog(UserServiceImpl.class, "Get user : " + userEmail);
		List<User> user = userRepository.findByUserEmail(userEmail);
		if (user.size()>0) {
			return user.get(0);
		} else {
			throw new UsernameNotFoundException(String.format("userEmail[%s] not found", userEmail));
		}
		
	}
	
	@Override
	public User modify(int no, User user) {
		AuditLogger.serviceLog(UserServiceImpl.class, "Modify a user : " + no + ", " + user.getUserEmail());
		User passwordUser = getUser(user.getUserEmail());
		user.setLastmodifiedTime(new Date());
		user.setPassword(passwordUser.getRePassword());
		user.setNo(no);
		User modyfyUser = userRepository.saveAndFlush(user);
		return modyfyUser;
	}

	@Override
	public Boolean getExistUser(String userEmail) {
		AuditLogger.serviceLog(UserServiceImpl.class, "Check exist user : " + userEmail);
		if( userRepository.findByUserEmail(userEmail).size()>0){
			return true;
		}
		return false;
	}
	
	@Override
	public User register(User user) {
		AuditLogger.serviceLog(UserServiceImpl.class, "Register a user : " + user.getUserEmail());
		user.setPassword(Common.encryptHash(MessageDigestAlgorithms.SHA_256, user.getPassword()));
		user.setRedirectiedCode(Common.randomCode());
		user.setActive(UserActive.NOT_EMAIL_AUTH.ordinal());
		user.setAuthority(UserRole.ROLE_USER.name());
		user.setPushType(9);
		user.setCreatedTime(new Date());
		user.setUserAor(user.getUserEmail().replace("@", "^"));
		User rtnUser = userRepository.saveAndFlush(user);
		Home saveHome = new Home(rtnUser.getNo(), user.getUserEmail(), user.getUserEmail().replace("@", "^"), "MyHome", new Date());
        homeService.createHome(saveHome);
        boolean result = authSendEmail(rtnUser);
        AuditLogger.serviceLog(UserServiceImpl.class, "Send email is succeed : " + result);
		return rtnUser;
	}
 
	private boolean authSendEmail(User rtnUsers) {
		HashMap<String, Object> map = new HashMap<>();
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
