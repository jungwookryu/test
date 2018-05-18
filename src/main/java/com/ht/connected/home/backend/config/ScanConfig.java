package com.ht.connected.home.backend.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EntityScan({ "com.ht.connected.home.backend.model.entity", "com.ht.connected.home.backend.sip.message.model.entity",
"com.ht.connected.home.backend.sip.media.model.entity" })
@EnableJpaRepositories({ "com.ht.connected.home.backend.repository",
"com.ht.connected.home.backend.sip.message.repository",
"com.ht.connected.home.backend.sip.media.repository" })
public class ScanConfig {
    //TODO 동적 로딩 구현
}
