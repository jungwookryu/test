//package com.ht.connected.home.backend.controller.mqtt;
//
//import org.slf4j.Logger;
//import org.springframework.amqp.core.AcknowledgeMode;
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.Exchange;
//import org.springframework.amqp.core.ExchangeBuilder;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.core.TopicExchange;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitAdmin;
//import org.springframework.amqp.rabbit.core.RabbitAdminEvent;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
//import org.springframework.amqp.support.ConsumerTagStrategy;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.beans.factory.config.ConfigurableBeanFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Scope;
//import org.springframework.core.env.Environment;
//import org.springframework.util.StringUtils;
//
//@Configuration("classpath:/mqtt.properties")
//public class RabbitConfiguration {
//    @Autowired
//    private Environment env;
//    @Value("${spring.activemq.user}")
//    String activemqUser;
//    @Value("${spring.activemq.password}")
//    String activemqPassword;
//    @Value("${spring.activemq.pool.enabled}")
//    String activemqPoolEnable;
//    @Value("${spring.activemq.pool.max-connections}")
//    int activemqPoolMaxConnections;
//    @Value("${spring.activemq.close-timeout}")
//    int activemqPoolColseTimeout;
//    @Value("${spring.activemq.zwaveCert}")
//    String activemqZwaveCert;
//    @Value("${spring.activemq.modelName}")
//    String activemqModelName;
//    @Value("${spring.activemq.queueName}")
//    String activemqQueueName;
//    @Value("${spring.activemq.exchange_queueName}")
//    String activemqExchangeQueueName;
//    @Value("${spring.mqtt.channel.server}")
//    String springMqttChannelServer;
//    @Qualifier("consumerTagStrategy")ConsumerTagStrategy consumerTagStrategy;
//    public static final String LOG = "rabbitmqlog";
//
//    @Bean
//    public Queue queue() {
//        return new Queue(activemqQueueName, false);
//    }
//
//    @Bean
//    public TopicExchange exchange() {
//        return new TopicExchange(activemqExchangeQueueName);
//    }
//
//    @Bean
//    public Binding binding(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with(springMqttChannelServer);
//    }
//
//    @Bean
//    public Jackson2JsonMessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    MessageListenerAdapter listenerAdapter(Receiver receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }
//
//    @Bean
//    public SimpleMessageListenerContainer container(Receiver receiver) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory());
//        container.setQueueNames(activemqQueueName);
//        container.setMessageListener(listenerAdapter(receiver));
//        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
//        container.setConsumerTagStrategy(consumerTagStrategy);
//        container.setMessageConverter(jsonMessageConverter());
//        return container;
//    }
//
//    @Bean
//    ConnectionFactory connectionFactory() {
//        String sActive = env.getRequiredProperty("spring.profiles.active");
//        if (StringUtils.isEmpty(sActive)) {
//            sActive = "dev";
//        }
//        String springMqttActualHost = env.getRequiredProperty("spring.activemq." + sActive + ".host");
//        int springMqttActualPort = Integer.parseInt(env.getRequiredProperty("spring.activemq." + sActive + ".port"));
//        CachingConnectionFactory factory = new CachingConnectionFactory();
//        factory.setAddresses(springMqttActualHost+":"+springMqttActualPort);
//        factory.setUsername(activemqUser);
//        factory.setPassword(activemqPassword);
//        factory.setVirtualHost("/");
//        factory.setPort(springMqttActualPort);
//        factory.setPublisherConfirms(true);
//        System.out.println("sActive::"+sActive+"ip:"+springMqttActualHost+",port:"+springMqttActualPort+",username:"+activemqUser+",password:"+activemqPassword);
//        return factory;
//    }
//    
//    @Bean
//    public RabbitAdmin rabbitAdmin(){
//        return new RabbitAdmin(connectionFactory());
//    }
//    
//    @Bean  
//    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)  
//    public RabbitTemplate rabbitTemplate() {  
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());  
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
//        return rabbitTemplate;  
//    } 
//
//    @Bean
//    public Exchange directLogExchange() {
//        return ExchangeBuilder.directExchange(LOG).durable(true).build();
//    }
//
//}
