package com.ht.connected.home.backend.controller.rest;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.model.entity.Certification;
import com.ht.connected.home.backend.repository.CertificationRepository;
import com.ht.connected.home.backend.service.impl.ZwaveServiceImpl;

/**
 * Rest API Zwave 요청 처리 컨트롤러
 * 
 * @author 구정화
 *
 */
@RestController
@RequestMapping("/zwave")
public class ZwaveController {
    
    @Autowired
    private ZwaveServiceImpl zwaveService;
    
    @Autowired
    private CertificationRepository certificationRepository;

    /**
     * 모든 요청에 version 이 있다 모든 요청을 처리가능 인증프로토몰과 실서비스 프로토몰 공통 사용 (execute 인자값 확인)
     * 
     *
     * @param classKey
     * @param commandKey
     * @param version
     * @param req
     * @return
     */
    @PostMapping(value = "/{classKey}/{commandKey}/{version}")
    public Object getRequestVersion(@PathVariable("classKey") String classKey,
            @PathVariable("commandKey") String commandKey, @PathVariable("version") String version,
            @RequestBody HashMap<String, Object> req) {
        ZwaveRequest zwaveRequest = new ZwaveRequest(req, classKey, commandKey, version);
        return zwaveService.execute(req, zwaveRequest, true);
    }

    @PostMapping
    public ResponseEntity regist(@RequestBody HashMap<String, Object> req) {
        String classKey = "0x34";
        String commandKey = "0x01";
        req.put("nodeId", 0);
        req.put("endpointId", 0);
        req.put("option", 0);
        ZwaveRequest zwaveRequest = new ZwaveRequest(req, classKey, commandKey, "v1");
        zwaveService.execute(req, zwaveRequest, false);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/{serial}")
    public ResponseEntity getList(@PathVariable("serial") String serial) {
        List<Certification> certification = certificationRepository.findBySerialAndMethodAndContext(serial, "0x52", "0x02");
        certification.get(0).getPayload();
        return new ResponseEntity<>(certification.get(0).getPayload(), HttpStatus.ACCEPTED);
    }

    @PutMapping
    public ResponseEntity control(@RequestBody HashMap<String, Object> req) {
        String classKey = (String) req.get("classkey");
        String commandKey = (String) req.get("cmdkey");
        String version = (String) req.get("version");
        ZwaveRequest zwaveRequest = new ZwaveRequest(req, classKey, commandKey, version);
        return zwaveService.execute(req, zwaveRequest, false);      
    }

}
