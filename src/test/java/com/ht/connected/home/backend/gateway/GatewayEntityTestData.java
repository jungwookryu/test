package com.ht.connected.home.backend.gateway;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ht.connected.home.backend.service.MockUtil;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public final class GatewayEntityTestData extends MockUtil
{


    private GatewayEntityTestData() {}
    public static List<Gateway> getLstZWave()
    {
        List<Gateway> rtnList = new ArrayList();
        rtnList.add(createGateway());
        return rtnList;
    }

    public static Gateway createGateway(){
	    Gateway gateway = new Gateway();
	    gateway.setNo(gatewayNo);
		return gateway;
	}
    
    public static Gateway getOne(){
       Gateway gateway = new Gateway();
       gateway.setNo(gatewayNo);
       gateway.setSerial(serial);
       gateway.setModel(model);
       gateway.setTargetType(targetType);
       return gateway;
   }
    
}