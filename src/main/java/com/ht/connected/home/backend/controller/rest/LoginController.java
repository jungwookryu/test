package com.ht.connected.home.backend.controller.rest;

import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.service.UsersService;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	UsersService usersService;
	
	@PostMapping
	public ResponseEntity<HashMap<String, Users>> createUser(@RequestBody Users users, HttpServletRequest request) {
		HashMap<String, Users> map = new HashMap<>();
		Users rtnUsers = usersService.insert(users);
		map.put("users", rtnUsers);
		return new ResponseEntity<HashMap<String, Users>>(map, HttpStatus.OK);
	}


}
