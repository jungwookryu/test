package com.ht.connected.home.backend.common;

import com.ht.connected.home.backend.config.MessagesConfig;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class, classes = { MessagesConfig.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageTest {
	
	@Autowired
	@Qualifier("errorMessageSource")
	MessageSource errorMessageSource;
	
	private final Locale defaultLocale = Locale.KOREA;
    

    @Before
    public void init() {
    }
	
	@Test
    public void localeTest_kr() {
		String aaa = localeMessage("test.local.message", defaultLocale);
		String localMessage = errorMessageSource.getMessage("test.local.message", new Object[] {}, defaultLocale);
		Assert.assertEquals(localMessage, "english");
	}
	
	@Test	
	public String localeMessage(String code, Locale locale) {
		return errorMessageSource.getMessage(code, new Object[] {}, locale);
	}
	
}
