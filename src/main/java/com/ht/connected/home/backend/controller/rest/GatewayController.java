package com.ht.connected.home.backend.controller.rest;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
import com.ht.connected.home.backend.model.dto.CategoryActive;
import com.ht.connected.home.backend.model.entity.Gateway;
import com.ht.connected.home.backend.model.entity.GatewayCategory;
import com.ht.connected.home.backend.model.entity.UserGateway;
import com.ht.connected.home.backend.model.entity.User;
import com.ht.connected.home.backend.repository.GatewayRepository;
import com.ht.connected.home.backend.repository.UserGatewayRepository;
import com.ht.connected.home.backend.repository.UserRepository;
import com.ht.connected.home.backend.service.GateWayService;

/**
 * gateway(호스트)관련 요청 처리
 * @author 구정화
 */
@RestController
@RequestMapping("/gateway")
public class GatewayController extends CommonController {

    GateWayService gateWayService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    GatewayRepository gatewayRepository;

    @Autowired
    UserGatewayRepository userGatewayRepository;

    @Autowired
    ZwaveController zwaveController;
    
    @Autowired
    public GatewayController(GateWayService gateWayService) {
        this.gateWayService = gateWayService;
    }

    /**
     * 호스트 등록
     * @param registerHostRequestDto
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @PostMapping
    public ResponseEntity registerGateway(@RequestBody HashMap<String, String> req)
            throws Exception {
        ResponseEntity responseEntity;
        String authUserEmail = getAuthUserEmail();
        List<User> users = userRepository.findByUserEmail(authUserEmail);
        if (users.size() == 0) {
            responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            User user = users.get(0);
            Gateway gateway = gatewayRepository.findBySerial(req.get("serial"));
            if (isNull(gateway)) {
                responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
            } else {
                UserGateway userGateway = userGatewayRepository.findByUserNoAndGatewayNo(user.getNo(), gateway.getNo());
                if (!isNull(userGateway)) {
                    responseEntity = new ResponseEntity(HttpStatus.OK);
                } else {
                    responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
                }
            }
        }
        return responseEntity;
    }

    /**
     * 멀티 호스트 리스트.
     * @param req
     * @return
     * @throws Exception
     */

    @GetMapping
    public ResponseEntity<HashMap<String, Object>> getGatewayList() throws Exception {
        String authUserEmail = getAuthUserEmail();

        List lstGateways = gateWayService.getGatewayList(authUserEmail);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("list", lstGateways);
        return new ResponseEntity<HashMap<String, Object>>(map, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{no}")
    public ResponseEntity deleteGateway(@PathVariable("no") int no) {
        gateWayService.delete(no);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PutMapping(value = "/{no}")
    public ResponseEntity deleteIoTDevice(@RequestBody GatewayCategory gatewayCategory) throws JsonProcessingException {
        
        if(CategoryActive.gateway.zwave.name().equals(gatewayCategory.getCategory())){
            zwaveController.delete(gatewayCategory.getCategoryNo());
        }
        
        return new ResponseEntity<>(HttpStatus.OK); 
    }
}
