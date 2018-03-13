/*package com.ht.connected.home.backend.config.service;

import java.util.HashMap;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
//@PropertySource("classpath:application.properties")
public class DataSourceConfig{
//    @Value("${spring.datasource.ht.driver-class-name}") String htDriverClassName;
//    @Value("${spring.datasource.ht.url}") String htUrl;
//    @Value("${spring.datasource.ht.username}") String htUsername;
//    @Value("${spring.datasource.ht.password}") String htPassword;
//    @Value("${spring.jpa.hibernate.ddl-auto}") String ddlAuto;
//    @Value("${spring.jpa.hibernate.naming.strategy}") String dialect;
    
    String htDriverClassName="com.mysql.jdbc.Driver";
    String htUrl="jdbc:mysql://192.168.2.112:3306/HT_IOT_CNND_HOME?characterEncoding=utf8&useSSL=false";
    String htUsername="root";
    String htPassword="apart0617";
    String ddlAuto="none";
    String dialect="org.hibernate.cfg.EJB3NamingStrategy";
    
    @Bean
    public abstract DataSource dataSource();
    
    @Bean(name="htEntityManager")
    public LocalContainerEntityManagerFactoryBean htEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(htDataSource());
        em.setPackagesToScan(new String[] { "com.ht.connected.home.backend" });

        HibernateJpaVendorAdapter vendorAdapter= new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",ddlAuto);
//        properties.put("hibernate.dialect",dialect);
        
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name="htDataSource")
    public DataSource htDataSource() {
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName(htDriverClassName);
        dataSource.setUrl(htUrl);
        dataSource.setUsername(htUsername);
        dataSource.setPassword(htPassword);

        return dataSource;
    }

    @Bean(name="htTransactionManager")
    public PlatformTransactionManager htTransactionManager(@Qualifier("htDataSource") DataSource htDataSource) {
    	 DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(htDataSource);
         transactionManager.setGlobalRollbackOnParticipationFailure(false);
         return transactionManager;
  }
}*/