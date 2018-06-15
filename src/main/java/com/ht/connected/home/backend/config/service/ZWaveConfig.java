package com.ht.connected.home.backend.config.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ijlee on 2018-02-05.
 */
@Configuration
public class ZWaveConfig {
	
    @Bean(name="zWaveProperties")
    public Properties zWaveProperties() throws IOException {
        Properties zwaveNicknames =new Properties();
        String propFile = "zwave/zwave-assigned-nickname.properties";
        InputStream filePath =getClass().getClassLoader().getResourceAsStream(propFile);
        zwaveNicknames.load(filePath);
        return zwaveNicknames;
    }

}