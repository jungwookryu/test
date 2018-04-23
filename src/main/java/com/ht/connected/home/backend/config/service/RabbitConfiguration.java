package com.ht.connected.home.backend.config.service;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.ht.connected.home.backend.model.rabbit.producer.Message;

@Configuration("classpath:/mqttConfig.properties")
@EnableRabbit
public class RabbitConfiguration {

    @Autowired
    private Environment env;
    String activemqHost = env.getRequiredProperty("spring.activemq.host");
    @Value("${spring.activemq.broker-url}")
    String activemqBrokerUrl;
    @Value("${spring.activemq.port}")
    int activemqPort;
    @Value("${spring.activemq.user}")
    String activemqUser;
    @Value("${spring.activemq.password}")
    String activemqPassword;
    @Value("${spring.activemq.pool.enabled}")
    String activemqPoolEnable;
    @Value("${spring.activemq.pool.max-connections}")
    int activemqPoolMaxConnections;
    @Value("${spring.activemq.close-timeout}")
    int activemqPoolColseTimeout;
    @Value("${spring.activemq.zwaveCert}")
    String activemqZwaveCert;
    @Value("${spring.activemq.modelName}")
    String activemqModelName;
    @Value("${spring.activemq.queueName}")
    String activemqQueueName;
    @Value("${spring.activemq.exchange_queueName}")
    String activemqExchangeQueueName;

    //
    // @Value("${spring.activemq.broker-url}")
    // String activemqBrokerUrl;
    // @Value("${spring.activemq.port}")
    // int activemqPort;
    // @Value("${spring.activemq.user}")
    // String activemqUser;
    // @Value("${spring.activemq.password}")
    // String activemqPassword;
    // @Value("${spring.activemq.pool.enabled}")
    // String activemqPoolEnable;
    // @Value("${spring.activemq.pool.max-connections}")
    // int activemqPoolMaxConnections;
    // @Value("${spring.activemq.close-timeout}")
    // int activemqPoolColseTimeout;
    // @Value("${spring.activemq.zwaveCert}")
    // String activemqZwaveCert;
    // @Value("${spring.activemq.modelName}")
    // String activemqModelName;
    // @Value("${spring.activemq.queueName}")
    // String activemqQueueName;
    // @Value("${spring.activemq.exchange_queueName}")
    // String activemqExchangeQueueName;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(activemqQueueName);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleMessageListenerContainer container() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames(activemqQueueName);
        // container.setMessageListener(baseMesage());
        container.setMessageConverter(jsonMessageConverter());
        return container;
    }

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
        return BindingBuilder.bind(queue).to(exchange).with(activemqQueueName);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Message baseMesage() {
        return new Message();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setVirtualHost(activemqHost);
        factory.setUsername(activemqUser);
        factory.setPassword(activemqPassword);
        factory.setPort(activemqPort);
        // factory.setCloseTimeout(activemqPoolColseTimeout);
        // factory.setConnectionLimit(activemqPoolMaxConnections);
        return factory;
    }

}
