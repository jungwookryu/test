package com.ht.connected.home.backend.category.zwave;

import java.io.IOException;
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
import com.ht.connected.home.backend.category.zwave.constants.commandclass.NetworkManagementInclusionCommandClass;
import com.ht.connected.home.backend.category.zwave.endpoint.Endpoint;
import com.ht.connected.home.backend.category.zwave.endpoint.EndpointRepository;
import com.ht.connected.home.backend.category.zwave.endpoint.EndpointService;
import com.ht.connected.home.backend.controller.rest.CommonController;
import com.ht.connected.home.backend.gateway.Gateway;
import com.ht.connected.home.backend.gateway.GatewayRepository;
import com.ht.connected.home.backend.user.User;
import com.ht.connected.home.backend.user.UserRepository;
import com.ht.connected.home.backend.userGateway.UserGateway;
import com.ht.connected.home.backend.userGateway.UserGatewayRepository;

@RestController
@RequestMapping("/zwave")
public class ZWaveController extends CommonController {

    @Autowired
    ZWaveService zwaveService;

    @Autowired
    EndpointService endpointService;

    @Autowired
    UserGatewayRepository userGatewayRepository;
    @Autowired
    GatewayRepository gatewayRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ZWaveRepository zWaveRepository;

    @Autowired
    EndpointRepository endpointRepository;

    /**
     * 기기등록 취소
     * @param req
     * @return
     * @throws JsonProcessingException
     * @throws InterruptedException
     */
    @PostMapping
    public ResponseEntity regist(@RequestBody HashMap<String, Object> req) throws JsonProcessingException, InterruptedException {
        String userEmail = getAuthUserEmail();
        HashMap map = new HashMap<>();
        int classKey = NetworkManagementInclusionCommandClass.INT_ID;
        // mode 1 == add, 5==stop
        int mode = (int) req.getOrDefault("mode", -1);
        String s2pin = (String) req.get("s2pin");
        logger.debug("mode:::" + mode);
        int commandKey = NetworkManagementInclusionCommandClass.INT_NODE_ADD;
        String serial = (String) req.getOrDefault("serial", "");
        Gateway gateway = gatewayRepository.findBySerial(serial);
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
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/{gateway_no}")
    public ResponseEntity getList(@PathVariable("gateway_no") int gatewayNo) {

        Map sRtnList = (Map) zwaveService.getZWaveListApp(gatewayNo);
        return new ResponseEntity<>(sRtnList, HttpStatus.ACCEPTED);
    }

    @PutMapping("/zwaveInfo/{zwaveNo}")
    public ResponseEntity modifyEndpointInfo(@PathVariable int zwaveNo, @RequestBody ZWave requestZwave) throws JsonProcessingException {
        Endpoint endpoint = endpointRepository.findByZwaveNoAndEpid(zwaveNo, 0);
        if (endpoint != null) {
            endpoint.setNickname(requestZwave.getNickname());
            ZWave rtnZwave = endpointService.modify(endpoint.getNo(), endpoint);
            return new ResponseEntity<>(rtnZwave, HttpStatus.OK);
        } else {
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
        String userEmail = getAuthUserEmail();
        List<User> lstUser = userRepository.findByUserEmail(userEmail);
        if (lstUser.size() > 0) {
            List<UserGateway> lstUserGateway = userGatewayRepository.findByUserNo(lstUser.get(0).getNo());
            if (lstUserGateway.size() <= 0) {
                return new ResponseEntity<>(lstUserGateway.size(), HttpStatus.NOT_FOUND);
            } else {
                int iZwave = zwaveService.getByUserEmailAndNo(userEmail, no);
                if (iZwave > 0) {
                    int deleteInt = zwaveService.deleteByNo(no);
                    return new ResponseEntity<>(deleteInt, HttpStatus.ACCEPTED);
                } else {
                    return new ResponseEntity<>(0, HttpStatus.NO_CONTENT);
                }
            }
        }
        return new ResponseEntity<>(0, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{zwave_no}")
    public ResponseEntity basicControl(@PathVariable int zwave_no, @RequestBody ZWaveControl zWaveControl) throws JsonProcessingException, InterruptedException {
        zWaveControl.setZwave_no(zwave_no);
        zwaveService.zwaveBasicControl(zWaveControl);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    @PutMapping("/function/{endpoint_no}")
    public ResponseEntity control(@PathVariable int endpoint_no, @RequestBody ZWaveControl zWaveControl) throws InterruptedException, JsonGenerationException, JsonMappingException, IOException {
        zWaveControl.setEndpoint_no(endpoint_no);
        endpointService.zwaveControl(zWaveControl);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
