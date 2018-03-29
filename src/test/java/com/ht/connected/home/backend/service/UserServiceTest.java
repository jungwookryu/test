package com.ht.connected.home.backend.service;

import static org.junit.Assert.assertEquals;

import com.ht.connected.home.backend.model.entity.Users;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest extends CommonServiceTest{

	
	@Before
	public void setUp() {
		
	}

	@Test
	public void authSendEmail() {
		Users rtnT = UsersEntityTestData.createUser();
		assertEquals(users, rtnT);
	}

}
