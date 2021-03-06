package com.ht.connected.home.backend.config.security;
import java.util.List;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.ht.connected.home.backend.client.user.UserDetailService;
import com.ht.connected.home.backend.common.Common;
@Component
public class HtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider{

    /**
     * The Logger for this class.
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * A Spring Security UserDetailsService implementation based upon the
     * Account entity model.
     */
    @Autowired
    private UserDetailService userDetailsService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken token) throws AuthenticationException {
        logger.info("additionalAuthenticationChecks start");

        if (token.getCredentials() == null || userDetails.getPassword() == null) {
            throw new BadCredentialsException("Credentials may not be null.");
        }
        
        String encoderPass = Common.encryptHash(MessageDigestAlgorithms.SHA_256, (String) token.getCredentials());
        logger.info("credential is {}"+ token.getCredentials());
        if(!encoderPass.equals(userDetails.getPassword())){
        	throw new BadCredentialsException("Invalid credentials....>>>>");
        }
    }
    /* (non-Javadoc)
     * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider#retrieveUser(java.lang.String, org.springframework.security.authentication.UsernamePasswordAuthenticationToken)
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken token) throws AuthenticationException {
        logger.info("==>retrieveUser");
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        if(null == userDetails){
        	logger.info("[userDetails==null]", username);
        }
        return userDetails;
    }
    
    @Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String encoderPass = authentication.getCredentials().toString();
		encoderPass = Common.encryptHash(MessageDigestAlgorithms.SHA_256, encoderPass);
		UserDetails userDetails = userDetailsService.loadUserByUsername(name);
		if (!encoderPass.equals(userDetails.getPassword())) {
			logger.error("Authentication failed for user = " + name);
			throw new BadCredentialsException("Authentication failed for user = " + name);
		}

		// find out the exited users
		List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) userDetails.getAuthorities();
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(name, encoderPass,
		        grantedAuthorities);

		logger.info("Succesful Authentication with user = " + name);
		return auth;
	}
}
