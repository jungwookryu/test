package com.ht.connected.home.backend.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;


@Configuration
public class MessagesConfig {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("messages");
        return resourceBundleMessageSource;
    }
    
    @Bean
    @Qualifier("errorMessageSource")
    public MessageSource ErrorMessageSource() {
        ResourceBundleMessageSource errorResourceBundleMessageSource = new ResourceBundleMessageSource();
        errorResourceBundleMessageSource.setBasename("errormessages");
        return errorResourceBundleMessageSource;
    }
}