package com.ht.connected.home.backend.config.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Created by ijlee on 2018-02-05.
 */
@PropertySource("classpath:/emailConfig.properties")
@Configuration
public class EmailConfig {
	
    @Autowired
    private Environment env;

    @Bean(name="mailProperties")
    public MailProperties mailProperties() {

        MailProperties mailProperties = new MailProperties();
        mailProperties.setHost(env.getRequiredProperty("mail.smtp.host"));
        mailProperties.setPort(Integer.valueOf(env.getRequiredProperty("mail.smtp.port")));
        mailProperties.setUsername(env.getRequiredProperty("mail.smtp.userEmail"));
        mailProperties.setPassword(env.getRequiredProperty("mail.smtp.password"));
        return mailProperties;
    }

    @Bean(name="mailSender")
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties() .getHost());
        if (mailProperties().getPort() != null) {
            mailSender.setPort(mailProperties().getPort());
        }
        mailSender.setUsername(mailProperties().getUsername());
        mailSender.setPassword(mailProperties().getPassword());
        return mailSender;
    }

    @Bean(name="emailProperties")
    public Properties properties() {
        MailProperties mailProperties = mailProperties();
        Properties props = new Properties();
        // SSL 사용하는 경우
        props.put("mail.smtp.host", mailProperties.getHost()); //SMTP Host
        props.put("mail.smtp.socketFactory.port",mailProperties.getPort()); //SSL Port
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
        props.put("mail.smtp.port",mailProperties.getPort());
        props.put("mail.smtp.auth",env.getRequiredProperty("mail.smtp.properties.auth")); //Enabling SMTP Authentication
        props.put("mail.smtp.maximumTotalQps",env.getRequiredProperty("mail.smtp.properties.maximumTotalQps"));
        props.put("mail.smtp.authUrl",env.getRequiredProperty("mail.smtp.properties.authUrl"));
        props.put("mail.smtp.sFile",env.getRequiredProperty("mail.smtp.properties.sFile"));
        props.put("mail.smtp.subject",env.getRequiredProperty("mail.smtp.properties.subject"));
        props.put("mail.smtp.username",env.getRequiredProperty("mail.smtp.username"));
        props.put("mail.smtp.userEmail",env.getRequiredProperty("mail.smtp.userEmail"));
        props.put("mail.smtp.contextUrl",env.getRequiredProperty("mail.smtp.properties.contextUrl"));
        String sActive = env.getRequiredProperty("spring.profiles.active");
        props.put("mail.smtp.active.authUrl", env.getRequiredProperty("mail.smtp.properties."+sActive+".authUrl")+env.getRequiredProperty("server.port"));
        
        return props;
    }

    // 인증
    @Bean(name="emailAuth")
    public Authenticator auth() {
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                MailProperties mailProperties = mailProperties();
                return new PasswordAuthentication(mailProperties.getUsername(), mailProperties.getPassword());
            }

        };
        return auth;
    }
}