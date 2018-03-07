package com.ht.connected.home.backend.config.service;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.ht.connected.home.backend.repository",
        entityManagerFactoryRef = "htEntityManager",
        transactionManagerRef = "htTransactionManager"
)
//@PropertySource("classpath:/application.properties")
abstract class DataSourceConfig {
    @Value("${spring.datasource.ht.driver-class-name}") String htDriverClassName;
    @Value("${spring.datasource.ht.url}") String htUrl;
    @Value("${spring.datasource.ht.username}") String htUsername;
    @Value("${spring.datasource.ht.password}") String htPassword;
    @Value("${spring.jpa.hibernate.ddl-auto}") String ddlAuto;
    @Value("${spring.jpa.hibernate.naming.strategy}") String dialect;

    @Bean(name="htEntityManager")
    public LocalContainerEntityManagerFactoryBean htEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(htDataSource());
        em.setPackagesToScan(new String[] { "com.ht.connected.home.backend" });

        HibernateJpaVendorAdapter vendorAdapter= new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",ddlAuto);
        properties.put("hibernate.dialect",dialect);
        
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name="htDataSource")
    public DataSource htDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(htDriverClassName);
        dataSource.setUrl(htUrl);
        dataSource.setUsername(htUsername);
        dataSource.setPassword(htPassword);

        return dataSource;
    }

    @Bean(name="htTransactionManager")
    public PlatformTransactionManager htTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(htEntityManager().getObject());
        return transactionManager;
    }
}