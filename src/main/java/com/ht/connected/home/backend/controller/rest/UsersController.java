package com.ht.connected.home.backend.controller.rest;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gargoylesoftware.htmlunit.javascript.host.Map;
import com.ht.connected.home.backend.model.entity.Users;

@ControllerAdvice
@RestController
@RequestMapping("/users")
public class UsersController extends CommonController {

	@PostMapping
	public ResponseEntity createUser(@RequestBody Users users) {
		HashMap map = new HashMap<>();
		
		return new ResponseEntity(map, HttpStatus.OK); 
	}
}
