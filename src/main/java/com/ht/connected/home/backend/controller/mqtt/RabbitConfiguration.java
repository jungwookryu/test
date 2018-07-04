package com.ht.connected.home.backend.controller.mqtt;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableRabbit
public class RabbitConfiguration {
    String activemqUser = "iot4";
    String activemqPassword = "@iot@";
    String activemqPoolEnable = "true";
    int activemqPoolMaxConnections = 50;
    int activemqPoolColseTimeout = 1000;
    String activemqZwaveCert = "certi";
    String activemqQueueName = "amqp";
    String activemqExchangeQueueName = "amq.topic";
    String springMqttChannelServer = "/a/#";
    String springActivemqQueueName = "htConnectedServerQueue1";
    
    public static final String LOG = "rabbitmqlog";
    
    private static final Logger logger = LoggerFactory.getLogger(RabbitConfiguration.class);

    @Autowired
    ConsumerListener consumerListener;
    
    public void configureRabbitTemplate(RabbitTemplate rabbitTemplate) {
        rabbitTemplate.setExchange(activemqExchangeQueueName);      
    }

    @Bean
    public Queue queue() {
        Queue queue =new Queue (springActivemqQueueName, false) ;
        return queue;
    }

    
    @Bean
    public AmqpTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(jsonMessageConverter());
        configureRabbitTemplate(template);
        return template;
    }

    @Bean
    SimpleMessageListenerContainer container() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setQueues(queue());
        container.setConnectionFactory(connectionFactory());
        container.setMessageListener(listenerAdapter());
        return container;
    }
    
    @Bean
    public ConnectionFactory connectionFactory() {
        String springMqttActualHost = "iot.dev.htiotservice.com";
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(springMqttActualHost, 5673);
        connectionFactory.setUsername(activemqUser);
        connectionFactory.setPassword(activemqPassword);
//        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(activemqExchangeQueueName);
    }

     @Bean
     public Jackson2JsonMessageConverter jsonMessageConverter() {
     Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
    // converter.setClassMapper(classMapper());
     return converter;
     }
     @Bean
     public Binding binding() throws IOException, TimeoutException {
//         channel().queueBind(activemqExchangeQueueName, activemqQueueName, springMqttChannelServer);
         return BindingBuilder.bind(queue()).to(exchange()).with(springMqttChannelServer);
     }
    // @Bean
    // public DefaultClassMapper classMapper() {
    // DefaultClassMapper typeMapper = new DefaultClassMapper();
    // typeMapper.setDefaultType(Message.class);
    // return typeMapper;
    // }


    @Bean
    MessageListenerAdapter listenerAdapter() {
        MessageListenerAdapter adapter = new MessageListenerAdapter(consumerListener, "onMessage");
        // adapter.setMessageConverter(jsonMessageConverter());
        return adapter;
    }



}
