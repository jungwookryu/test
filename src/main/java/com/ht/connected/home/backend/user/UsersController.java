package com.ht.connected.home.backend.user;

import com.ht.connected.home.backend.controller.rest.CommonController;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController extends CommonController {

	UsersService usersService;

	@Autowired
	public UsersController(UsersService usersService) {
		this.usersService = usersService;
	}

	/**
	 * 201, 204, 500, 406
	 * 
	 * @param users
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity createUser(@RequestBody User users, HttpServletRequest request) throws IllegalArgumentException, UnsupportedEncodingException {

		boolean rtnUser = usersService.getExistUser(users.getUserEmail());
		if (rtnUser) {
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("message", "exist useremail");
			return new ResponseEntity("exist userEmail", responseHeaders, HttpStatus.NOT_ACCEPTABLE);
		}else {
			User rtnUsers = usersService.register(users);
	
			logger.debug(rtnUsers.toString());
			return new ResponseEntity(HttpStatus.CREATED);
		}

	}

	@GetMapping
	public ResponseEntity<HashMap<String, List>> getUsers() {
		HashMap<String, List> map = new HashMap<>();
		List rtnUsers = usersService.getAll();
		map.put("users", rtnUsers);
		return new ResponseEntity<HashMap<String, List>>(map, HttpStatus.OK);
	}

	@GetMapping("/user/{userEmail}")
	public ResponseEntity<HashMap<String, User>> getUser(@PathVariable("userEmail") String userEmail) throws IllegalArgumentException, UnsupportedEncodingException {
		HashMap map = new HashMap<>();
		String authUserEmail = getAuthUserEmail();
		 SecurityContextHolder.getContext().getAuthentication().getName();
		User rtnUsers = usersService.getUser(userEmail);
		if (null!=rtnUsers) {
			map.put("users", rtnUsers);
			return new ResponseEntity(map, HttpStatus.OK);
		}
		return new ResponseEntity(map, HttpStatus.OK);
	}

	@DeleteMapping("/user/{no}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable("no") int no) {
		User user = usersService.getOne(no);
		usersService.delete(no);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}

	@PutMapping("/user/{no}")
	public ResponseEntity<HashMap<String, User>> modifyUser(@PathVariable("no") int no, @RequestBody User users) {
		HashMap<String, User> map = new HashMap<>();
		User rtnUsers = usersService.modify(no, users);
		map.put("users", rtnUsers);
		return new ResponseEntity<HashMap<String, User>>(map, HttpStatus.OK);
	}
}
