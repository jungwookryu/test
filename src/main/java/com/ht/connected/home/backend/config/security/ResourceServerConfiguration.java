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
        http.authorizeRequests()
        	.antMatchers("/authentication/**").permitAll()
        	.antMatchers("/users").permitAll()
        	.and()
        	.requestMatcher(new OAuthRequestedMatcher())
            .anonymous().disable()
            .authorizeRequests()
			.antMatchers("/","/**").permitAll()
            .antMatchers(HttpMethod.OPTIONS).permitAll();
//    			.antMatchers("/authentication/login").permitAll()
//    			.antMatchers("/group/*").access("hasRole('" + ROLE_OWNER + "') or hasRole('" + ROLE_USER + "')")
//    			.antMatchers("/admin/**").access("hasRole('" + ROLE_OWNER + "') or hasRole('" + ROLE_USER + "')")
//    			.antMatchers("/autheication/*").access("hasRole('" + ROLE_OWNER + "') or hasRole('" + ROLE_USER + "')")
//    			.antMatchers("/passwordReset/**").access("hasRole('" + ROLE_OWNER + "') or hasRole('" + ROLE_USER + "')")
//    			.antMatchers("/user/**").access("hasRole('" + ROLE_OWNER + "') or hasRole('" + ROLE_USER + "')")
//    			.antMatchers("/error/*").permitAll().antMatchers("/login")
//    			.permitAll()
//                .antMatchers("/","/**").permitAll();
//    			.and().logout()
//    			.logoutSuccessUrl("/logout?logout").and().exceptionHandling().accessDeniedPage("/403");
//                .antMatchers("/api/hello").access("hasAnyRole('USER')")
//                .antMatchers("/api/me").hasAnyRole("USER", "ADMIN")
//                .antMatchers("/api/register").hasAuthority("ROLE_REGISTER");
    }

    private static class OAuthRequestedMatcher implements RequestMatcher {
        public boolean matches(HttpServletRequest request) {
            String auth = request.getHeader("Authorization");
            // Determine if the client request contained an OAuth Authorization
            boolean haveOauth2Token = (auth != null) && auth.startsWith("Bearer");
            boolean haveAccessToken = request.getParameter("access_token")!=null;
            return haveOauth2Token || haveAccessToken;
        }
    }



}
