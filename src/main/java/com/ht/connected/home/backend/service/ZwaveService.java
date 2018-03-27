package com.ht.connected.home.backend.service;

import java.util.HashMap;

import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.model.entity.Certification;
import com.ht.connected.home.backend.service.base.CrudService;

public interface ZwaveService extends CrudService<Certification, Integer>{

	Object execute(HashMap<String, Object> req, ZwaveRequest zwaveCommonRequest, boolean b);

	
}
