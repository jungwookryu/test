package com.ht.connected.home.backend.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;
import com.ht.connected.home.backend.user.UserRepository;
import com.ht.connected.home.backend.user.UsersService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommonServiceTest extends MockUtil {

    @Mock
    public UserRepository usersRepository;
    @Mock
    public CrudServiceImpl<Class, Integer> crudImplService;
    @InjectMocks
    public UsersService usersService;

    public static Common common = new Common();

    @Before
    public void setUpMockUtil() {
        Mockito.when(usersRepository.findAll()).thenReturn(lstUsers);
        // Mockito.when(crudImplService.insert(Class.class)).thenReturn(anyClass);
        Mockito.when(crudImplService.getAll()).thenReturn(new ArrayList());
        Mockito.doNothing().when(usersRepository).delete(userNo);
    }

    @Test
    public void sendEmail() throws Exception {
        HashMap map = new HashMap();
        map.put("rtnUsers", users);
        map.put("sFile", resetPasswordFile);
        map.put("contextUrl", contextUrl);
        // common.sendEmail(map);
    }

}
