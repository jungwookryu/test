package com.ht.connected.home.backend.service.impl;

import com.ht.connected.home.backend.model.entity.Groups;
import com.ht.connected.home.backend.repository.GroupsRepository;
import com.ht.connected.home.backend.service.GroupsService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupsServiceImpl extends CrudServiceImpl<Groups , Integer> implements GroupsService{
	private GroupsRepository groupsRepository;

	@Autowired
	public GroupsServiceImpl(GroupsRepository groupsRepository) {
		super(groupsRepository);
		this.groupsRepository = groupsRepository;
	}
}
