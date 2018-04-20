package com.ht.connected.home.backend.controller.rest;

import java.util.HashMap;
import java.util.List;
import static java.util.Objects.isNull;

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

import com.ht.connected.home.backend.config.service.ZwaveClassKey;
import com.ht.connected.home.backend.config.service.ZwaveCommandKey;
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
    public ResponseEntity getRequestVersion(@PathVariable("classKey") String classKey,
            @PathVariable("commandKey") String commandKey, @PathVariable("version") String version,
            @RequestBody HashMap<String, Object> req) {
        ZwaveRequest zwaveRequest = new ZwaveRequest(req, Integer.parseInt(classKey),  Integer.parseInt(commandKey), version);
        return zwaveService.execute(req, zwaveRequest, true);
    }

    @PostMapping
    public ResponseEntity regist(@RequestBody HashMap<String, Object> req) {
        int classKey = ZwaveClassKey.NETWORK_MANAGEMENT_INCLUSION;
        int commandKey = ZwaveCommandKey.NODE_ADD;
        req.put("nodeId", 0);
        req.put("endpointId", 0);
        req.put("option", 0);
        ZwaveRequest zwaveRequest = new ZwaveRequest(req, classKey, commandKey, "v1");
        zwaveService.execute(req, zwaveRequest, false);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/{serial}")
    public ResponseEntity getList(@PathVariable("serial") String serial) {
        ResponseEntity response = new ResponseEntity(HttpStatus.NOT_FOUND);
        List<Certification> certification = certificationRepository.findBySerialAndMethodAndContext(serial,
                Integer.toString(ZwaveClassKey.NETWORK_MANAGEMENT_PROXY), Integer.toString(ZwaveCommandKey.NODE_LIST_REPORT));
        if(!isNull(certification)) {
            certification.get(0).getPayload();    
            response = new ResponseEntity<>(certification.get(0).getPayload(), HttpStatus.ACCEPTED);
        }
        return response;
    }

    @PutMapping
    public ResponseEntity control(@RequestBody HashMap<String, Object> req) {
        int classKey = Integer.parseInt((String)req.get("cmdkey"));
        int commandKey = Integer.parseInt((String)req.get("cmdkey"));
        String version = (String) req.get("version");
        ZwaveRequest zwaveRequest = new ZwaveRequest(req, classKey, commandKey, version);
        return zwaveService.execute(req, zwaveRequest, false);
    }

}
