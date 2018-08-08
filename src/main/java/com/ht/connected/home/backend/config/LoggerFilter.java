package com.ht.connected.home.backend.config;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.MDC;
import org.springframework.web.filter.GenericFilterBean;

public class LoggerFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String logTraceId = UUID.randomUUID().toString();
		MDC.put("TRACE_ID", logTraceId);
		chain.doFilter(request, response);
	}

}
