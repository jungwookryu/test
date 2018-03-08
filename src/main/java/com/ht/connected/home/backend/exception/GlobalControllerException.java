package com.ht.connected.home.backend.exception;

import java.util.Locale;

import org.hibernate.exception.GenericJDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalControllerException{
	
	@Autowired
	@Qualifier(value = "errorMessageSource")
	private MessageSource errorMessageSource;
	
	private MessageSourceAccessor accessor = new MessageSourceAccessor(errorMessageSource, Locale.KOREA);;
	
	protected Logger logger = LoggerFactory.getLogger(GlobalControllerException.class);
    protected static String message = "defalut.Base.Exception";
    protected String messageKey;
    protected Object[] messageParameters;
    protected Exception wrappedException;
    
    
    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
  	public ResponseEntity<String> onExampleError(RestClientException exception) {
    	exception.printStackTrace();
  		return ResponseEntity.badRequest().body("notAccceptable");
  	}


    @ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<String> authenticationException(
			AuthenticationException exception) {
		exception.printStackTrace();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body("AuthenticationException request: " + exception.getMessage());
	}
    
    @ExceptionHandler(GenericJDBCException.class)
	public ResponseEntity<String> genericJDBCException(
			GenericJDBCException exception) {
		exception.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("server error: " + exception.getMessage());
	}
    
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<String> noHandlerFoundHandler(
			NoHandlerFoundException exception) {
		exception.printStackTrace();
		return ResponseEntity.badRequest()
				.body("Invalid request: " + exception.getMessage());
	}
	
}