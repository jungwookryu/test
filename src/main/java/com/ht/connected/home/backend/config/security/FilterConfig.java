package com.ht.connected.home.backend.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.ht.connected.home.backend.config.HttpLoggerFilter;
import com.ht.connected.home.backend.config.LoggerFilter;

@Configuration
public class FilterConfig {
	@Bean
	public CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return characterEncodingFilter;
	}

	@Bean
	public LoggerFilter loggerFilter() {
		return new LoggerFilter();
	}

	@Bean
	public HttpLoggerFilter httpLoggerFilter() {
		return new HttpLoggerFilter();
	}
}
