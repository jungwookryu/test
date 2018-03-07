package com.ht.connected.home.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ht.connected.home.backend"})
public class HtConnectedHomeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HtConnectedHomeServerApplication.class, args);
	}
}
