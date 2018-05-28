package com.ht.connected.home.backend.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ht.connected.home.backend.user.User;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest extends CommonServiceTest{

	
	@Before
	public void setUp() {
		
	}

	@Test
	public void authSendEmail() {
		User rtnT = UsersEntityTestData.createUser();
		assertEquals(users, rtnT);
	}

}
