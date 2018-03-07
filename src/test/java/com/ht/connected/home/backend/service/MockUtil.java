package com.ht.connected.home.backend.service;

import com.ht.connected.home.backend.model.entity.Users;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
@ActiveProfiles("test")
public class MockUtil 
{

//    @Autowired

//    public static final  PageRequest pageRequest =  new PageRequest(0, 1) ;

    /**
     * service excute end then
     * return List
     */
	public static final  int userNo = 0 ;
	public static final  String username = "username" ;
	public static final  String userId = "userId" ;
	public static final  String userEmail =  "userEmail" ;
	public static final  String password =  "password";
	public static final  String encryptStr =  "password";
	public static final  String decryptStr =  password;
	
//	public static Class anyClass = mock(Class.class);
	public static Users users = UsersEntityTestData.createScUser();
    public static List<Users> lstUsers = UsersEntityTestData.getLstUsers();

}