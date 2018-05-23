package com.ht.connected.home.backend.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.config.security.SecretKeyProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

public class CommonController extends Common {
	@Autowired
	private SecretKeyProvider keyProvider;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * 사용자 접속을 위한 SSO로 들어오는 사용자만 허용되는 session 확인
	 *
	 * @return Authentication
	 */
	public Authentication getAuthentication() {
		if (SecurityContextHolder.getContext() == null) {
			throw new InternalAuthenticationServiceException("세션이 존재하지 않습니다.", new Exception("세션에러"));
		}
		return SecurityContextHolder.getContext().getAuthentication();

	}

	public void expireLogout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
		SecurityContextLogoutHandler ctxLogOut = new SecurityContextLogoutHandler(); // concern
		Authentication auth = getAuthentication(); // concern you
		ctxLogOut.logout(httpServletRequest, httpServletResponse, auth);
	}
	public String getAuthUserEmail(String access_token) {
		return getAuthUserEmail();
	}

	public String getAuthUserEmail() {
		return (String) getAuthentication().getPrincipal();
	}
}
