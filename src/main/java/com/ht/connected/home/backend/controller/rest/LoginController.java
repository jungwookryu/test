package com.ht.connected.home.backend.controller.rest;

import com.ht.connected.home.backend.exception.CustomLoginException;
import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.service.UsersService;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class LoginController extends CommonController{

	UsersService usersService;

	@Autowired
	@Qualifier("errorMessageSource")
	MessageSource errorMessageSource;
	
	@Autowired
	public LoginController(UsersService usersService) {
		this.usersService = usersService;
	}
	
	
	@SuppressWarnings("unused")
	@PostMapping
	public ResponseEntity login(@RequestBody Users users, HttpServletRequest request) {
		logger.info("login start");
		HashMap<String, Users> map = new HashMap<>();
		List<Users> lstUsers = usersService.getUser(users.getUserId());
		final Users rtnUser;
		//사용자 정보를 넣어준다. 
		lstUsers.forEach(lusers -> {
			if(users.getPassword().equals(lusers.getPassword())){
				new ResponseEntity(lstUsers, HttpStatus.FORBIDDEN);
			}
		});
		
		AuthorizationException authorizationException = 
				new AuthorizationException(errorMessageSource.getMessage("failed.authentication ", null , request.getLocale()));
		new CustomLoginException().customLoginException(authorizationException);
		logger.info("login end");
		return new ResponseEntity(null, HttpStatus.FORBIDDEN);
	}

}
