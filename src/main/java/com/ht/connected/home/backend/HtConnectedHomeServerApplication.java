package com.ht.connected.home.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.Banner;

@SpringBootApplication
public class HtConnectedHomeServerApplication {

	public static void main(String[] args) {
//	    System.out.println(args[0]);
//	    String[] appArgs = {"--debug"};
//	    SpringApplication app = new SpringApplication(HtConnectedHomeServerApplication.class);
//        app.setBannerMode(Banner.Mode.OFF);
//        app.setLogStartupInfo(false);
//        ConfigurableApplicationContext c = app.run(appArgs);
		SpringApplication.run(HtConnectedHomeServerApplication.class, args);
	}
}
