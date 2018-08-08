package com.ht.connected.home.backend.exception;

import java.io.IOException;
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
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@ControllerAdvice
public class GlobalControllerException {

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
        logger.error(exception.getLocalizedMessage());
        exception.printStackTrace();
        return ResponseEntity.badRequest().body("notAccceptable");
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> onJsonMappingException(JsonProcessingException exception) {
        logger.error("Zwave Handler not defined for classKey %s" + exception.getLocalizedMessage());
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body("onJsonProcessingException : " + exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> onIllegalArgumentException(IllegalArgumentException exception) {
        logger.error("Zwave Handler not defined for classKey %s"+exception.getLocalizedMessage());
        exception.printStackTrace();
        
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body("onIllegalArgumentException : " + exception.getMessage());
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<String> onJsonMappingException(JsonMappingException exception) {
        logger.error("Zwave Handler not defined for classKey %s" + exception.getLocalizedMessage());
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body("onJsonMappingException : " + exception.getMessage());
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<String> onJsonProcessingException(JsonParseException exception) {
        logger.error("Zwave Handler not defined for classKey %s" + exception.getLocalizedMessage());
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body("onJsonParseException : " + exception.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> onJsonProcessingException(IOException exception) {
        logger.error("Zwave Handler not defined for classKey %s" + exception.getLocalizedMessage());
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body("onJsonProcessingException : " + exception.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> authenticationException(
            AuthenticationException exception) {
    	logger.warn("Auth Exception occurs", exception.fillInStackTrace());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("AuthenticationException request: " + exception.getMessage());
    }

    @ExceptionHandler(GenericJDBCException.class)
    public ResponseEntity<String> genericJDBCException(
            GenericJDBCException exception) {
    	logger.warn("JDBC Exception occurs", exception.fillInStackTrace());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("server error: " + exception.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> noHandlerFoundHandler(
            NoHandlerFoundException exception) {
    	logger.warn("NoHandlerFoundException occurs", exception.fillInStackTrace());
        return ResponseEntity.badRequest()
                .body("Invalid request: " + exception.getMessage());
    }
    
    @ExceptionHandler(BadClientCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> noBadClientCredentialsException(
            BadClientCredentialsException exception) {
    	logger.warn("BadClientCredentialsException occurs", exception.fillInStackTrace());
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("AuthenticationException request: " + exception.getMessage());
    }


}
