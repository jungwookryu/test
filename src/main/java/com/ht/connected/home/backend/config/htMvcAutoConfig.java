package com.ht.connected.home.backend.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
public class htMvcAutoConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {
		resourceHandlerRegistry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/**");
		super.addResourceHandlers(resourceHandlerRegistry);
	}

}