package com.ht.connected.home.backend.config.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtFilter implements Filter
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChanin) throws IOException, ServletException {
        logger.info("request.getInputStream()"+request.getInputStream());
        logger.info("request::::::::::"+request.getInputStream());
        
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
        
    }
 
}
