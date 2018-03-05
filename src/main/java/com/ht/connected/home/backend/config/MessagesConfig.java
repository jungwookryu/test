package com.ht.connected.home.backend.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;


@Configuration
public class MessagesConfig {

    @Bean
    public SessionLocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.KOREA);
        return sessionLocaleResolver;
    }
	
    @Bean(name="messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("/messages/messages**");
        resourceBundleMessageSource.setDefaultEncoding("UTF-8");
        return resourceBundleMessageSource;
    }
    
    @Bean(name="errorMessageSource")
    public MessageSource errorMessageSource() {
        ResourceBundleMessageSource errorResourceBundleMessageSource = new ResourceBundleMessageSource();
        errorResourceBundleMessageSource.setBasename("/error/errorMessages**");
        errorResourceBundleMessageSource.setDefaultEncoding("UTF-8");
        return errorResourceBundleMessageSource;
    }
}