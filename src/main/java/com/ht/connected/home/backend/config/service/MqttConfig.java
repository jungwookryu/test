package com.ht.connected.home.backend.config.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
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

import com.ht.connected.home.backend.category.ir.IRService;
import com.ht.connected.home.backend.category.zwave.ZWaveRequest;
import com.ht.connected.home.backend.category.zwave.ZWaveService;
import com.ht.connected.home.backend.gateway.Gateway;
import com.ht.connected.home.backend.gateway.GatewayService;
import com.ht.connected.home.backend.gatewayCategory.CategoryActive;
import com.ht.connected.home.backend.service.mqtt.Target;

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
    @Value("${spring.mqtt.channel.server}")
    String springMqttChannelServer;
    @Value("${mqtt.topic.manager.noti}")
    String mqttTopicManagerNoti;

    @Autowired
    private ZWaveService zwaveService;
    @Autowired
    private GatewayService gateWayService;

    @Autowired
    private IRService irService;

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
     * subscriber가 사용할 채널
     * @return
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
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
     * Subscriber
     * @return
     */
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

    /**
     * Subscribe 메시지 핸들링
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = String.valueOf(message.getHeaders().get("mqtt_topic"));
                String payload = String.valueOf(message.getPayload());
                LOGGER.info("messageArrived: Topic=" + topic + ", Payload=" + payload);
                String[] topicSplited = topic.split("/");
                try {
                    //message topic 4개이상이어야 gateway관련 메세지임.
                    if (topicSplited.length > 4) {
                        Gateway gateway = new Gateway();
                        if (4 <= topicSplited.length) {
                            gateway = new Gateway(topicSplited[3].toString(), topicSplited[4].toString());
                        }
                        // 서버에서 보낸것이 아닐경우만 subscribe함.
                        if (!Target.server.name().equals(topicSplited[1].toString())) {
                            LOGGER.info(topicSplited[5].toString() + " subStart");
                            if (CategoryActive.gateway.manager.name().equals(topicSplited[5].toString())) {
                                gateWayService.subscribe(topic, payload);
                            }
                            // zwave service
                            if (CategoryActive.gateway.zwave.name().equals(topicSplited[5].toString())) {
                                ZWaveRequest zwaveRequest = new ZWaveRequest(topicSplited);
                                if (CategoryActive.zwave.certi.name().equals(topicSplited[6].toString())) {
                                    
                                    zwaveService.subscribe(zwaveRequest, payload);
                                }
                                if (CategoryActive.zwave.init.name().equals(topicSplited[6].toString())) {
                                    zwaveService.subscribeInit(gateway);
                                }
                            }
                            if (CategoryActive.gateway.ir.name().equals(topicSplited[5].toString().trim())) {
                                irService.subscribe(topicSplited, payload);
                            }
                        }
                        String mqttLog ="host :: category ::";
                        if (topicSplited.length>5) {
                            mqttLog += topicSplited[5].toString();
                        }
                        if (topicSplited.length>6) {
                            mqttLog += "active ::" + topicSplited[6].toString() + " subEnd";
                        }
                        // TODO category가 정해지지 않은 mqtt에대한 로직이 생길경우 .else에 대한 로직을 넣을 예정
                        LOGGER.info(mqttLog);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    LOGGER.error("Mqtt :::::: " + e);
                }

            }

        };
    }

}
