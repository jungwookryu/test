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
	public static final  int userNo = 1 ;
	public static final  String username = "username" ;
	public static final  String userId = "userId" ;
	public static final  String userEmail =  "userEmail" ;
	public static final  String password =  "password";
	public static final  String encryptStr =  "password";
	public static final  String decryptStr =  password;
	public static final  String resetPasswordFile =  "resetPassword.html";
	public static final  String contextUrl =  "test/authPassword";
	
	
	public final static int gatewayNo = 11;
	public final static String model = "model";
	public final static String serial = "serial";
	public final static String targetType = "test";

	public final static String topic = "topic";
	public final static String payload = "payload";
	
	
	public final static int zwaveNo = 21;
	public final static int irNo = 31;

    
	public final static int nodeId = 1;
	public final static int epId0 = 0;
	public final static int epId1 = 0;
	
	
	/**
	 * result 
	 */
	public final static int rtnInt1 = 1;
	
	
	
    //	public static Class anyClass = mock(Class.class);
	public static User users = UsersEntityTestData.createUser();
    public static List<User> lstUsers = UsersEntityTestData.getLstUsers();

}