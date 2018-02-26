package com.ht.connected.home.backend.common;

import java.util.Properties;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@EnableAutoConfiguration
public class PropertiesTest {
	
	@Autowired
	@Qualifier(value="errorMessageSource")
	Properties props = new Properties();
	
	public void propertiesTest() {
		
		String filename = "settings.properties";
		props.load(PropertiesTest.class.getClassLoader().getResources(name));
		System.out.println(props.getProperty("driver"));
	}
}
