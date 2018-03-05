package com.ht.connected.home.backend.service.impl;

import com.ht.connected.home.backend.model.entity.GroupsUsers;
import com.ht.connected.home.backend.repository.GroupsUsersRepository;
import com.ht.connected.home.backend.service.GroupsUsersService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupsUsersServiceImpl extends CrudServiceImpl<GroupsUsers , Integer> implements GroupsUsersService{
	private GroupsUsersRepository groupsUsersRepository;

	@Autowired
	public GroupsUsersServiceImpl(@NotNull GroupsUsersRepository groupsUsersRepository) {
		super(groupsUsersRepository);
		this.groupsUsersRepository = groupsUsersRepository;
	}
}
