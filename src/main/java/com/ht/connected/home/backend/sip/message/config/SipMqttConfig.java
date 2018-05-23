package com.ht.connected.home.backend.sip.message.config;

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
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.sip.message.service.SipMqttSubscribeService;

/**
 * 스프링 mqtt 설정 클래스
 * 
 * @author 구정화
 */
@Configuration
@PropertySource("classpath:mqtt.properties")
public class SipMqttConfig {

    private static final Log LOGGER = LogFactory.getLog(SipMqttConfig.class);
    @Autowired
    private Environment env;
    @Value("${spring.mqtt.broker-url}")
    String springMqttBrokerUrl;
    @Value("${spring.mqtt.dev.broker-url}")
    String springMqttDevBrokerUrl;
    @Value("${spring.mqtt.local.broker-url}")
    String springMqttLocalBrokerUrl;
    @Value("${spring.mqtt.user}")
    String springMqttUser;
    @Value("${spring.mqtt.password}")
    String springMqttPassword;
    @Value("${spring.mqtt.client-id-prefix}")
    String springMqttClientIdPrefix;
//    @Value("${spring.mqtt.channel.server}")
    String springMqttChannelServer = "/user/#";
    @Value("${spring.mqtt.certification.topic-segment}")
    String springMqttCertificationTopicSegment;
    @Value("${mqtt.topic.manager.noti}")
    String mqttTopicManagerNoti;
    
    @Autowired
    private SipMqttSubscribeService sipMqttSubscribeService;
    
    @Autowired
    private ObjectMapper objectMapper;


    /**
     * MQTT 클라언트 생성
     * 
     * @return
     */
    @Bean
    public MqttPahoClientFactory sipMqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        String sActive = env.getRequiredProperty("spring.profiles.active");
        if (StringUtils.isEmpty(sActive)) {
            sActive = "dev";
        }

        String springMqttBrokerUrlActual = env.getRequiredProperty("spring.mqtt." + sActive + ".broker-url");
        if (StringUtils.isEmpty(sActive)) {
            springMqttBrokerUrlActual = springMqttBrokerUrl;
        }
        factory.setServerURIs(springMqttBrokerUrlActual);
        factory.setUserName(springMqttUser);
        factory.setPassword(springMqttPassword);
        return factory;
    }

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
     * publisher가 사용할 채널
     * 
     * @return
     */
    @Bean
    public MessageChannel sipMqttOutboundChannel() {
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
                "SIP" + springMqttClientIdPrefix + System.nanoTime(), sipMqttClientFactory(), springMqttChannelServer);
//        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(sipMqttInputChannel());
        return adapter;
    }

    /**
     * Publisher
     * 
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "sipMqttOutboundChannel")
    public MqttPahoMessageHandler SIPMqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("SIP" + springMqttClientIdPrefix + System.nanoTime(),
                sipMqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(1);
        messageHandler.setDefaultRetained(false);
        messageHandler.setAsyncEvents(false);
        messageHandler.setDefaultTopic("#");
        return messageHandler;
    }

    /**
     * 메세지 발송 처리
     * 
     * @author 구정화
     */
    @MessagingGateway(defaultRequestChannel = "sipMqttOutboundChannel", name="sipMqttGateway")
    public interface MqttGateway {
        void sendToMqtt(String data);
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
