package com.ht.connected.home.backend.pushwise.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ht.connected.home.backend.controller.rest.CommonController;
import com.ht.connected.home.backend.pushwise.service.PWService;


/**
 * 방범 제어 RESTful 요청 처리
 * 
 * @author 구정화
 *
 */
@RestController
@RequestMapping("/security")
public class PWController extends CommonController {

    @Autowired
    private PWService service;

    /**
     * 방범 상태 업데이트 요청 처리
     * 
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<String> updateHomeSecruity(@RequestBody HashMap<String, Integer> request) {
        return service.updateHomeSecurity(getAuthUserEmail(), request);
    }

    /**
     * 방범 상태값 요청 처리
     * 
     * @param homeNo
     * @return
     */
    @GetMapping("/{homeNo}")
    public ResponseEntity<String> getHomeSecurityStatus(@PathVariable("homeNo") int homeNo) {
        return service.getHomeSecurityStatus(getAuthUserEmail(), homeNo);
    }

}
