package com.ht.connected.home.backend.config.security;

import com.ht.connected.home.backend.model.entity.User;
import com.ht.connected.home.backend.service.UserDetailService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationSeverConfig extends AuthorizationServerConfigurerAdapter {

	@Value("${security.oauth2.resource.id}")
	private String resourceId;

	@Value("${access_token.validity_period}")
	private int accessTokenValiditySeconds;

	@Value("${refresh_token.validity_period}")
	private int refreshTokenValiditySeconds;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	DataSource dataSource;

	@Autowired
	UserDetailService userDetailsService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		try {
			endpoints.pathMapping("/oauth/token", "/authentication/login");
			endpoints.authenticationManager(this.authenticationManager).tokenServices(tokenServices())
					.tokenStore(tokenStore()).accessTokenConverter(accessTokenConverter());
		} catch (Exception e) {
			logger.info("AuthorizationServerEndpointsConfigurer exception");
			e.printStackTrace();
		}
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
		try {
			oauthServer.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')")
					.checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
		} catch (Exception e) {
			logger.info("AuthorizationServerSecurityConfigurer exception");
			e.printStackTrace();
		}

	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) {
		try {
			clients.jdbc(dataSource);
		} catch (Exception e) {
			logger.info("ClientDetailsServiceConfigurer exception");
			e.printStackTrace();
		}
	}

	@Bean
	public TokenStore tokenStore() {
		try {
			return new JwtTokenStore(accessTokenConverter());
		} catch (Exception e) {
			logger.info("tokenStore exception");
			e.printStackTrace();
			return null;
		}

	}

	@Autowired
	private SecretKeyProvider keyProvider;

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {

		JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {

			@Override
			public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
				if (authentication.isAuthenticated()) {
					if (null != authentication.getUserAuthentication()) {
						String userEmail = authentication.getUserAuthentication().getName();
						User users = userDetailsService.findUserDetailByUsername(userEmail);
						final Map<String, Object> additionalInformation = new HashMap<>();
						additionalInformation.put("nickname", users.getNickName());
						((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
					}
				}
				OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);
				return enhancedToken;
			}
		};

		try {
			converter.setSigningKey(keyProvider.getKey());
		} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException | CertificateException
				| URISyntaxException | IOException e) {
			logger.info("JwtAccessTokenConverter exception");
			e.printStackTrace();
		}
		return converter;
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		try {
			DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
			defaultTokenServices.setTokenStore(tokenStore());
			defaultTokenServices.setSupportRefreshToken(true);
			defaultTokenServices.setTokenEnhancer(accessTokenConverter());
			return defaultTokenServices;
		} catch (Exception e) {
			logger.info("tokenServices exception");
			e.printStackTrace();
		}
		return null;
	}

}
