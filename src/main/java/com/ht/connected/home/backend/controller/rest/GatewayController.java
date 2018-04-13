package com.ht.connected.home.backend.controller.rest;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ht.connected.home.backend.model.entity.Gateway;
import com.ht.connected.home.backend.model.entity.UserGateway;
import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.repository.GateWayRepository;
import com.ht.connected.home.backend.repository.UserGatewayRepository;
import com.ht.connected.home.backend.repository.UsersRepository;


/**
 * gateway(호스트)관련 요청 처리
 * @author 구정화
 *
 */
@RestController
@RequestMapping("/gateway")
public class GatewayController extends CommonController{

	@Autowired
	UsersRepository userRepository;

	@Autowired
	GateWayRepository gatewayRepository;

	@Autowired
	UserGatewayRepository userGatewayRepository;
	
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
		List<Users> users = userRepository.findByUserEmail(authUserEmail);
		if (users.size() == 0) {
			responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
		} else {
			Users user = users.get(0);
			Gateway gateway = gatewayRepository.findBySerial(req.get("serialNo"));
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
	 *
	 * @param req
	 * @return
	 * @throws Exception
	 */
	
	@GetMapping
	public ResponseEntity<HashMap<String, Object>> getGatewayList() throws Exception {
		String authUserEmail = getAuthUserEmail();

		List<Gateway> lstGateways = new ArrayList<>();
		List<Users> users = userRepository.findByUserEmail(authUserEmail);
		Users user = users.get(0);
		List<UserGateway> userGateways = userGatewayRepository.findByUserNo(user.getNo());

		List<Integer> gatewayNos = new ArrayList<Integer>();
		userGateways.stream().forEach(userGateway -> gatewayNos.add(userGateway.getGatewayNo()));
		List<Gateway> gateways = gatewayRepository.findAll(gatewayNos);
		List<UserGateway> userGatewayList = userGatewayRepository.findByGatewayNoIn(gatewayNos);

		gateways.forEach(gateway -> {
			Gateway aGateway = new Gateway();
			aGateway.setSerial(gateway.getSerial());
			aGateway.setModel(gateway.getModel());
			aGateway.setNickname(gateway.getNickname());
			Users master = getMasterUserNicknameByGatewayNo(userGatewayList, gateway.getNo());
			aGateway.setUserNickname(master.getNickName());
			aGateway.setUserEmail(master.getUserEmail());
			lstGateways.add(aGateway);
		});
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("list", lstGateways);
		return new ResponseEntity<HashMap<String, Object>>(map, HttpStatus.OK);
	}

	private Users getMasterUserNicknameByGatewayNo(List<UserGateway> userGatewayList, Integer gatewayNo) {		
		UserGateway userGateway = userGatewayList.stream()
				.filter(ug -> ug.getGroupRole().equals("master") && gatewayNo.equals(ug.getGatewayNo()))
				.collect(Collectors.toList()).get(0);
		Users user = userRepository.findOne(userGateway.getUserNo());
		return user;
	}

}
