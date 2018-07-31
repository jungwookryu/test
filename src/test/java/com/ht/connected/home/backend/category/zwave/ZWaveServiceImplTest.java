package com.ht.connected.home.backend.category.zwave;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ht.connected.home.backend.category.zwave.ZWaveServiceImpl.status;
import com.ht.connected.home.backend.gateway.GatewayEntityTestData;
import com.ht.connected.home.backend.gateway.GatewayRepository;
import com.ht.connected.home.backend.service.MockUtil;


@RunWith(SpringRunner.class)
public class ZWaveServiceImplTest  extends MockUtil{

    @MockBean
    ZWaveRepository zwaveRepository;
    
    @MockBean
    GatewayRepository gatewayRepository;
    
    @InjectMocks
    ZWaveServiceImpl zWaveService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        /**
         * ZWaveRepository mockito init
         */
        Mockito.when(zwaveRepository.getOne(zwaveNo)).thenReturn(ZWaveEntityTestData.getOneZwave());
        Mockito.when(gatewayRepository.getOne(gatewayNo)).thenReturn(GatewayEntityTestData.getOne());
        Mockito.when(zwaveRepository.setFixedStatusForNo(status.delete.name(), zwaveNo)).thenReturn(rtnInt1);
//        Mockito.doNothing().when(messageHandler).setDefaultTopic(topic);
//        Mockito.doNothing().when(mqttGateway).sendToMqtt(payload);
//        Mockito.when(zwaveServiceImpl.deleteByNo(zwaveNo)).thenReturn(rtnInt1);
    }

    @Test
    public void deleteByNo() throws JsonProcessingException, InterruptedException {
        int expectedResult = 1;
        int result = zWaveService.deleteByNo(zwaveNo);
        Assert.assertEquals(expectedResult, result);
    }

}
