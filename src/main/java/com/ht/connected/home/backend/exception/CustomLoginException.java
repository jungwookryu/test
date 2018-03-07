package com.ht.connected.home.backend.exception;

import org.apache.shiro.authz.AuthorizationException;


/**
 * @author lee in jeong
 * @version 1.0 , 2017.7.7
 * @Description Login RuntimeException 관련 CutomException 처리
 */

public class CustomLoginException extends Exception {

    /**
     * 203 status
     * @param exception
     * @return
     */
    public CustomLoginException(AuthorizationException exception) {
    	super(exception.getMessage(), exception.getCause());
    }
}



