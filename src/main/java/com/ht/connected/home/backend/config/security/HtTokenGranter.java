package com.ht.connected.home.backend.config.security;


	import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

	/**
	 * A custom {@link TokenGranter} that always grants a token, and does not authenticate users (hence the client has to be
	 * trusted to only send authenticated client details).
	 * 
	 * @author Dave Syer
	 *
	 */
	public class HtTokenGranter extends AbstractTokenGranter {

		HtTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
				OAuth2RequestFactory requestFactory, String grantType) {
			super(tokenServices, clientDetailsService, requestFactory, grantType);
		}

		protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
			Map<String, String> params = tokenRequest.getRequestParameters();
			
			String username = params.containsKey("user_email") ? params.get("user_email") : "";
			Map map = tokenRequest.getRequestParameters();
			map.put("", )
			tokenRequest.setRequestParameters(map.);
			List<GrantedAuthority> authorities = params.containsKey("authorities") ? AuthorityUtils
					.createAuthorityList(OAuth2Utils.parseParameterList(params.get("authorities")).toArray(new String[0]))
					: AuthorityUtils.NO_AUTHORITIES;
			Authentication user = new UsernamePasswordAuthenticationToken(username, "N/A", authorities);
			OAuth2Authentication authentication = new OAuth2Authentication(tokenRequest.createOAuth2Request(client), user);
			return authentication;
		}
	}
