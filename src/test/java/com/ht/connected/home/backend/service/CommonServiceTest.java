package com.ht.connected.home.backend.service;

import java.util.ArrayList;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.ht.connected.home.backend.repository.UsersRepository;
import com.ht.connected.home.backend.service.impl.base.CrudImplService;

public class CommonServiceTest extends MockUtil{
	
	@Mock
    public UsersRepository usersRepository;
	@Mock
    public CrudImplService<Class, Integer> crudImplService;

	@Before
	public void setUpMockUtil() {
		Mockito.when(usersRepository.findAll()).thenReturn(lstUsers);
//		Mockito.when(crudImplService.insert(Class.class)).thenReturn(anyClass);
		Mockito.when(crudImplService.getAll()).thenReturn(new ArrayList());
		Mockito.doNothing().when(usersRepository).delete(userNo); 		
	 }
	 
}
