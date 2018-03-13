package com.ht.connected.home.backend.config;

import com.ht.connected.home.backend.HtConnectedHomeServerApplication;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HtConnectedHomeServerApplication.class);
    }
}