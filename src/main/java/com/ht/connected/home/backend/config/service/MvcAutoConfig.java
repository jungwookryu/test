package com.ht.connected.home.backend.config.service;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan("com.ht.connected.home.backend")
@EntityScan("com.ht.connected.home.backend")
@AutoConfigureBefore(DispatcherServletAutoConfiguration.class)
public class MvcAutoConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {
		resourceHandlerRegistry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/**");
		super.addResourceHandlers(resourceHandlerRegistry);
	}

}