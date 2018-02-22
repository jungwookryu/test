package com.ht.connected.home.backend.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.ht.connected.home.front.repository")
@EntityScan("com.ht.connected.home.fornt.model.entity")
@ComponentScan(basePackages = "com.ht.connected.home.front")
public class HtConnectedHomeBackendConfig {
}

