
//package com.ht.connected.home.backend.service.impl.zwave;
//
//import java.util.HashMap;
//
//import com.ht.connected.home.backend.config.service.ZwaveClassKey;
//import com.ht.connected.home.backend.service.impl.zwave.handler.Basic;
//import com.ht.connected.home.backend.service.impl.zwave.handler.NetworkManagementInclution;
//import com.ht.connected.home.backend.service.impl.zwave.handler.NetworkManagementProxy;
//
//public class ZwaveDefinedHandler {
//
//    @SuppressWarnings("rawtypes")
//    public static HashMap<Integer, Class> handlers = new HashMap<>();
//
//    static {
//        handlers.put(ZwaveClassKey.NETWORK_MANAGEMENT_PROXY, NetworkManagementProxy.class);
//        handlers.put(ZwaveClassKey.NETWORK_MANAGEMENT_INCLUSION, NetworkManagementInclution.class);
//        handlers.put(ZwaveClassKey.BASIC, Basic.class);
//    }
//
//}
