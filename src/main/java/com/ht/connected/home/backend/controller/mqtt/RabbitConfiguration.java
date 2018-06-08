package com.ht.connected.home.backend.controller.mqtt;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RabbitConfiguration {
    String activemqUser = "iot2";
    String activemqPassword = "@iot@";
    String activemqPoolEnable = "true";
    int activemqPoolMaxConnections = 50;
    int activemqPoolColseTimeout = 1000;
    String activemqZwaveCert = "certi";
    String activemqQueueName = "mqtt";
    String activemqExchangeQueueName = "amq.topic";
    String springMqttChannelServer = "#";
    public static final String LOG = "rabbitmqlog";

    @Bean
    public Queue queue() {
        return new Queue(activemqQueueName, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(activemqExchangeQueueName);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(springMqttChannelServer);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    MessageListenerAdapter listenerAdapter() {
        return new MessageListenerAdapter(new Receiver(), "receiveMessage");
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
//        container.setQueueNames("injeong");
        container.setMessageListener(exampleListener());
        return container;
    }
    
    @Bean
    public MessageListener exampleListener() {
        return new MessageListener() {
            
            public void onMessage(Message message) {
                System.out.println("received: " + message);
            }
        };
          
    }
    

    @Bean
    public CachingConnectionFactory connectionFactory() {
        String springMqttActualHost = "iot.testing.htiotservice.com";
        int springMqttActualPort = 5673;
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(springMqttActualHost, springMqttActualPort);
        connectionFactory.setUsername("iot3");
        connectionFactory.setPassword("@iot@");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }
    
     @Bean
     public RabbitAdmin rabbitAdmin(){
     return new RabbitAdmin(connectionFactory());
     }
    
     @Bean
     @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
     public RabbitTemplate rabbitTemplate() {
     RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
     rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
     return rabbitTemplate;
     }

    @Bean
    Exchange directLogExchange() {
        return ExchangeBuilder.directExchange(LOG).durable(true).build();
    }

}
