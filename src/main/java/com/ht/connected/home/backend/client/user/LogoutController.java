package com.ht.connected.home.backend.client.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {
	
	@GetMapping
	public ResponseEntity logout( HttpServletRequest request) {
		request.getSession().invalidate(); 
		return new ResponseEntity(HttpStatus.OK);
	}


}
