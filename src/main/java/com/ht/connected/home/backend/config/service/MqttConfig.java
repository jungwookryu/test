package com.ht.connected.home.backend.config.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Configuration
@PropertySource("classpath:/mqttConfig.properties")
public class MqttConfig {

	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inbound() {
		MqttPahoMessageDrivenChannelAdapter adapter = 
				new MqttPahoMessageDrivenChannelAdapter("tcp://192.168.2.112:1883",
				"guest", "/hcs-w1001/abcd/#", "{\r\n" + 
						"    \"user_email\": \"k@k.com\",\r\n" + 
						"    \"serialNo\": \"01D0014\",\r\n" + 
						"    \"nodeId\":6,\r\n" + 
						"    \"endpointId\":0,\r\n" + 
						"    \"option\":\"none\"\r\n" + 
						"}\r\n" + 
						"");
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {
		return new MessageHandler() {

			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				System.out.println(message.getPayload());
			}

		};
	}
}