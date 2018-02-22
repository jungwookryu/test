package com.ht.connected.home.backend.service;

import java.util.ArrayList;
import java.util.List;

import com.ht.connected.home.backend.model.entity.Users;

public final class UsersEntityTestData extends MockUtil
{

    private static String userId = "userId";
    private static String password = "password";


    private UsersEntityTestData() {}

    public static List<Users> getLstUsers()
    {
        List<Users> rtnList = new ArrayList();
        rtnList.add(createScUser());
        return rtnList;
    }

	public static Users createScUser() {
		Users rtnUsers = new Users(userId, password);
		return rtnUsers;
	}

    
}