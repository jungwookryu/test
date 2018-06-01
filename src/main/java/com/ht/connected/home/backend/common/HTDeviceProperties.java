package com.ht.connected.home.backend.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.springframework.context.annotation.Configuration;

@Configuration
public class HTDeviceProperties {

    public Properties zwaveProperties() throws IOException {
        InputStream typePath = getClass().getClassLoader().getResourceAsStream("zwave-plus-assigned-icon-types");
        Properties typePathProp = new Properties();
        typePathProp.load(typePath);
        return typePathProp;
    }
    
//    public Properties irProperties() throws IOException {
//        InputStream typePath = getClass().getClassLoader().getResourceAsStream("ir-plus-assigned-types");
//        Properties typePathProp = new Properties();
//        typePathProp.load(typePath);
//        
//        List devicetype = typePathProp.stringPropertyNames();
//        return typePathProp;
//    }
}
