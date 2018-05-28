/**
 * Project : ht-iot-connected-home-backend-server
 * Package : com.ht.connected.home.backend.service.impl
 * File : HtAuthenticationToken.java
 * Description : 
 * @auther : COM
 * @version : 1.0
 * @since : 2018. 3. 6.
 */
package com.ht.connected.home.backend.config.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author COM
 *
 */
public interface HtAuthenticationToken {

	void beforeAuthentication(AuthenticationToken token) throws AuthenticationException;

	void onSuccessfulAuthentication(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException;

	void onUnsuccessfulAuthentication(AuthenticationToken token, AuthenticationInfo info)
			throws AuthenticationException;

}
