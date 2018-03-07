package com.ht.connected.home.backend.controller.rest;

import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.service.UsersService;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	 * 201, 204, 500, 403
	 * @param users
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	@PostMapping
	public ResponseEntity createUser(@RequestBody Users users){
		
		boolean rtnUser = usersService.getExistUser(users.getUserId());
		if(rtnUser) {
			return new ResponseEntity("exist userId", HttpStatus.FORBIDDEN);
		}
		Users createUser = new Users(users.getUserId(), users.getPassword());
		createUser.setUserMail(users.getUserMail());
		createUser.setUsername(users.getUsername());
		Users rtnUsers = usersService.insert(users);
		logger.debug(rtnUsers.toString());
		return new ResponseEntity(rtnUsers, HttpStatus.CREATED);
		
	}
	/**
	 * 201,204 
	 * @param userId
	 * @return
	 */
	@GetMapping({"userId"})
	public ResponseEntity getExistUser(@PathVariable("userId") String userId) {
		boolean rtnUser = usersService.getExistUser(userId);
		if(rtnUser) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity(HttpStatus.CREATED);
	}

	@GetMapping("/user/")
	public ResponseEntity<HashMap<String, List>> getUsers() {
		HashMap<String, List> map = new HashMap<>();
		List rtnUsers = usersService.getAll();
		map.put("users", rtnUsers);
		return new ResponseEntity<HashMap<String, List>>(map, HttpStatus.OK);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<HashMap<String, List<Users>>> getUser(@PathVariable("userId") String userId) {
		HashMap<String, List<Users>> map = new HashMap<>();
		List<Users> rtnUsers = usersService.getUser(userId);
		map.put("users", rtnUsers);
		return new ResponseEntity<HashMap<String, List<Users>>>(map, HttpStatus.OK);
	}

	@DeleteMapping("/user/{no}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable("no") int no) {
		usersService.delete(no);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}

	@PutMapping("/user/{no}")
	public ResponseEntity<HashMap<String, Users>> modifyUser(@PathVariable("no") int no, @RequestBody Users users) {
		HashMap<String, Users> map = new HashMap<>();
		Users rtnUsers = usersService.modify(no, users);
		map.put("users", rtnUsers);
		return new ResponseEntity<HashMap<String, Users>>(map, HttpStatus.OK);
	}
}
