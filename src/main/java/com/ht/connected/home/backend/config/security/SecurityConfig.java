package com.ht.connected.home.backend.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	
	 @Autowired
     private AuthenticationProvider authenticationProvider;
	 
	 @Autowired
     private LogoutSuccessHandler logoutSuccessHandler;

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
    /**
	 * Role that users accessing the endpoint must have.
	 */
    public static final String ROLE_CREATE_Y = "ROLE_CREATE_Y";

    /**
	 * Role that users accessing the endpoint must have.
	 */
    public static final String ROLE_PASSWD_SET = "ROLE_PASSWD_SET";
	
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationManager.class);
    
//    @Bean
//    public AES256CBC passwordEncoder() {
//        return new AES256CBC();
//    }
//    
    @Bean(name = "authenticationManager")
    public AuthenticationManager authenticationManager() {
        try {
            return super.authenticationManager();
        }catch(Exception e){

        	logger.debug("<<<<<<<<<<<<<authenticationManager<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            e.printStackTrace();
            logger.debug("<<<<<<<<<<<<<authenticationManager<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
        return new OAuth2AuthenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http
        	.csrf().disable()
            .authorizeRequests()
            .antMatchers("/passwordReset/**").access("hasRole('" + ROLE_PASSWD_SET +"')")
            .antMatchers("/user/**").access("hasRole('" + ROLE_USER + "')")
            .antMatchers("/group/*").access("hasRole('" + ROLE_OWNER + "')")
            .antMatchers("/autheication/*").access("hasRole('" + ROLE_ADMIN + "')")
            .antMatchers("/admin/**").access("hasRole('" + ROLE_ADMIN+"')")
            .antMatchers("**").permitAll()
	        .antMatchers("/error/*").permitAll()
	        .and()
	        .logout()
	        .logoutSuccessHandler(logoutSuccessHandler)
	        .logoutSuccessUrl("/index")
	        .invalidateHttpSession(true)
	        .and()
            .exceptionHandling().accessDeniedHandler((AccessDeniedHandler) new AccessDeniedException("not.enough.privileges"));
    }
}
