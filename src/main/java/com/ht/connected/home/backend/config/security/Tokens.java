package com.ht.connected.home.backend.config.security;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

public final class Tokens {
	 /**
	   * Constructs ...
	   *
	   */
	  private Tokens() {}

	  //~--- methods --------------------------------------------------------------

	  /**
	   * Build an {@link AuthenticationToken} for use with
	   * {@link Subject#login(org.apache.shiro.authc.AuthenticationToken)}.
	   *
	   *
	   * @param request servlet request
	   * @param username username of the user to authenticate
	   * @param password password of the user to authenticate
	   *
	   * @return authentication token
	   */
	  public static AuthenticationToken createAuthenticationToken(
	    HttpServletRequest request, String username, String password)
	  {
	    return new UsernamePasswordToken(username, password,
	      request.getRemoteAddr());
	  }
}
