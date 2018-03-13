/*package com.ht.connected.home.backend.config.service;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/mqttConfig.properties")
public class MqttConfig {
	final static String queueName = "ht-rabbitMQ";

	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("spring-boot-exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	
	 * @Bean public MessageChannel mqttInputChannel() { return new DirectChannel();
	 * }
	 * 
	 * @Bean public MessageProducer inbound() { MqttPahoMessageDrivenChannelAdapter
	 * adapter = new MqttPahoMessageDrivenChannelAdapter("tcp://192.168.2.112:1883",
	 * "guest", "/hcs-w1001/abcd/#", "{\r\n" +
	 * "    \"user_email\": \"k@k.com\",\r\n" + "    \"serialNo\": \"01D0014\",\r\n"
	 * + "    \"nodeId\":6,\r\n" + "    \"endpointId\":0,\r\n" +
	 * "    \"option\":\"none\"\r\n" + "}\r\n" + "");
	 * adapter.setCompletionTimeout(5000); adapter.setConverter(new
	 * DefaultPahoMessageConverter()); adapter.setQos(1);
	 * adapter.setOutputChannel(mqttInputChannel()); return adapter; }
	 * 
	 * @Bean
	 * 
	 * @ServiceActivator(inputChannel = "mqttInputChannel") public MessageHandler
	 * handler() { return new MessageHandler() {
	 * 
	 * @Override public void handleMessage(Message<?> message) throws
	 * MessagingException { System.out.println(message.getPayload()); }
	 * 
	 * }; }
	 
}*/