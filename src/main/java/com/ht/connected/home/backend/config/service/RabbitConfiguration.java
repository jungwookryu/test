package com.ht.connected.home.backend.config.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.StringUtils;

import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.controller.mqtt.ConsumerListener;

@Configuration
@PropertySource("classpath:mqtt.properties")
@EnableRabbit
public class RabbitConfiguration {
    
    @Value("${spring.activemq.channel.local.server}")
    String springMqttChannelServer;
    
    @Value("${spring.activemq.queueName}")
    private String activemqQueueName;
    
    //amq.topic
    @Value("${spring.activemq.exchange.queueName}")
    String activemqExchangeQueueName;

    @Value("${spring.activemq.user}")
    String activemqUser;

    @Value("${spring.activemq.password}")
    String activemqPassword;

    @Autowired
    ConsumerListener consumerListener;
    
    @Autowired
    private Environment env;
    
    public static final String LOG = "rabbitmqlog";
    private static final Logger logger = LoggerFactory.getLogger(RabbitConfiguration.class);


    @Bean
    public Queue queue(AmqpAdmin amqpAdmin) {
        Queue queue = new Queue(activemqQueueName, false);
        return queue;
    }

    @Bean
    public TopicExchange exchange(AmqpAdmin amqpAdmin) {
        TopicExchange topicExchange = new TopicExchange(activemqExchangeQueueName);
        amqpAdmin.declareExchange(topicExchange);
        return topicExchange;
    }

    @Bean
    Binding binding(AmqpAdmin amqpAdmin, Queue queue, TopicExchange exchange) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(springMqttChannelServer);
        amqpAdmin.declareBinding(binding);
        return binding;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory, MessageListenerAdapter exampleListener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(activemqQueueName);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return container;
    }

    @Bean
    public MessageChannel amqpInputChannel() {
        DirectChannel chanel = new DirectChannel();
        return chanel;
    }

    @Bean
    public MessageChannel amqpOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public AmqpInboundChannelAdapter inbound(SimpleMessageListenerContainer messageListenerContainer,
            @Qualifier("amqpInputChannel") MessageChannel channel) {
        AmqpInboundChannelAdapter adapter = new AmqpInboundChannelAdapter(messageListenerContainer);
        adapter.setOutputChannel(channel);
        return adapter;
    }

    @Bean
    public MessageListenerAdapter exampleListener(ConsumerListener consumerListener) {
        return new MessageListenerAdapter(consumerListener, "receiveMessage");
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        String sActive = env.getRequiredProperty("spring.profiles.active");
        if (StringUtils.isEmpty(sActive)) {
            sActive = "dev";
        }
        String springMqttActualHost = env.getRequiredProperty("spring.activemq." + sActive + ".host");
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(springMqttActualHost, 5673);
        connectionFactory.setUsername(activemqUser);
        connectionFactory.setPassword(activemqPassword);
        return connectionFactory;
    }

    @Bean
    AmqpAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    @ServiceActivator(inputChannel = "amqpInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) {
                try {
                    logger.info(message.toString());
                    MessageHeaders messageHeaders = message.getHeaders();
                    String topic = (String) messageHeaders.getOrDefault(AmqpHeaders.RECEIVED_ROUTING_KEY,"");
                    ObjectMapper opjectMapper = new ObjectMapper();
                    if (Common.notEmpty(topic)) {
                        Object payload = message.getPayload();
                        String sPayLoad;
                        sPayLoad = opjectMapper.writeValueAsString(payload);
                        com.ht.connected.home.backend.controller.mqtt.Message returnMessage = new com.ht.connected.home.backend.controller.mqtt.Message(topic, sPayLoad);
                        consumerListener.receiveMessage(returnMessage);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
    }
}
