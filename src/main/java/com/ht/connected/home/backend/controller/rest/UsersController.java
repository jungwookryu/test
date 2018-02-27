package com.ht.connected.home.backend.controller.rest;

import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.service.UsersService;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
@RequestMapping("/users")
public class UsersController extends CommonController {

	UsersService userService;

	@PostMapping
	public ResponseEntity<HashMap<String, Users>> createUser(@RequestBody Users users) {
		HashMap<String, Users> map = new HashMap<>();
		Users rtnUsers = userService.insert(users);
		map.put("users", rtnUsers);
		return new ResponseEntity<HashMap<String, Users>>(map, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<HashMap<String, List>> getUser() {
		HashMap<String, List> map = new HashMap<>();
		List rtnUsers = userService.getAll();
		map.put("users", rtnUsers);
		return new ResponseEntity<HashMap<String, List>>(map, HttpStatus.OK);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<HashMap<String, List<Users>>> getUser(@PathVariable("userId") String userId) {
		HashMap<String, List<Users>> map = new HashMap<>();
		List<Users> rtnUsers = userService.getUser(userId);
		map.put("users", rtnUsers);
		return new ResponseEntity<HashMap<String, List<Users>>>(map, HttpStatus.OK);
	}

	@DeleteMapping("/{no}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable("no") int no) {
		userService.delete(no);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<HashMap<String, Users>> modifyUser(@PathVariable("no") int no, @RequestBody Users users) {
		HashMap<String, Users> map = new HashMap<>();
		Users rtnUsers = userService.insert(users);
		map.put("users", rtnUsers);
		return new ResponseEntity<HashMap<String, Users>>(map, HttpStatus.OK);
	}
}
