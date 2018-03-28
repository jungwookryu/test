package com.ht.connected.home.backend.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class HtLogoutSuccessHandler  extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(HtLogoutSuccessHandler.class);
	public HtLogoutSuccessHandler() {
        super();
    }

    // API
    @Override
    public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
        final String refererUrl = request.getHeader("Referer");
        SecurityContextHolder.getContext().setAuthentication(null);
        request.getSession().invalidate();
        logger.debug(refererUrl);

        super.onLogoutSuccess(request, response, authentication);
    }
}
