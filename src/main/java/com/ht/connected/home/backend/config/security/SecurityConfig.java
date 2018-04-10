package com.ht.connected.home.backend.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity(debug = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    @Autowired
	public UserDetailsService userDetailsService;
    
    @Autowired
	private HtAuthenticationProvider htAuthenticationProvider;

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService());
		return provider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.authenticationProvider(htAuthenticationProvider).userDetailsService(userDetailsService);

	}

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

    @Override
	protected void configure(HttpSecurity http) throws Exception {
    	logger.debug("configure::::::::::HttpSecurity::::::::::::start1"+SecurityProperties.ACCESS_OVERRIDE_ORDER);
        http
        .csrf().disable()
        .authorizeRequests()
        .anyRequest().authenticated()
        .antMatchers(HttpMethod.GET, "/addUser").permitAll()
        .antMatchers(HttpMethod.POST,"/users").permitAll()
        .antMatchers(HttpMethod.GET,"/users").permitAll()
        .antMatchers(HttpMethod.DELETE,"/users").permitAll()
        .antMatchers(HttpMethod.PUT,"/users").permitAll()
        .antMatchers("/users/**").permitAll()
        .antMatchers("/zwave/").permitAll()
        .antMatchers("/gateway").permitAll()
        .antMatchers("/users/user/**").permitAll()
        .antMatchers("/authentication/**").permitAll()
        .and()
        .exceptionHandling().accessDeniedPage("/error/403")
        .and().logout()
		.logoutSuccessUrl("/logout?logout")
        .invalidateHttpSession(true)
        ;
	}


    

}
