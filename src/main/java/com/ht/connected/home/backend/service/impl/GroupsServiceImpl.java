package com.ht.connected.home.backend.service.impl;

import com.ht.connected.home.backend.model.entity.Groups;
import com.ht.connected.home.backend.repository.GroupsRepository;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupsServiceImpl extends CrudServiceImpl<Groups , Integer> {
	private GroupsRepository groupsRepository;

	@Autowired
	public GroupsServiceImpl(@NotNull GroupsRepository groupsRepository) {
		super(groupsRepository);
		this.groupsRepository = groupsRepository;
	}
}
