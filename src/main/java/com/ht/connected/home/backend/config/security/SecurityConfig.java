package com.ht.connected.home.backend.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	
//    @Bean
//    public AES256CBC passwordEncoder() {
//        return new AES256CBC();
//    }
//    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http
                .authorizeRequests()
//                		.requestMatchers(EndpointRequest.to("health", "flyway")).permitAll()
//                        .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("MY_ADMIN")
                .antMatchers("/**")
                .permitAll()
                .and()
                .csrf().disable();
    }
}
