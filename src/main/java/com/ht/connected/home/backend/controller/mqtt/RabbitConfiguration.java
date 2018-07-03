package com.ht.connected.home.backend.controller.mqtt;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
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
    String springMqttChannelServer = "a/a/#";
    public static final String LOG = "rabbitmqlog";
    private static final Logger logger = LoggerFactory.getLogger(RabbitConfiguration.class);

    @Autowired
    ConsumerListener consumerListener;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setExchange(activemqExchangeQueueName);
        return template;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        String springMqttActualHost = "iot.dev.htiotservice.com";
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(springMqttActualHost, 5673);
        connectionFactory.setUsername(activemqUser);
        connectionFactory.setPassword(activemqPassword);
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    public Queue queue3() {
        Queue queue = new Queue("ijlee", false, false, true);
        return queue;
    }
//
//    @Bean
//    public TopicExchange exchange() {
//        return new TopicExchange(activemqExchangeQueueName);
//    }
//
//    @Bean
//    public Binding binding() throws IOException, TimeoutException {
//        // channel().queueBind(activemqExchangeQueueName, activemqQueueName, springMqttChannelServer);
//        return BindingBuilder.bind(queue()).to(exchange()).with(springMqttChannelServer);
//    }

//    @Bean
//    public Jackson2JsonMessageConverter jsonMessageConverter() {
//        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
////        converter.setClassMapper(classMapper());
//        return converter;
//    }
//
//    @Bean
//    public DefaultClassMapper classMapper() {
//        DefaultClassMapper typeMapper = new DefaultClassMapper();
//        typeMapper.setDefaultType(Message.class);
//        return typeMapper;
//    }

//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(activemqQueueName);
//        container.setMessageListener(listenerAdapter());
//        return container;
//    }


//    @Bean
//    MessageListenerAdapter listenerAdapter() {
//        MessageListenerAdapter adapter = new MessageListenerAdapter(consumerListener, "onMessage");
//       // adapter.setMessageConverter(jsonMessageConverter());
//        return adapter;
//    }

    // @Bean
    // public SimpleMessageListenerContainer messageListenerContainer() {
    // SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
    // container.setQueues(queue());
    // container.setMessageListener(exampleListener());
    // container.setAcknowledgeMode(AcknowledgeMode.AUTO);
    // return container;
    // }

    // @Bean
    // Channel zwaveChannel() throws IOException, TimeoutException {
    // Connection newConnection = connectionFactory().newConnection();
    // Channel zwaveChannel = newConnection.createChannel();

    // zwaveChannel.queueDeclare(QUEUE_NAME, false, false, false, null);
    // zwaveChannel.queueBind(activemqQueueName, activemqExchangeQueueName, ".server.#");
    //
    // zwaveChannel.basicConsume(activemqQueueName, true, new DefaultConsumer(zwaveChannel) {
    // @Override
    // public void handleDelivery(String consumerTag,
    // Envelope envelope,
    // AMQP.BasicProperties properties,
    // byte[] body)
    // throws IOException
    // {
    // String routingKey = envelope.getRoutingKey();
    // String contentType = properties.getContentType();
    // long deliveryTag = envelope.getDeliveryTag();
    // // (process the message components here ...)
    // zwaveChannel.basicAck(deliveryTag, false);
    // }
    // });
    // new MessageListener() {
    // @Override
    // public void onMessage(Message message) {
    // logger.info("received: " + message);
    // }
    // };

    // zwaveChannel.close();
    // newConnection.close();
    // return zwaveChannel;
    // }

}
