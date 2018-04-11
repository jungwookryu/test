package com.ht.connected.home.backend.controller.rest;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ht.connected.home.backend.service.GateWayService;


/**
 * gateway(호스트)관련 요청 처리
 * @author 구정화
 *
 */
@RestController
@RequestMapping("/gateway")
public class GatewayController extends CommonController{

    GateWayService gateWayService;

    @Autowired
    public GatewayController(GateWayService gateWayService) {
        this.gateWayService = gateWayService;
    }
    /**
	 * 멀티 호스트 리스트.
	 *
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

}
