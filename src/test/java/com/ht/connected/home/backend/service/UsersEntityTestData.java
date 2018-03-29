package com.ht.connected.home.backend.service;

import com.ht.connected.home.backend.model.entity.Users;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public final class UsersEntityTestData extends MockUtil
{


    private UsersEntityTestData() {}

    public static List<Users> getLstUsers()
    {
        List<Users> rtnList = new ArrayList();
        rtnList.add(createUser());
        return rtnList;
    }

	public static Users createUser(){
		Users rtnUsers = new Users(userEmail, password);
		return rtnUsers;
	}

    
}