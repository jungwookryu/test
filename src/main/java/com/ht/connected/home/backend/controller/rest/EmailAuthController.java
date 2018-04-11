package com.ht.connected.home.backend.controller.rest;

import com.ht.connected.home.backend.model.dto.UserActive;
import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.service.UsersService;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailAuthController extends CommonController {

	UsersService usersService;

	@Autowired
	public EmailAuthController(UsersService usersService) {
		this.usersService = usersService;
	}


	@GetMapping("/adduser")
	public ResponseEntity<HashMap<String, Users>> addAuthUser(@RequestParam(value = "user_email", required=true) String userEmail ,
															  @RequestParam(value = "redirected_code", required=true) String redirected_code
			) {
		Users rtnUsers = usersService.getUser(userEmail);
		if (null!=rtnUsers) {
			if(redirected_code.equals(rtnUsers.getRedirectiedCode())){
				rtnUsers.setActive(UserActive.EMAIL_AUTH.ordinal());

				usersService.modify(rtnUsers.getNo(), rtnUsers);
			}
		}
		return new ResponseEntity(HttpStatus.OK);
	}
}
