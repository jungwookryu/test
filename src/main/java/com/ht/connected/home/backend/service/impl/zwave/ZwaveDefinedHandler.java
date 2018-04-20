package com.ht.connected.home.backend.service.impl.zwave;

import java.util.HashMap;

import com.ht.connected.home.backend.config.service.ZwaveClassKey;
import com.ht.connected.home.backend.service.impl.zwave.handler.Basic;
import com.ht.connected.home.backend.service.impl.zwave.handler.NetworkManagementInclution;
import com.ht.connected.home.backend.service.impl.zwave.handler.NetworkManagementProxy;

public class ZwaveDefinedHandler {

    @SuppressWarnings("rawtypes")
    public static HashMap<String, Class> handlers = new HashMap<>();

    static {
        handlers.put(Integer.toString(ZwaveClassKey.NETWORK_MANAGEMENT_PROXY), NetworkManagementProxy.class);
        handlers.put(Integer.toString(ZwaveClassKey.NETWORK_MANAGEMENT_INCLUSION), NetworkManagementInclution.class);
        handlers.put(Integer.toString(ZwaveClassKey.BASIC), Basic.class);
    }

}
