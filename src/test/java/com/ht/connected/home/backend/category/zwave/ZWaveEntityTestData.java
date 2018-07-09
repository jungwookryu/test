package com.ht.connected.home.backend.category.zwave;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ht.connected.home.backend.service.MockUtil;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public final class ZWaveEntityTestData extends MockUtil
{


    private ZWaveEntityTestData() {}

    public static List<ZWave> getLstZWave()
    {
        List<ZWave> rtnList = new ArrayList();
        rtnList.add(createZwave());
        return rtnList;
    }

	public static ZWave createZwave(){
	    ZWave zwave = new ZWave();
	    zwave.setNo(zwaveNo);
	    zwave.setGatewayNo(gatewayNo);
	    zwave.setNodeId(nodeId);
		return zwave;
	}

	   public static ZWave getOneZwave(){
	        ZWave zwave = new ZWave();
	        zwave.setNo(zwaveNo);
	        zwave.setGatewayNo(gatewayNo);
	        zwave.setNodeId(nodeId);
	        return zwave;
	    }
    
}

