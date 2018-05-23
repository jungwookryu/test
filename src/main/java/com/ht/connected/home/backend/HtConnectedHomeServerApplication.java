package com.ht.connected.home.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.ht.connected.home.backend")
@EnableJpaRepositories("com.ht.connected.home.backend")
@EntityScan("com.ht.connected.home.backend")
public class HtConnectedHomeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HtConnectedHomeServerApplication.class, args);
	}
}
