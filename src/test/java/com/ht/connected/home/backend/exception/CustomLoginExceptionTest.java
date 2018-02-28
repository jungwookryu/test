package com.ht.connected.home.backend.exception;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.shiro.authz.AuthorizationException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Configuration
public class CustomLoginExceptionTest {
	
	@Autowired
	@Qualifier("errorMessageSource")
	MessageSource errorMessageSource;
	
    @Before
    public void setUp() {
    	
    }
    
	@Test
	public void customLoginExceptionTest() {
		
		AuthorizationException authorizationException = 
				new AuthorizationException(errorMessageSource.getMessage("failed.authentication ", null , Locale.ENGLISH));
		new CustomLoginException().customLoginException(authorizationException);
		
		Mockito.doNothing().when(authorizationException);

	}
	
	
	


public ResponseEntity CustomLoginException(Exception exception) {
    Map map = new HashMap();
    map.put("error", exception.getMessage());
    return new ResponseEntity(exception.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
}
}
