package com.ht.connected.home.backend.category.zwave.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ZWaveTypeProperties {

    public Properties zwaveType() throws IOException {
        InputStream typePath = getClass().getClassLoader().getResourceAsStream("zwave-plus-assigned-icon-types");
        Properties typePathProp = new Properties();
        typePathProp.load(typePath);
        return typePathProp;
    }
}
