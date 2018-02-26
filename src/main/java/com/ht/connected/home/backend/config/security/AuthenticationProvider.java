package com.ht.connected.home.backend.config.security;

import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.service.UsersService;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class AuthenticationProvider implements AuthenticationProvider{

	
	private final UsersService usersService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    public AuthenticationProvider(UsersService usersService) {
        logger.debug("AuthenticationProvider start");
        this.usersService = usersService;
        logger.debug("AuthenticationProvider end");
    }
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		 Authentication rtnAuthentication ;
		 final Object details = authentication.getDetails();
	        logger.info("authenticate start ::: name"+name);
	        Authentication rtnAuthentication ;
	        if (!(details instanceof DashboardAuthenticationDetails)) {
	            logger.debug(" authentication details [" + details.getClass().getName()  + "] are not an instance of  start");
	            throw new InternalAuthenticationServiceException("The authentication details [" + details
	                  + "] are not an instance of " + DashboardAuthenticationDetails.class.getSimpleName());
	        }

	        DashboardAuthenticationDetails dashboardAuthenticationDetails = (DashboardAuthenticationDetails) details;
	        //instance manage service false 일경우 권한없음 발생
		 if (!(details instanceof AuthenticationDetails)) {
	            logger.debug(" authentication details [" + details.getClass().getName()  + "] are not an instance of  start");
	            throw new InternalAuthenticationServiceException("The authentication details [" + details
	                  + "] are not an instance of " + DashboardAuthenticationDetails.class.getSimpleName());
	        }

	        DashboardAuthenticationDetails dashboardAuthenticationDetails = (DashboardAuthenticationDetails) details;
	        //instance manage service false 일경우 권한없음 발생
	        if(!dashboardAuthenticationDetails.isManagingService()) {
	            throw new InternalAuthenticationServiceException("instance authentication not exist by user based on [" + name + "]");
	        }
	        try {
	            /**사용자와 instanceId에대한 권한은 체크 */
		if (instanceUse.size() == 0) {
		    /**
		 * 다른 조직의 사용자일 경우 인스턴스 정보만 추가되고 사용자는 추가 되지 않아야함.
		 */
		LinkedHashMap requestInstanceUser = new LinkedHashMap();
		requestInstanceUser.put("instanceId", dashboardAuthenticationDetails.getManagingServicesInstance());
		requestInstanceUser.put("userId",name);
		requestInstanceUser.put("repoRole","owner");
		requestInstanceUser.put("createrYn","N");
		requestInstanceUser.put("displayName",name);
		requestInstanceUser.put("acitve",true);
		    ResponseEntity rtnInstanceUser = userService.createInstanceUser(requestInstanceUser);
		    if (200 == rtnInstanceUser.getStatusCodeValue()) {
		        instanceUse= instanceUseService.getAll(dashboardAuthenticationDetails.getManagingServicesInstance(), name);
		    }
		}
		//사용자가 없을 경우 생성하는 자동으로 사용자 추가하는 로직 추가함
		Map rtnUser = rss.getBody();
		if(Common.empty(rtnUser.get("ScUser"))||Common.empty(rtnUser.get("rtnUser"))){
		Map createUser = new HashMap();
		createUser.put("name",name);
		if(Common.empty(rtnUser.get("rtnUser"))){
		createUser.put("PasswordSet","false");
		    }
		    userService.createUser(createUser);
		}
		
		if (instanceUse.size() >0){
		    instanceUse.forEach(e -> lstInstanceUser.add(objectMapper.convertValue(e, InstanceUser.class)));
		    lstInstanceUser.forEach(e -> {
		            role.add(new SimpleGrantedAuthority("ROLE_"+e.getRepoRole().toUpperCase()));
		    role.add(new SimpleGrantedAuthority("ROLE_CREATE_"+e.getCreaterYn().toUpperCase()));
		    });
		}
		
		// 사용자 정보조회
		ResponseEntity<Map> rsltRss = userService.getUser(name);
		Map map = rsltRss.getBody();
		if(Common.empty(map.get("rtnUser"))|| Common.empty(map.get("ScUser"))){
		throw new InternalAuthenticationServiceException("Error while creating a user based on [" + name + "]");
		}
		
		Map scUserMap = (Map) map.get("ScUser");
		Map rtnUserAfter = (Map) map.get("rtnUser");
		String mail = (String) scUserMap.getOrDefault("userMail", "");
		String desc = (String) scUserMap.getOrDefault("userDesc", "");
		boolean admin = (boolean) rtnUserAfter.getOrDefault("admin", false);
		boolean active = (boolean) rtnUserAfter.getOrDefault("active", false);
		boolean password = false;
		if(rtnUserAfter.getOrDefault("properties", null)!=null){
		password = Boolean.parseBoolean((String)((LinkedHashMap)rtnUserAfter.get("properties")).getOrDefault("PasswordSet","false"));
		}
		// 로그인한 사람의 권한은 OWNER
		dashboardAuthenticationDetails.setActive(active);
		dashboardAuthenticationDetails.setAdmin(admin);
		dashboardAuthenticationDetails.setUserDesc(desc);
		dashboardAuthenticationDetails.setEmail(mail);
		dashboardAuthenticationDetails.setActive(active);
		dashboardAuthenticationDetails.setAdmin(admin);
		dashboardAuthenticationDetails.setPermissions(role);
		dashboardAuthenticationDetails.setPasswordSet(password);
		rtnAuthentication = new OAuth2Authentication(((OAuth2Authentication) authentication).getOAuth2Request()
		        , new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), "N/A", role));
		    ((OAuth2Authentication) rtnAuthentication).setDetails(dashboardAuthenticationDetails );
		} catch (Exception e) {
		    e.printStackTrace();
		    logger.error("Error while creating a user based on ",e);
		throw new InternalAuthenticationServiceException("Error while creating a user based on [" + name + "]", e);
		}
		
		logger.debug("authenticate end");
		 
		 
		return rtnAuthentication;
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public boolean supports(Class<?> authentication){
        logger.debug("support start");
        try {
            return OAuth2Authentication.class.isAssignableFrom(authentication);
        }catch (Exception e){
            logger.error("supports::::::::::",e);
            return false;
        }
    }
}
