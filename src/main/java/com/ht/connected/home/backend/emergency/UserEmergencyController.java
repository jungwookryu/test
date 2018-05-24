package com.ht.connected.home.backend.emergency;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

import com.ht.connected.home.backend.controller.rest.CommonController;

@RestController
@RequestMapping("/emergency")
public class UserEmergencyController extends CommonController {

    UserEmergencyService userEmergencyService;

    @Autowired
    public UserEmergencyController(UserEmergencyService userEmergencyService) {
        this.userEmergencyService = userEmergencyService;
    }

    /**
     * 201, 204, 500, 406
     * @param users
     * @return
     * @throws UnsupportedEncodingException
     * @throws IllegalArgumentException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping
    public ResponseEntity createUserEmergency(@RequestBody UserEmergency userEmergency, HttpServletRequest request) throws IllegalArgumentException, UnsupportedEncodingException {
        userEmergency.setUserEmail(getAuthUserEmail());
        userEmergency.setCreatedTime(new Date());
        userEmergency.setLastModifiedTime(new Date());
        UserEmergency rtnUserEmergencys = userEmergencyService.register(userEmergency);
        logger.debug("rtnUserEmergencys:" + rtnUserEmergencys.toString());
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<HashMap<String, List>> getUserEmergencys() {
        String authUserEmail = getAuthUserEmail();
        List<UserEmergency> rtnUserEmergencys = userEmergencyService.getUserEmergency(authUserEmail);
        HashMap map = new HashMap();
        map.put("userEmergencys", new ArrayList());
        if (rtnUserEmergencys.size() > 0) {
            map.replace("userEmergencys", rtnUserEmergencys);
        }
        return new ResponseEntity(map, HttpStatus.OK);
    }

    @GetMapping("/{gatewayNo}")
    public ResponseEntity<HashMap<String, UserEmergency>> getUserEmergency(@PathVariable("gatewayNo") int gatewayNo) throws IllegalArgumentException, UnsupportedEncodingException {
        String authUserEmail = getAuthUserEmail();
        List<UserEmergency> rtnUserEmergencys = userEmergencyService.getUserEmergency(authUserEmail, gatewayNo);
        HashMap map = new HashMap();
        map.put("userEmergencys", new ArrayList());
        if (rtnUserEmergencys.size() > 0) {
            map.replace("userEmergencys", rtnUserEmergencys);
        }
        return new ResponseEntity(map, HttpStatus.OK);
    }

    @DeleteMapping("/{gatewayNo}")
    public ResponseEntity<HttpStatus> deleteUserEmergency(@PathVariable("gatewayNo") int gatewayNo) {
        userEmergencyService.delete(getAuthUserEmail(), gatewayNo);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    @PutMapping("/{no}")
    public ResponseEntity<HashMap<String, UserEmergency>> modifyUser(@PathVariable("no") int no, @RequestBody UserEmergency userEmergency) {
        HashMap<String, UserEmergency> map = new HashMap<>();
        UserEmergency rtnUserEmergency = userEmergencyService.modify(no, userEmergency);
        map.put("rtnUserEmergency", rtnUserEmergency);
        return new ResponseEntity<HashMap<String, UserEmergency>>(map, HttpStatus.OK);
    }
}
