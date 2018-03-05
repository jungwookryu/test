package com.ht.connected.home.backend.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class GlobalControllerException{
	
	@Autowired
	@Qualifier(value = "errorMessageSource")
    MessageSource errMessageSource;
	
	protected Logger logger;

    protected String message;
    protected String messageKey;
    protected Object[] messageParameters;
    protected Exception wrappedException;
    
	public GlobalControllerException() {
		this("BaseException without message", null, null);
		logger = LoggerFactory.getLogger(getClass());
	}
	
	/**
	* GlobalControllerException 생성자
	* @param defaultMessage 메세지 지정
	*/
    public GlobalControllerException(String defaultMessage) {
        this(defaultMessage, null, null);
    }

    /**
     * GlobalControllerException 생성자
     * @param wrappedException 발생한 Exception 내포함.
     */

    public GlobalControllerException(Throwable wrappedException) {
        this("BaseException without message", null, wrappedException);
    }
    
    
	@ExceptionHandler(RuntimeException.class)
	public String handleBaseRuntimeException(RuntimeException re) {
		re.printStackTrace();
		return re.getMessage();
	}
	
	@ExceptionHandler(Exception.class)
	public String handleBaseException(Exception e) {
		e.printStackTrace();
		return e.getMessage();
	}

	@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation") // 409
	@ExceptionHandler(DataIntegrityViolationException.class)
	public void conflict(HttpServletRequest req, Exception ex) {
		
	}
	
	public GlobalControllerException(String s, Object object, Object object2) {
		logger.debug(s);
		
	}
	
	// Total control - setup a model and return the view name yourself. Or
	// consider subclassing ExceptionHandlerExceptionResolver (see below).
	/*@ExceptionHandler(Exception.class)
	public ModelAndView handleError(HttpServletRequest req, Exception ex) {
		logger.error("Request: " + req.getRequestURL() + " raised " + ex);

		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", ex);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName("error");
		return mav;
	}
	*/
}