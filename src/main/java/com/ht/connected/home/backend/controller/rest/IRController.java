package com.ht.connected.home.backend.controller.rest;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.ht.connected.home.backend.model.entity.IR;
import com.ht.connected.home.backend.service.IRService;

@RestController
@RequestMapping("/ir")
public class IRController extends CommonController {

	IRService iRService;

	@Autowired
	public IRController(IRService iRService) {
		this.iRService = iRService;
	}

	/**
	 * 201, 204, 500, 406
	 * 
	 * @param IR
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchAlgorithmException
	 */
	//신규 학습
	@PostMapping
	public ResponseEntity createIR(@RequestBody IR IR, HttpServletRequest request) throws IllegalArgumentException, UnsupportedEncodingException {
		return null;
	}

	//기기리스트
	@GetMapping
	public ResponseEntity<HashMap<String, List>> getIR() {
	
	    Map map = new HashMap();
		return new ResponseEntity(map, HttpStatus.OK);
	}

	//기기정보 가져오기
	@GetMapping("/{no}")
	public ResponseEntity getIR(@PathVariable("no") int no) throws IllegalArgumentException, UnsupportedEncodingException {
	    Map map = new HashMap();
		return new ResponseEntity(map, HttpStatus.OK);
	}

	//IR 기기 학습 삭제
	@DeleteMapping("/ir/{no}")
	public ResponseEntity<HttpStatus> deleteIR(@PathVariable("no") int no) {
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}

	//기기 재학습
	@PutMapping("/ir/{no}")
	public ResponseEntity modifyIR(@PathVariable("no") int no, @RequestBody IR IR) {
	    Map map = new HashMap();
		return new ResponseEntity(map, HttpStatus.OK);
	}
	
	//학습모드요청
   @PostMapping("/ir/")
    public ResponseEntity reModifyIR(@PathVariable("no") int no, @RequestBody IR IR) {
       Map map = new HashMap();
        return new ResponseEntity(map, HttpStatus.OK);
    }
}
