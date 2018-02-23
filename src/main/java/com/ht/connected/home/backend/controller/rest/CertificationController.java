package com.ht.connected.home.backend.controller.rest;

import com.ht.connected.home.backend.model.entity.Certification;
import com.ht.connected.home.backend.service.CertificationService;

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
@RequestMapping("/certification")
public class CertificationController extends CommonController {

	CertificationService Certificationervice;
	
	@PostMapping
	public ResponseEntity<HashMap<String, Certification>> createUser(@RequestBody Certification Certification) {
		HashMap<String, Certification> map = new HashMap<>();
		Certification rtnCertification = Certificationervice.insert(Certification);
		map.put("Certification",rtnCertification);
		return new ResponseEntity<HashMap<String, Certification>>(map, HttpStatus.OK); 
	}
	
	@GetMapping
	public ResponseEntity<HashMap<String, List<Certification>>> getUser(@RequestBody Certification Certification) {
		HashMap<String, List<Certification>> map = new HashMap<>();
		List<Certification> rtnCertification = Certificationervice.getAll();
		map.put("Certification",rtnCertification);
		return new ResponseEntity<HashMap<String, List<Certification>>>(map, HttpStatus.OK); 
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<HashMap<String, List<Certification>>> getUser(@PathVariable("userId") String userId) {
		HashMap<String, List<Certification>> map = new HashMap<>();
//		List<Certification> rtnCertification = Certificationervice.getUser(userId);
//		map.put("Certification",rtnCertification);
		return new ResponseEntity<HashMap<String, List<Certification>>>(map, HttpStatus.OK); 
	}
	
	@DeleteMapping("/{no}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable("no") int no) {
		Certificationervice.delete(no);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK); 
	}
	
	@PutMapping
	public ResponseEntity<HashMap<String, Certification>> modifyUser(@PathVariable("no") int no,@RequestBody Certification Certification) {
		HashMap<String, Certification> map = new HashMap<>();
		Certification rtnCertification = Certificationervice.insert(Certification);
		map.put("Certification",rtnCertification);
		return new ResponseEntity<HashMap<String, Certification>>(map, HttpStatus.OK); 
	}
}
