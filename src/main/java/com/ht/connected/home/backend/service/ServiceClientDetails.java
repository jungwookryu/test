package com.ht.connected.home.backend.service;

import com.ht.connected.home.backend.model.entity.Users;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

public class ServiceClientDetails implements ClientDetailsService {

	@Autowired
	UsersService usersService;
	private static final Logger logger = LoggerFactory.getLogger(ServiceClientDetails.class);
    
	@Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        
        List<Users> lstUsers = usersService.getUser(clientId);
        
        if (lstUsers.size()< 1 ) {
            return null;
        }
        
        Users users = lstUsers.get(0);
        BaseClientDetails baseClientDetails = new BaseClientDetails(users.getUserId(),
                null, null, users.getAuthority(), users.getAuthorities(),
                null);
        baseClientDetails.setClientSecret(users.getPassword());
        logger.info("BaseClientDetails = " + baseClientDetails);

        return baseClientDetails;
    }
    
}
