package com.ht.connected.home.backend.config.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.StringUtils;

/**
 * 스프링 mqtt 설정 클래스
 * @author 구정화
 */
@Configuration
@PropertySource("classpath:mqtt.properties")
public class MqttConfig {

    private static final Log LOGGER = LogFactory.getLog(MqttConfig.class);
    @Autowired
    private Environment env;
    @Value("${spring.mqtt.broker-url}")
    String springMqttBrokerUrl;
    @Value("${spring.mqtt.local.broker-url}")
    String springMqttLocalBrokerUrl;
    @Value("${spring.mqtt.user}")
    String springMqttUser;
    @Value("${spring.mqtt.password}")
    String springMqttPassword;
    @Value("${spring.mqtt.client-id-prefix}")
    String springMqttClientIdPrefix;

    /**
     * MQTT 클라언트 생성
     * @return
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        String sActive = env.getRequiredProperty("spring.profiles.active");
        if (StringUtils.isEmpty(sActive)) {
            sActive = "dev";
        }
        String springMqttBrokerUrlActual = env.getRequiredProperty("spring.mqtt." + sActive + ".broker-url");
        factory.setServerURIs(springMqttBrokerUrlActual);
        factory.setUserName(springMqttUser);
        factory.setPassword(springMqttPassword);
        return factory;
    }

    /**
     * publisher가 사용할 채널
     * @return
     */
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * Publisher
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MqttPahoMessageHandler MqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(springMqttClientIdPrefix + System.nanoTime(),
                mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(1);
        messageHandler.setDefaultRetained(false);
        messageHandler.setAsyncEvents(false);
        messageHandler.setDefaultTopic("/hcs-w1001");
        return messageHandler;
    }

    /**
     * 메세지 발송 처리
     * @author 구정화
     */
    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    public interface MqttGateway {
        void sendToMqtt(String data);
    }

}
