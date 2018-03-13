//package com.ht.connected.home.backend.common;
//
//import java.util.Locale;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.MessageSource;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
//@Configuration
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//
//public class MessageTest {
//	
//	/*@Autowired
//	@Qualifier("errorMessageSource")
//	MessageSource errorMessageSource;
//	
//	*/
//	
//	@Autowired
//	@Qualifier("messageSource")
//	MessageSource messageSource;
//	
//	private final Locale krLocale = Locale.KOREA;
//	private final Locale enLocale = Locale.ENGLISH;
//    
//
//    @Before
//    public void init() {
//    	 String welcomeMessage = messageSource
//                 .getMessage("welcome", new Object[]{"John", "Spring Message Source"}, Locale.getDefault());
//         System.out.println(welcomeMessage);
//    }
//	
//	@Test
//    public void localeTest_kr() {
//		String aaa = localeMessage("test.local.message", krLocale);
//		String localMessage = messageSource.getMessage("test.local.message", new Object[] {}, krLocale);
//		Assert.assertEquals(localMessage, aaa);
//	}
//	
//	public String localeMessage(String code, Locale locale) {
//		return messageSource.getMessage(code, new Object[] {}, locale);
//	}
//	
//}
