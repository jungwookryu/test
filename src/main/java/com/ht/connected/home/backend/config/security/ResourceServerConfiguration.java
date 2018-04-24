package com.ht.connected.home.backend.config.security;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.util.matcher.RequestMatcher;


@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
	@Value("${security.oauth2.resource.id}")
    private String resourceId;
    @Autowired
    private DefaultTokenServices tokenServices;

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
                .resourceId(resourceId)
                .tokenServices(tokenServices)
                .tokenStore(tokenStore);
    }

    public void configure(HttpSecurity http) throws Exception {
    	logger.debug("configure::::::::::HttpSecurity::::::::::::start22222222222"+SecurityProperties.ACCESS_OVERRIDE_ORDER);
    	http
    	.requestMatcher(new OAuthRequestedMatcher())
        .authorizeRequests()
        .antMatchers("/adduser").permitAll()
        .antMatchers("/zwave").permitAll()
        .antMatchers("/zwave/**").permitAll()
        .antMatchers("/authentication/login").permitAll()
        .antMatchers(HttpMethod.POST,"/users").permitAll()
        .antMatchers("/passwordReset/**").permitAll()
        .antMatchers("/error/*").permitAll()
        .and()
        .authorizeRequests()
        .anyRequest().access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))");
    }

    private static class OAuthRequestedMatcher implements RequestMatcher {
        public boolean matches(HttpServletRequest request) {               
            String auth = request.getHeader("Authorization");
            // Determine if the client request contained an OAuth Authorization
            boolean haveOauth2Token = (auth != null) && (auth.startsWith("Bearer") || auth.startsWith("bearer"));
            boolean haveAccessToken = request.getParameter("access_token")!=null;
            
            return haveOauth2Token || haveAccessToken;
        }
    }

}
