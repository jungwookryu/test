//package com.ht.connected.home.backend.config.service;
//
//
//import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//static class RabbitConfiguration {
//
//
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
//        connectionFactory.setUsername("guest");
//        connectionFactory.setPassword("guest");
//        return connectionFactory;
//    }
//	
//	@Bean
//	public SimpleRabbitListenerContainerFactory myFactory(
//			SimpleRabbitListenerContainerFactoryConfigurer configurer) {
//		SimpleRabbitListenerContainerFactory factory =
//				new SimpleRabbitListenerContainerFactory();
//		configurer.configure(factory, connectionFactory);
//		factory.setMessageConverter(myMessageConverter());
//		return factory;
//	}
//
//}