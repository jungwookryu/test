package com.ht.connected.home.backend.exception;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


/**
 * @author lee in jeong
 * @version 1.0 , 2017.7.7
 * @Description Login RuntimeException 관련 CutomException 처리
 */

public class CustomLoginException extends Exception {

    public CustomLoginException() {
    }
    
    /**
     * 203 status
     * @param exception
     * @return
     */
    public ResponseEntity customLoginException(AuthorizationException exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    public ResponseEntity customLoginException(Exception exception) {
        Map map = new HashMap();
        map.put("error", exception.getMessage());
        return new ResponseEntity(exception.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}



