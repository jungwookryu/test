package com.ht.connected.home.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ht.connected.home.backend.user.User;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public final class UsersEntityTestData extends MockUtil
{


    private UsersEntityTestData() {}

    public static List<User> getLstUsers()
    {
        List<User> rtnList = new ArrayList();
        rtnList.add(createUser());
        return rtnList;
    }

	public static User createUser(){
		User rtnUsers = new User(userEmail, password);
		return rtnUsers;
	}

    
}