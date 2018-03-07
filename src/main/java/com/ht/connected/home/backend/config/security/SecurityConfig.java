package com.ht.connected.home.backend.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	
	/**
	 * Role that users accessing the endpoint must have.
	 */
    public static final String ROLE_OWNER = "ROLE_OWNER";
	/**
	 * Role that users accessing the endpoint must have.
	 */
    public static final String ROLE_USER = "ROLE_USER";
    
	/**
	 * Role that users accessing the endpoint must have.
	 */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    
    public static final String ROLE_ACTIVE = "ROLE_ACTIVE";
    
    /**
	 * Role that users accessing the endpoint must have.
	 */
    public static final String ROLE_CREATE_Y = "ROLE_CREATE_Y";

    /**
	 * Role that users accessing the endpoint must have.
	 */
    public static final String ROLE_PASSWD_SET = "ROLE_PASSWD_SET";
	
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http
        	.csrf().disable()
            .authorizeRequests()
            .antMatchers("/group/*").access("hasRole('" + ROLE_OWNER + "')")
            .antMatchers("/admin/**").access("hasRole('" + ROLE_ADMIN+"')")
            .antMatchers("/autheication/*").access("hasRole('" + ROLE_ADMIN + "')")
            .antMatchers("/passwordReset/**").access("hasRole('" + ROLE_PASSWD_SET +"')")
            .antMatchers("/user/**").access("hasRole('" + ROLE_ACTIVE +"')")
	        .antMatchers("/error/*").permitAll()
	        .antMatchers("/login").permitAll()
	        .and()
	        .logout()
	        .logoutSuccessUrl("/logout?logout")
	        .and()
            .exceptionHandling().accessDeniedPage("/403");
    }
    

    

}
