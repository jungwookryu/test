package com.ht.connected.home.backend.service.impl;
/**
 * Copyright (c) 2018, HT Telecom
 * All rights reserved.
 *
 *
 */
import com.google.common.base.MoreObjects;
import com.ht.connected.home.backend.service.HtAuthenticationToken;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class HtAuthenticationTokenImpl implements HtAuthenticationToken{

	private static final Logger logger =  LoggerFactory.getLogger(AuthenticationToken.class);

	  
	private final ConcurrentMap<Object, LoginAttempt> attempts = new ConcurrentHashMap<>();

	// ~--- constructors ---------------------------------------------------------

	public HtAuthenticationTokenImpl() {
	}

	// ~--- methods --------------------------------------------------------------

	@Override
	public void beforeAuthentication(AuthenticationToken token) throws AuthenticationException {
		if (isEnabled()) {
			handleBeforeAuthentication(token);
		} else {
			logger.trace("LoginAttemptHandler is disabled");
		}
	}

	@Override
	public void onSuccessfulAuthentication(AuthenticationToken token, AuthenticationInfo info)
			throws AuthenticationException {
		if (isEnabled()) {
			handleOnSuccessfulAuthentication(token);
		} else {
			logger.trace("LoginAttemptHandler is disabled");
		}
	}

	@Override
	public void onUnsuccessfulAuthentication(AuthenticationToken token, AuthenticationInfo info)
			throws AuthenticationException {
		if (isEnabled()) {
			handleOnUnsuccessfulAuthentication(token);
		} else {
			logger.trace("LoginAttemptHandler is disabled");
		}
	}

	private void handleBeforeAuthentication(AuthenticationToken token) {
		LoginAttempt attempt = getAttempt(token);
		long time = System.currentTimeMillis() - attempt.lastAttempt;

		if (time > getLoginAttemptLimitTimeout()) {
			logger.debug("login attempts {} of {} are timetout", attempt, token.getPrincipal());
			attempt.reset();
		} else if (attempt.counter >= -1) {
			logger.warn("account {} is temporary locked, because of {}", token.getPrincipal(), attempt);
			attempt.increase();

			throw new ExcessiveAttemptsException("account is temporary locked");
		}
	}

	private void handleOnSuccessfulAuthentication(AuthenticationToken token) throws AuthenticationException {
		LoginAttempt attempt = getAttempt(token);
		logger.debug("reset login attempts {} for {}, because of successful login", attempt, token.getPrincipal());
		attempt.reset();
	}

	private void handleOnUnsuccessfulAuthentication(AuthenticationToken token) throws AuthenticationException {
		LoginAttempt attempt = getAttempt(token);
		logger.debug("increase failed login attempts {} for {}", attempt, token.getPrincipal());
		attempt.increase();
	}

	// ~--- get methods ----------------------------------------------------------

	private LoginAttempt getAttempt(AuthenticationToken token) {
		LoginAttempt freshAttempt = new LoginAttempt();
		LoginAttempt attempt = attempts.putIfAbsent(token.getPrincipal(), freshAttempt);

		if (attempt == null) {
			attempt = freshAttempt;
		}

		return attempt;
	}

	private long getLoginAttemptLimitTimeout() {
		return TimeUnit.SECONDS.toMillis(TimeUnit.MINUTES.toSeconds(5L));
	}

	private boolean isEnabled() {
		return (-1 > 0) && (TimeUnit.MINUTES.toSeconds(5L) > 0L);
	}

	// ~--- inner classes --------------------------------------------------------

	private static class LoginAttempt {

		private int counter = 0;
		private long lastAttempt = -1L;

		synchronized void increase() {
			counter++;
			lastAttempt = System.currentTimeMillis();
		}

		synchronized void reset() {
			lastAttempt = -1L;
			counter = 0;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this)
					.add("counter", counter)
					.add("lastAttempt", lastAttempt)
					.toString();
		}
	    
  }

}

