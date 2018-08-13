package com.ht.connected.home.backend.device.category.zwave;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
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
import com.ht.connected.home.backend.client.user.User;
import com.ht.connected.home.backend.client.user.UserRepository;
import com.ht.connected.home.backend.common.AuditLogger;
import com.ht.connected.home.backend.controller.rest.CommonController;
import com.ht.connected.home.backend.device.category.gateway.Gateway;
import com.ht.connected.home.backend.device.category.gateway.GatewayRepository;
import com.ht.connected.home.backend.device.category.gateway.GatewayService;
import com.ht.connected.home.backend.device.category.zwave.certi.ZWaveCertiNetworkManagementBasicService;
import com.ht.connected.home.backend.device.category.zwave.certi.commandclass.NetworkManagementInclusionCommandClass;
import com.ht.connected.home.backend.device.category.zwave.endpoint.Endpoint;
import com.ht.connected.home.backend.device.category.zwave.endpoint.EndpointRepository;
import com.ht.connected.home.backend.device.category.zwave.endpoint.EndpointService;

@RestController
@RequestMapping("/zwave")
public class ZWaveController extends CommonController {

	@Autowired
	ZWaveService zwaveService;

	@Autowired
	ZWaveCommonService zWaveCommonService;

	@Autowired
	EndpointService endpointService;

	@Autowired
	GatewayRepository gatewayRepository;
	
	@Autowired
	GatewayService gatewayService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ZWaveRepository zWaveRepository;

	@Autowired
	EndpointRepository endpointRepository;

	@Autowired
	ZWaveCertiNetworkManagementBasicService zWaveCertiNetworkManagementBasicService;

	/**
	 * 기기등록 취소
	 * 
	 * @param req
	 * @return
	 * @throws JsonProcessingException
	 * @throws InterruptedException
	 */
	@PostMapping
	public ResponseEntity regist(@RequestBody HashMap<String, Object> req)
			throws JsonProcessingException, InterruptedException {
		String userEmail = getAuthUserEmail();
		HashMap map = new HashMap<>();
		int classKey = NetworkManagementInclusionCommandClass.INT_ID;
		// mode 1 == add, 5==stop
		int mode = (int) req.getOrDefault("mode", -1);
		String s2pin = (String) req.get("s2pin");
		int commandKey = NetworkManagementInclusionCommandClass.INT_NODE_ADD;
		String serial = (String) req.getOrDefault("serial", "");
		AuditLogger.startLog(ZWaveController.class,
				"Register ZWave [mode : " + mode + ", s2pin : " + s2pin + ", serial : " + serial + "]");
		Gateway gateway = gatewayRepository.findBySerial(serial);
		if (gateway == null) {
			AuditLogger.endLog(ZWaveController.class, "Register ZWave : failed. not found gateway (" + serial + ")");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		HashMap requestMap = new HashMap<>();
		if (mode != -1) {
			map.put("mode", mode);
		}
		if (!Objects.isNull(s2pin)) {
			map.put("s2pin", s2pin);
		}
		requestMap.put("set_data", map);
		ZWaveRequest zwaveRequest = new ZWaveRequest(req, classKey, commandKey, "v1");
		zwaveRequest.setTarget(gateway.getTargetType());
		zwaveRequest.setModel(gateway.getModel());
		zwaveService.publish(requestMap, zwaveRequest);
		AuditLogger.endLog(ZWaveController.class, "Register ZWave : succeed");
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@GetMapping(value = "/{gateway_no}")
	public ResponseEntity getList(@PathVariable("gateway_no") int gatewayNo) {
		AuditLogger.startLog(ZWaveController.class, "Get zwave device list : " + gatewayNo);
		Map sRtnList = zWaveCommonService.getZWaveListApp(gatewayNo);
		AuditLogger.endLog(ZWaveController.class, "Get zwave device list : Succeed");
		return new ResponseEntity<>(sRtnList, HttpStatus.ACCEPTED);
	}

	@PutMapping("/zwaveInfo/{zwaveNo}")
	public ResponseEntity modifyEndpointInfo(@PathVariable int zwaveNo, @RequestBody ZWave requestZwave)
			throws JsonProcessingException {
		String deviceNickname = requestZwave.getNickname();
		AuditLogger.startLog(ZWaveController.class, "Modify zwave device info : " + zwaveNo + ", " + deviceNickname);
		Endpoint endpoint = endpointRepository.findByZwaveNoAndEpid(zwaveNo, 0);
		if (endpoint != null) {
			endpoint.setNickname(deviceNickname);
			ZWave rtnZwave = endpointService.modify(endpoint.getNo(), endpoint);
			AuditLogger.endLog(ZWaveController.class, "Modify zwave device info : Succeed");
			return new ResponseEntity<>(rtnZwave, HttpStatus.OK);
		} else {
			AuditLogger.endLog(ZWaveController.class, "Modify zwave device info : failed (not found zwave device)");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * @param no
	 * @return
	 * @throws JsonProcessingException
	 * @throws InterruptedException
	 */
	@DeleteMapping(value = "/remove/{no}")
	public ResponseEntity delete(@PathVariable int no) throws JsonProcessingException, InterruptedException {
		AuditLogger.startLog(ZWaveController.class, "Delete a zwave device : " + no);
		String userEmail = getAuthUserEmail();
		List<User> lstUser = userRepository.findByUserEmail(userEmail);
		if (lstUser.size() == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		
		ZWave zwave = zWaveRepository.findOne(no);
		if (Objects.isNull(zwave)) {
			AuditLogger.endLog(ZWaveController.class, "Delete a zwave device : failed (Not found zwave device)");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		zWaveCommonService.deleteByNo(zwave);
		AuditLogger.endLog(ZWaveController.class, "Delete a zwave device : Server request to Gateway");
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@PutMapping("/{zwave_no}")
	public ResponseEntity basicControl(@PathVariable int zwave_no, @RequestBody ZWaveControl zWaveControl)
			throws JsonProcessingException, InterruptedException {
		AuditLogger.startLog(ZWaveController.class, "Control a zwave device (Basic): " + zwave_no);
		zWaveControl.setZwave_no(zwave_no);
		zWaveCommonService.zwaveBasicControl(zWaveControl);
		AuditLogger.endLog(ZWaveController.class, "Control a zwave device (Basic): MQTT command send");
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@PutMapping("/function/{endpoint_no}")
	public ResponseEntity control(@PathVariable int endpoint_no, @RequestBody ZWaveControl zWaveControl)
			throws InterruptedException, JsonGenerationException, JsonMappingException, IOException {
		AuditLogger.startLog(ZWaveController.class, "Control a zwave device (Function endpoint) : " + endpoint_no);
		zWaveControl.setEndpoint_no(endpoint_no);
		endpointService.zwaveControl(zWaveControl);
		AuditLogger.endLog(ZWaveController.class, "Control a zwave device (Function endpoint) : MQTT Command send");
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@PostMapping("/learn")
	public ResponseEntity registLearn(@RequestBody HashMap<String, Object> req)
			throws JsonProcessingException, InterruptedException {
		int mode = (int) req.getOrDefault("mode", -1);
		String serial = (String) req.getOrDefault("serial", "");
		AuditLogger.startLog(ZWaveController.class, "Set zwave \"Learn\" mode : " + serial + ", " + mode);
		Gateway gateway = gatewayRepository.findBySerial(serial);
		zWaveCertiNetworkManagementBasicService.setLearnMode(gateway, mode);
		AuditLogger.endLog(ZWaveController.class, "Set zwave \"Learn\" mode : MQTT Command send");
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@PostMapping("/reset")
	public ResponseEntity registReset(@RequestBody HashMap<String, Object> req)
			throws JsonProcessingException, InterruptedException {
		String serial = (String) req.getOrDefault("serial", "");
		AuditLogger.startLog(ZWaveController.class, "Set zwave \"Reset\" mode : " + serial);
		Gateway gateway = gatewayRepository.findBySerial(serial);
		zWaveCertiNetworkManagementBasicService.setZWaveResetMode(gateway);
		AuditLogger.endLog(ZWaveController.class, "Set zwave \"Reset\" mode : MQTT Command send");
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
