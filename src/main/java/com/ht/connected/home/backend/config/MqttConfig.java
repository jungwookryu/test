package com.ht.connected.home.backend.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.repository.GateWayRepository;

@Configuration
@PropertySource("classpath:mqtt.properties")
public class MqttConfig {

	private static final Log LOGGER = LogFactory.getLog(MqttConfig.class);

	@Value("${spring.mqtt.broker-url}")
	String springMqttBrokerUrl;
	@Value("${spring.mqtt.user}")
	String springMqttUser;
	@Value("${spring.mqtt.password}")
	String springMqttPassword;
	@Value("${spring.mqtt.client-id-prefix}")
	String springMqttClientIdPrefix;
	@Value("${spring.mqtt.channel.gateway}")
	String springMqttChannelGateway;
	@Value("${spring.mqtt.channel.wallpad}")
	String springMqttChannelWallPad;
	@Value("${spring.mqtt.channel.server}")
	String springMqttChannelServer;

	@Autowired
	private BeanFactory beanFactory;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private GateWayRepository gatewayRepository;

	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setServerURIs(springMqttBrokerUrl);
		factory.setUserName(springMqttUser);
		factory.setPassword(springMqttPassword);
		return factory;
	}

	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer MqttInbound() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
				springMqttClientIdPrefix + System.nanoTime(), mqttClientFactory(), springMqttChannelServer);
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel")
	public MessageHandler MqttOutbound() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(springMqttClientIdPrefix + System.nanoTime(),
				mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic("/hcs-w1001");
		return messageHandler;
	}

	@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
	public interface MqttGateway {
		void sendToMqtt(String data);
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {
		return new MessageHandler() {

			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				String topic = String.valueOf(message.getHeaders().get("mqtt_topic"));
				String payload = String.valueOf(message.getPayload());
				LOGGER.info("messageArrived: Topic=" + topic + ", Payload=" + payload);
			}

		};
	}

}
