package com.ht.connected.home.backend.exception;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * https://www.toptal.com/java/spring-boot-rest-api-error-handling
 * @author ijlee
 *
 */
public class HtIoTRestBaseException extends Exception{
	private HttpStatus status;
	
	@Autowired
	@Qualifier("errorMessageSource")
	private MessageSource errorMessageSource;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss" )
	private LocalDateTime timestamp;
	private Locale locale = Locale.KOREA;
	private String message;
	private String debugMessage; 
	private List<SubError> subErrors;

   HtIoTRestBaseException() {
       this.timestamp = LocalDateTime.now();
   }

   HtIoTRestBaseException(HttpStatus status) {
       this();
       this.status = status;
   }

   HtIoTRestBaseException(HttpStatus status, Throwable ex) {
       this();
       this.status = status;
       this.message = "Unexpected error";
       this.debugMessage = ex.getLocalizedMessage();
   }

   HtIoTRestBaseException(HttpStatus status, String message, Throwable ex, Locale locale) {
       this();
       this.status = status;
       this.locale = locale;
       this.message = errorMessageSource.getMessage(message, new Object[0], locale);
       this.debugMessage = ex.getLocalizedMessage();
   }
  	
}
class SubError{
	
	private String code;
	
	private String message;
	
	public SubError() {
		
	}
	public SubError(String code,String message) {
		this.code = code;
		this.message = message;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}