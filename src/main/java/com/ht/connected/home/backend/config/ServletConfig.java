package com.ht.connected.home.backend.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

import com.ht.connected.home.backend.HtConnectedHomeServerApplication;
import com.ht.connected.home.backend.config.service.RabbitConfiguration;

@Configuration
@AutoConfigureBefore(RabbitConfiguration.class)
public class ServletConfig extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HtConnectedHomeServerApplication.class);
    }
    
}