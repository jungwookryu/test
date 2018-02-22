package com.ht.connected.home.backend.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

import com.ht.connected.home.backend.HtConnectedHomeServerApplication;

@Configuration
public class ServletConfig extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HtConnectedHomeServerApplication.class);
    }
}