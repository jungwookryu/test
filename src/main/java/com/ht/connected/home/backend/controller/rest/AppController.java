package com.ht.connected.home.backend.controller.rest;

import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.service.UsersService;

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
@RequestMapping("/app")
public class AppController extends CommonController {

    IRController iRController; 
	@Autowired
	public AppController(IRController iRController) {
	    iRController = iRController;
	}

	@PostMapping(path="/uiIrInfo")
	public ResponseEntity uiIrInfo(@RequestBody Users users, HttpServletRequest request) throws IllegalArgumentException, UnsupportedEncodingException {
	    String sEmail = getAuthUserEmail();
	    return iRController.getIR(2);
		
	}

	/**
	@GetMapping
	public ResponseEntity<HashMap<String, List>> getUsers() {
		HashMap<String, List> map = new HashMap<>();
		List rtnUsers = usersService.getAll();
		map.put("users", rtnUsers);
		return new ResponseEntity<HashMap<String, List>>(map, HttpStatus.OK);
	}

	@GetMapping("/user/{userEmail}")
	public ResponseEntity<HashMap<String, Users>> getUser(@PathVariable("userEmail") String userEmail) throws IllegalArgumentException, UnsupportedEncodingException {
		HashMap map = new HashMap<>();
		String authUserEmail = getAuthUserEmail();
		 SecurityContextHolder.getContext().getAuthentication().getName();
		Users rtnUsers = usersService.getUser(userEmail);
		if (null!=rtnUsers) {
			map.put("users", rtnUsers);
			return new ResponseEntity(map, HttpStatus.OK);
		}
		return new ResponseEntity(map, HttpStatus.OK);
	}

	@DeleteMapping("/user/{no}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable("no") int no) {
		Users user = usersService.getOne(no);
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
	*/
}
