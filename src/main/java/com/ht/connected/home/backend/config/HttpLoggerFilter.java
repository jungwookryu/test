package com.ht.connected.home.backend.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

public class HttpLoggerFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger("HTTP");

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (logger.isDebugEnabled()) {
			HttpServletRequest requestToCache = new ContentCachingRequestWrapper((HttpServletRequest) request);
			HttpServletResponse responseToCache = new ContentCachingResponseWrapper((HttpServletResponse) response);
			chain.doFilter(requestToCache, responseToCache);

			String requestData = getRequestData(requestToCache);
			StringBuilder sb = new StringBuilder("Request - ");

			StringBuffer requestUrl = requestToCache.getRequestURL();
			String queryString = requestToCache.getQueryString();
			if (queryString != null) {
				requestUrl.append("?" + queryString);
			}
			
			sb.append("[" + requestToCache.getMethod() + "] ");
			sb.append(requestUrl);
			sb.append("\n");
			sb.append(requestData != null ? requestData : "");
			logger.debug(sb.toString());

			String responseData = getResponseData(responseToCache);
			sb = new StringBuilder("Response - ");
			sb.append("[" + responseToCache.getStatus() + "] ");
			sb.append(requestUrl + "\n");
			sb.append(responseData != null ? responseData : "");
			logger.debug(sb.toString());
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
	}

	private static String getRequestData(final HttpServletRequest request) throws UnsupportedEncodingException {
		String payload = null;
		ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
		if (wrapper != null) {
			byte[] buf = wrapper.getContentAsByteArray();
			if (buf.length > 0) {
				payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
			}
		}
		return payload;
	}

	private static String getResponseData(final HttpServletResponse response) throws IOException {
		String payload = null;
		ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response,
				ContentCachingResponseWrapper.class);
		if (wrapper != null) {
			byte[] buf = wrapper.getContentAsByteArray();
			if (buf.length > 0) {
				payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
				wrapper.copyBodyToResponse();
			}
		}
		return payload;
	}

}
