package com.ht.connected.home.backend.controller.rest;

import com.ht.connected.home.backend.model.entity.Users;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {
	
	@GetMapping
	public ResponseEntity logout(@RequestBody Users users, HttpServletRequest request) {
		request.getSession().invalidate();
		return new ResponseEntity(HttpStatus.OK);
	}


}
