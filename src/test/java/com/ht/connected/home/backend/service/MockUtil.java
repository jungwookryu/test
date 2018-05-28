package com.ht.connected.home.backend.service;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.ht.connected.home.backend.user.User;


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
	public static final  String resetPasswordFile =  "resetPassword.html";
	public static final  String contextUrl =  "test/authPassword";
	
//	public static Class anyClass = mock(Class.class);
	public static User users = UsersEntityTestData.createUser();
    public static List<User> lstUsers = UsersEntityTestData.getLstUsers();

}