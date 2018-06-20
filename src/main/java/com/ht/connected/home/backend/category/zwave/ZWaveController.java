package com.ht.connected.home.backend.category.zwave;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ht.connected.home.backend.category.zwave.certification.CertificationRepository;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.NetworkManagementInclusionCommandClass;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.controller.rest.CommonController;
import com.ht.connected.home.backend.gateway.Gateway;
import com.ht.connected.home.backend.gateway.GatewayRepository;
import com.ht.connected.home.backend.user.User;
import com.ht.connected.home.backend.user.UserRepository;
import com.ht.connected.home.backend.userGateway.UserGateway;
import com.ht.connected.home.backend.userGateway.UserGatewayRepository;

/**
 * Rest API Zwave 요청 처리 컨트롤러
 * @author 구정화
 */
@RestController
@RequestMapping("/zwave")
public class ZWaveController extends CommonController {

    @Autowired
    ZWaveServiceImpl zwaveService;

    @Autowired
    UserGatewayRepository userGatewayRepository;
    @Autowired
    GatewayRepository gatewayRepository;
    
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

        ZWaveRequest zwaveRequest = new ZWaveRequest(req, Integer.parseInt(classKey), Integer.parseInt(commandKey), version);
        return zwaveService.execute(req, zwaveRequest, true);
    }

    /**
     * 기기등록 취소
     * @param req
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping
    public ResponseEntity regist(@RequestBody HashMap<String, Object> req) throws JsonProcessingException {
        String userEmail = getAuthUserEmail();
        HashMap map = new HashMap<>();
        int classKey = NetworkManagementInclusionCommandClass.INT_ID;
        //mode 1 == add, 5==stop
        int mode = (int) req.getOrDefault("mode", 1);
        logger.debug("mode:::"+mode);
        int commandKey = NetworkManagementInclusionCommandClass.INT_NODE_ADD;
        String serial = (String) req.getOrDefault("serial", "");
        Gateway gateway = gatewayRepository.findBySerial(serial);
        HashMap requestMap = new HashMap<>();
        if (mode == 1) {
            map.put("mode", 1);
        }
        if (mode == 2) {
            map.put("mode", 2);
        }
        requestMap.put("set_data", map);
        ZWaveRequest zwaveRequest = new ZWaveRequest(req, classKey, commandKey, "v1");
        zwaveRequest.setModel(gateway.getModel());
        zwaveService.publish(requestMap, zwaveRequest);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/{gateway_no}")
    public ResponseEntity getList(@PathVariable("gateway_no") int gatewayNo) {
        
        Map sRtnList = (Map) zwaveService.getZWaveListApp(gatewayNo);
        return new ResponseEntity<>(sRtnList, HttpStatus.ACCEPTED);
    }


    @PutMapping("/{zwave_no}")
    public ResponseEntity control(@PathVariable int zwave_no, @RequestBody ZWaveControl zWaveControl) throws JsonProcessingException {
        zWaveControl.setZwave_no(zwave_no);
        zwaveService.zwaveControl(zWaveControl);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
    
    /**
     * 
     * @param no
     * @return
     * @throws JsonProcessingException
     */
    @DeleteMapping(value = "/remove/{no}")
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
