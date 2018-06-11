package com.ht.connected.home.backend.sip.message.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.ht.connected.home.backend.sip.message.service.SipMqttSubscribeService;

/**
 * 스프링 mqtt 설정 클래스
 * 
 * @author 구정화
 */
@Configuration
@PropertySource("classpath:mqtt.properties")
public class SipMqttConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SipMqttConfig.class);

    @Value("${spring.mqtt.client-id-prefix}")
    String springMqttClientIdPrefix;
    @Value("${spring.mqtt.channel.server.sip}")
    String springMqttChannelServer;

    @Autowired
    private SipMqttSubscribeService sipMqttSubscribeService;

    @Autowired
    @Qualifier(value = "mqttClientFactory")
    private DefaultMqttPahoClientFactory sipMqttClientFactory;

    /**
     * subscriber가 사용할 채널
     * 
     * @return
     */
    @Bean
    public MessageChannel sipMqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * Subscriber
     * 
     * @return
     */
    @Bean
    public MessageProducer SIPMqttInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                "SIP" + springMqttClientIdPrefix + System.nanoTime(), sipMqttClientFactory, springMqttChannelServer);
        // adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(sipMqttInputChannel());
        return adapter;
    }

    /**
     * Subscribe 메시지 핸들링
     * 
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "sipMqttInputChannel")
    public MessageHandler sipMqttMessageHandler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                sipMqttSubscribeService.subscribe(message);
            }
        };
    }

}
