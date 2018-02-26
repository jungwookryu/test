package com.ht.connected.home.backend.exception;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@RestController
public class GlobalControllerException{
	
	@Resource(name = "errorMessageSource")
    ReloadableResourceBundleMessageSource errMessageSource;
	
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
	
	public GlobalControllerException(String string, Object object, Object object2) {
		// TODO Auto-generated constructor stub
	}
	
	// Total control - setup a model and return the view name yourself. Or
	// consider subclassing ExceptionHandlerExceptionResolver (see below).
	@ExceptionHandler(Exception.class)
	public ModelAndView handleError(HttpServletRequest req, Exception ex) {
		logger.error("Request: " + req.getRequestURL() + " raised " + ex);

		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", ex);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName("error");
		return mav;
	}
	
}