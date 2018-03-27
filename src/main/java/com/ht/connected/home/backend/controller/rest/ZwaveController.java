package com.ht.connected.home.backend.controller.rest;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.service.ZwaveService;

@RestController
@RequestMapping("/zwave")
public class ZwaveController {
	@Autowired
	private ZwaveService zwaveService;

	/**
	 * 모든 요청에 version 이 있다 모든 요청을 처리가능
	 *
	 * @param classKey
	 * @param commandKey
	 * @param version
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/{classKey}/{commandKey}/{version}")
	public Object getRequestVersion(@PathVariable("classKey") String classKey,
			@PathVariable("commandKey") String commandKey, @PathVariable("version") String version,
			@RequestBody HashMap<String, Object> req) {
		ZwaveRequest zwaveCommonRequest = new ZwaveRequest(req, classKey, commandKey, version);
		return zwaveService.execute(req, zwaveCommonRequest, true);
	}
}
