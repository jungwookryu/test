package com.ht.connected.home.backend.category.zwave;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.NetworkManagementInclusionCommandClass;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.NetworkManagementProxyCommandClass;
import com.ht.connected.home.backend.common.ByteUtil;
import com.ht.connected.home.backend.controller.rest.CommonController;
import com.ht.connected.home.backend.user.User;
import com.ht.connected.home.backend.user.UserGatewayRepository;
import com.ht.connected.home.backend.user.UserRepository;
import com.ht.connected.home.backend.userGateway.UserGateway;

/**
 * Rest API Zwave 요청 처리 컨트롤러
 * @author 구정화
 */
@RestController
@RequestMapping("/zwave")
public class ZwaveController extends CommonController {

    @Autowired
    ZwaveServiceImpl zwaveService;

    @Autowired
    UserGatewayRepository userGatewayRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CertificationRepository certificationRepository;

    /**
     * 모든 요청에 version 이 있다 모든 요청을 처리가능 인증프로토몰과 실서비스 프로토몰 공통 사용 (execute 인자값 확인)
     * @param classKey
     * @param commandKey
     * @param version
     * @param req
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping(value = "/{classKey}/{commandKey}/{version}")
    public ResponseEntity getRequestVersion(@PathVariable("classKey") String classKey,
            @PathVariable("commandKey") String commandKey, @PathVariable("version") String version,
            @RequestBody HashMap<String, Object> req) throws JsonProcessingException {
        logger.info("commandKey:" + commandKey + " :::classKey:" + classKey + "version:::" + version);

        ZwaveRequest zwaveRequest = new ZwaveRequest(req, Integer.parseInt(classKey), Integer.parseInt(commandKey), version);
        return zwaveService.execute(req, zwaveRequest, true);
    }

    /**
     * 기기등록
     * @param req
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping
    public ResponseEntity regist(@RequestBody HashMap<String, Object> req) throws JsonProcessingException {
        String userEmail = getAuthUserEmail();
        int classKey = NetworkManagementInclusionCommandClass.INT_ID;
        int commandKey = (int) req.getOrDefault("mode", 1);
        ZwaveRequest zwaveRequest = new ZwaveRequest(req, classKey, commandKey, "v1");
        zwaveService.execute(req, zwaveRequest, false);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/{serial}")
    public ResponseEntity getList(@PathVariable("serial") String serial) throws JsonProcessingException {
        HashMap map = new HashMap();
        map.put("nodelist", new ArrayList());
        String sRtnList = objectMapper.writeValueAsString(map);
        List<Certification> certification = certificationRepository.findBySerialAndMethodAndContext(serial,
                ByteUtil.getHexString(NetworkManagementProxyCommandClass.INT_ID), ByteUtil.getHexString(NetworkManagementProxyCommandClass.INT_NODE_LIST_REPORT));
        if (certification.size() > 0) {
            sRtnList = certification.get(0).getPayload();
        }
        return new ResponseEntity<>(sRtnList, HttpStatus.ACCEPTED);
    }

    @PutMapping
    @ResponseBody
    // public ResponseEntity control(@RequestBody ZwaveRequest zwaveRequest) throws JsonProcessingException {
    public ResponseEntity control(@RequestBody HashMap map) throws JsonProcessingException {
        zwaveService.execute(map, false);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * 
     * @param no
     * @return
     * @throws JsonProcessingException
     */
    @DeleteMapping(value = "/{no}")
    public ResponseEntity delete(@PathVariable int no) throws JsonProcessingException{
        String userEmail = getAuthUserEmail();
        List<User> lstUser = userRepository.findByUserEmail(userEmail);
        if(lstUser.size()>0) {
            List<UserGateway> lstUserGateway = userGatewayRepository.findByUserNo(lstUser.get(0).getNo());
            if (lstUserGateway.size() <= 0) {
                return new ResponseEntity<>(lstUserGateway.size(), HttpStatus.NOT_FOUND);
            }else {
               int iZwave = zwaveService.getByUserEmailAndNo(userEmail, no);
               if(iZwave > 0) {
                   int deleteInt = zwaveService.deleteByNo(no);
                   return new ResponseEntity<>(deleteInt, HttpStatus.ACCEPTED);
               }else {
                   return new ResponseEntity<>(0, HttpStatus.NO_CONTENT);
               }
            }
        }
        return new ResponseEntity<>(0, HttpStatus.NO_CONTENT);
    }
}