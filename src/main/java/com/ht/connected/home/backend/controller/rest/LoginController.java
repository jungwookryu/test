package com.ht.connected.home.backend.controller.rest;

import com.ht.connected.home.backend.service.UsersService;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController extends CommonController {

	UsersService usersService;

	@Autowired
	@Qualifier("errorMessageSource")
	MessageSource errorMessageSource;

	@Autowired
	public LoginController(UsersService usersService) {
		this.usersService = usersService;
	}

	/**
	 * 200,401,500 get Authentication accessToken get
	 */
	@GetMapping
	public ResponseEntity getAuthentication(HttpServletRequest request) {
		logger.info("login start");

		return new ResponseEntity(null, HttpStatus.FORBIDDEN);
	}

//	@PostMapping("/token")
//	public ResponseEntity getAuthenticationOriginal(HttpServletRequest request) {
//		logger.debug("token start");
//
//		return new ResponseEntity(null, HttpStatus.OK);
//	}	
	
	/**
	 * 302,401,500 get Authentication accessToken get
	 * 
	 * @return
	 * @return json User
	 * @throws AuthenticationException 
	 */
	/**
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody Users users, HttpServletRequest request) throws AuthenticationException {
		logger.info("login start");
		HashMap<String, Users> map = new HashMap<>();
		List<Users> lstUsers = usersService.getUser(users.getUserId());
		Users rtnUser = null;
		final String userPassword = Common.encryptHash("SHA-256", users.getPassword());
		// 사용자 정보를 넣어준다.
		for (Users lusers : lstUsers) {
			if (userPassword.equals(lusers.getRePassword())) {
//				JwtAccessTokenConverter authenticationToken = new JwtAccessTokenConverter();
//				Tokens.createAuthenticationToken(request, users.getUserId(), userPassword);

				HttpHeaders responseHeaders = new HttpHeaders();
				responseHeaders.set("message", "login sucess");
				rtnUser = lusers;
				responseHeaders.set("access-token", "access-token");
			}
		}
		if (rtnUser != null) {
			return new ResponseEntity(rtnUser, HttpStatus.OK);
		}

		// AuthorizationException authorizationException =
		// new
		// AuthorizationException(errorMessageSource.getMessage("failed.authentication
		// ", null , request.getLocale()));
				// new AuthorizationException("Not Acceptable");

		// new CustomLoginException(authorizationException);
		logger.info("login end");
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("message", "Not Found");
		return new ResponseEntity("Not Foun", responseHeaders, HttpStatus.UNAUTHORIZED);
//		throw new AuthenticationException("Not Found");
	}
	*/

}
