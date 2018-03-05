package com.ht.connected.home.backend.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error/")
public class ErrorController extends CommonController {

	@Autowired
	@Qualifier(value = "errorMessageSource")
	MessageSource errMessageSource;
	
	@RequestMapping(value = "/{status}")
	@ResponseBody
	public ResponseEntity forbiddenError(@PathVariable("status") String status, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, HttpSession session) {

		if ("403".equals(status) || "401".equals(status)) {
			expireLogout(httpServletRequest, httpServletResponse, session);
			return new ResponseEntity(errMessageSource.getMessage("403", null, httpServletRequest.getLocale()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
