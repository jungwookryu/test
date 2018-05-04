package com.ht.connected.home.backend.service.impl;

import com.ht.connected.home.backend.model.entity.GroupUser;
import com.ht.connected.home.backend.repository.GroupsUsersRepository;
import com.ht.connected.home.backend.service.GroupsUsersService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupsUsersServiceImpl extends CrudServiceImpl<GroupUser , Integer> implements GroupsUsersService{
	private GroupsUsersRepository groupsUsersRepository;

	@Autowired
	public GroupsUsersServiceImpl(GroupsUsersRepository groupsUsersRepository) {
		super(groupsUsersRepository);
		this.groupsUsersRepository = groupsUsersRepository;
	}
}
