package com.ht.connected.home.backend.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CrudImplServiceTest extends CommonServiceTest{


    @Mock
    public
    CrudServiceImpl<Class, Serializable> crudImplService;

    @Before
    public void setUp() {

    }
    
	
	public void insert() {
		
		Class anyClass = mock(Class.class);
		Class rtnT = crudImplService.insert(Class.class);
		assertEquals(anyClass, rtnT);
	}

	@Test
	public void getAll() {
		final Logger logger = LoggerFactory.getLogger(getClass());
		byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
		System.out.println("ivBytes.toString():"+ivBytes.toString());
		logger.info("ivBytes.toString():"+ivBytes.toString());
		logger.debug("ivBytes.toString():"+ivBytes.toString());
//		logger.isTraceEnabled(true);
		List rtnT = crudImplService.getAll();
		assertEquals(rtnT, new ArrayList());
				
	}
	@Test
	public void delete() {
		
		Mockito.doNothing().when(crudImplService).delete(userNo);

	}
		
}
