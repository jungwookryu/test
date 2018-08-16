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

import com.ht.connected.home.backend.common.AuditLogger;
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
        AuditLogger.startLog(this.getClass(), String.format("Update home security of home No: %s by user %s",
                request.get("home_no"), getAuthUserEmail()));
        ResponseEntity<String> response = service.updateHomeSecurity(getAuthUserEmail(), request);
        AuditLogger.endLog(this.getClass(), "Update home security status successed");
        return response;
    }

    /**
     * 방범 상태값 요청 처리
     * 
     * @param homeNo
     * @return
     */
    @GetMapping("/{homeNo}")
    public ResponseEntity<String> getHomeSecurityStatus(@PathVariable("homeNo") int homeNo) {
        AuditLogger.startLog(this.getClass(),
                String.format("Request security status of home No: %s by user %s", homeNo, getAuthUserEmail()));
        ResponseEntity<String> response = service.getHomeSecurityStatus(getAuthUserEmail(), homeNo);
        AuditLogger.endLog(this.getClass(),
                String.format("Response security status of home No: %s to user %s", homeNo, getAuthUserEmail()));
        return response;
    }

}
