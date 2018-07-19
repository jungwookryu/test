package com.ht.connected.home.backend.gateway;

import static java.util.Objects.isNull;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ht.connected.home.backend.category.zwave.ZWaveController;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.controller.rest.CommonController;
import com.ht.connected.home.backend.user.User;
import com.ht.connected.home.backend.user.UserRepository;
import com.ht.connected.home.backend.userGateway.UserGateway;
import com.ht.connected.home.backend.userGateway.UserGatewayRepository;

/**
 * gateway(호스트)관련 요청 처리
 * @author 구정화
 */
@RestController
@RequestMapping("/gateway")
public class GatewayController extends CommonController {

    GatewayService gateWayService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GatewayRepository gatewayRepository;
    @Autowired
    UserGatewayRepository userGatewayRepository;
    @Autowired
    ZWaveController zwaveController;
    @Autowired
    public GatewayController(GatewayService gateWayService) {
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
     * 로그인한 사용자가 등록한 모든 gateway를 조회한다.
     * @param req
     * @return
     * @throws Exception
     */

    @GetMapping
    public ResponseEntity<HashMap<String, Object>> getGatewayList(
            @RequestParam(value = "status", required=false) String status)
            throws Exception {
        String authUserEmail = getAuthUserEmail();
        List lstGateways = gateWayService.getGatewayList(status, authUserEmail);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("list", lstGateways);
        return new ResponseEntity<HashMap<String, Object>>(map, HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/{no}")
    public ResponseEntity deleteGateway(@PathVariable("no") int no) throws Exception {
        gateWayService.delete(no);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PutMapping(value = "/{no}")
    public ResponseEntity modifyGateway(@PathVariable("no") int no, @RequestBody Gateway gateway) {
        String userEmail = getAuthUserEmail();
        Gateway originGateway = gateWayService.findOne(no);
        if(null!=originGateway) {
            if(originGateway.getCreatedUserId().equals(userEmail)){
                Gateway rtnGateway = gateWayService.modifyGateway(originGateway, gateway);
                return new ResponseEntity<>(rtnGateway,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
    
    @PutMapping
    public ResponseEntity shareGateway(@RequestBody HashMap map) {
        String userEmail = getAuthUserEmail();
        int no = (int) map.getOrDefault("gateway_no",-1);
        String share_user_email = (String) map.getOrDefault("share_user_email","");
        String mode = (String) map.getOrDefault("mode","");
        if(Common.empty(share_user_email)) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        if(-1 == no) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        Gateway originGateway = gateWayService.findOne(no);
        if(null==originGateway) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        List<User> users = userRepository.findByUserEmail(share_user_email);
        if(users.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
//       권한체크
//      if(!originGateway.getCreatedUserId().equals(userEmail)){
//        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
//}
        boolean rtnGateway = gateWayService.shareGateway(mode, originGateway, users.get(0));
        if(rtnGateway) {
            return new ResponseEntity<>(rtnGateway,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

    }
    
}
