package com.ht.connected.home.backend.controller.mqtt;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

@Configuration
@EnableRabbit
public class RabbitConfiguration {
    String activemqUser = "iot2";
    String activemqPassword = "@iot@";
    String activemqPoolEnable = "true";
    int activemqPoolMaxConnections = 50;
    int activemqPoolColseTimeout = 1000;
    String activemqZwaveCert = "certi";
    String activemqQueueName = "amqp";
    String activemqExchangeQueueName = "amq.topic";
    String springMqttChannelServer = "/host/#";
    public static final String LOG = "rabbitmqlog";
    private static final Logger logger = LoggerFactory.getLogger(RabbitConfiguration.class);
    @Bean
    public Queue queue() {
        Queue queue =new Queue (activemqQueueName, false) ;
        return queue;
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(activemqExchangeQueueName);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(springMqttChannelServer);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

//    @Bean
//    public SimpleMessageListenerContainer messageListenerContainer() {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
//        container.setQueues(queue());
//        container.setMessageListener(exampleListener());
//        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
//        return container;
//    }

    @Bean
    public MessageListener exampleListener() {
        return new MessageListener() {
            @Override
            public void onMessage(Message message) {
                logger.info("received: " + message);
            }
        };

    }
    @Bean
    public ConnectionFactory connectionFactory() {
        String springMqttActualHost = "iot.testing.htiotservice.com";
        int springMqttActualPort = 5673;
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(springMqttActualHost);
        connectionFactory.setPort(springMqttActualPort);
        connectionFactory.setUsername("iot3");
        connectionFactory.setPassword("@iot@");
        return connectionFactory;
    }
//    @Bean
//    public CachingConnectionFactory connectionFactory() {
//        String springMqttActualHost = "iot.testing.htiotservice.com";
//        int springMqttActualPort = 5673;
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(springMqttActualHost, springMqttActualPort);
//        connectionFactory.setUsername("iot3");
//        connectionFactory.setPassword("@iot@");
//        connectionFactory.setPublisherConfirms(true);
//        return connectionFactory;
//    }
    
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        String springMqttActualHost = "iot.testing.htiotservice.com";
//        int springMqttActualPort = 5673;
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setHost(springMqttActualHost);
//        connectionFactory.setPort(springMqttActualPort);
//        connectionFactory.setUsername("iot3");
//        connectionFactory.setPassword("@iot@");
//        return connectionFactory;
//    }


    
    @Bean
    Channel zwaveChannel() throws IOException, TimeoutException {
        Connection newConnection = connectionFactory().newConnection();
        Channel zwaveChannel = newConnection.createChannel();
        
//        zwaveChannel.queueDeclare(QUEUE_NAME, false, false, false, null);
        zwaveChannel.queueBind(activemqQueueName, activemqExchangeQueueName, ".server.#");
//      
//          zwaveChannel.basicConsume(activemqQueueName, true,  new DefaultConsumer(zwaveChannel) {
//              @Override
//              public void handleDelivery(String consumerTag,
//                                         Envelope envelope,
//                                         AMQP.BasicProperties properties,
//                                         byte[] body)
//                  throws IOException
//              {
//                  String routingKey = envelope.getRoutingKey();
//                  String contentType = properties.getContentType();
//                  long deliveryTag = envelope.getDeliveryTag();
//                  // (process the message components here ...)
//                  zwaveChannel.basicAck(deliveryTag, false);
//              }
//          });
//          new MessageListener() {
//              @Override
//              public void onMessage(Message message) {
//                  logger.info("received: " + message);
//              }
//          };

        zwaveChannel.close();
        newConnection.close();
        return zwaveChannel;
    }
    
    
    
    
    
    

}
