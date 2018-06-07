package com.ht.connected.home.backend.sip.media.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import com.ht.connected.home.backend.sip.media.service.SipMediaMqttSubscribeService;

/**
 * 스프링 mqtt 설정 클래스
 * 
 * @author 구정화
 */
@Configuration
@PropertySource("classpath:mqtt.properties")
public class SipMediaMqttConfig {

    private static final Log LOGGER = LogFactory.getLog(SipMediaMqttConfig.class);

    @Value("${spring.mqtt.client-id-prefix}")
    String springMqttClientIdPrefix;
    @Value("${spring.mqtt.channel.server.sip.media}")
    String springMqttChannelServer;

    @Autowired
    private SipMediaMqttSubscribeService sipMediaMqttSubscribeService;

    @Autowired
    @Qualifier(value = "mqttClientFactory")
    private DefaultMqttPahoClientFactory sipMediaMqttClientFactory;

    /**
     * subscriber가 사용할 채널
     * 
     * @return
     */
    @Bean
    public MessageChannel sipMediaMqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * Subscriber
     * 
     * @return
     */
    @Bean
    public MessageProducer SIPMediaMqttInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                "SIP" + springMqttClientIdPrefix + System.nanoTime(), sipMediaMqttClientFactory, springMqttChannelServer);
        // adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(sipMediaMqttInputChannel());
        return adapter;
    }

    /**
     * Subscribe 메시지 핸들링
     * 
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "sipMediaMqttInputChannel")
    public MessageHandler sipMediaMqttMessageHandler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                sipMediaMqttSubscribeService.subscribe(message);
            }
        };
    }

}
