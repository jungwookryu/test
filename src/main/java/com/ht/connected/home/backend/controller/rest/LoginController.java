package com.ht.connected.home.backend.controller.rest;

import com.ht.connected.home.backend.service.UsersService;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController extends CommonController {

	UsersService usersService;

	@Autowired
	@Qualifier("errorMessageSource")
	MessageSource errorMessageSource;

	@Autowired
	private TokenEndpoint tokenEndpoint;

	@Autowired
	public LoginController(UsersService usersService) {
		this.usersService = usersService;
	}

	@GetMapping(value = "/authentication/login")
	public ResponseEntity<OAuth2AccessToken> getAccessToken(Principal principal,
			@RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
		return tokenEndpoint.getAccessToken(principal, parameters);
	}
 
	@PostMapping(value = "/authentication/login")
	public ResponseEntity<OAuth2AccessToken> postAccessToken(Principal principal,
			@RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
		parameters.put("grant_type", parameters.getOrDefault("grant_type", "password"));
		String userEmail = parameters.getOrDefault("user_email", "");
		String userName = parameters.getOrDefault("username", "");
		if ("".equals(userEmail) && "".equals(userName)) {
			throw new BadClientCredentialsException();
		}
		if (("".equals(userEmail)) &&(!"".equals(userName))) {
			parameters.put("user_email", userName);
		}
		if ((!"".equals(userEmail)) &&("".equals(userName))) {
			parameters.put("username",userEmail);
		}
		return tokenEndpoint.postAccessToken(principal, parameters);
	}

}
