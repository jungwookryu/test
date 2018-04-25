package com.ht.connected.home.backend.config.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.BeanFactory;
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

import com.ht.connected.home.backend.model.dto.Category;
import com.ht.connected.home.backend.model.dto.MqttMessageArrived;
import com.ht.connected.home.backend.model.dto.Target;
import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.repository.GateWayRepository;
import com.ht.connected.home.backend.service.GateWayService;
import com.ht.connected.home.backend.service.ZwaveService;
import com.ht.connected.home.backend.service.mqtt.MqttNoticeExecutor;
import com.ht.connected.home.backend.service.mqtt.MqttPayloadExecutor;

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
    @Value("${spring.mqtt.certification.topic-segment}")
    String springMqttCertificationTopicSegment;
    @Value("${mqtt.topic.manager.noti}")
    String mqttTopicManagerNoti;
    @Value("${mqtt.topic.category}")
    List<String> mqttTopicCategory;
    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private GateWayRepository gatewayRepository;
    @Autowired
    private ZwaveService zwaveService;
    @Autowired
    private GateWayService gateWayService;

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
    public MessageHandler MqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(springMqttClientIdPrefix + System.nanoTime(),
                mqttClientFactory());
        messageHandler.setAsync(true);
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
                    if (topicSplited.length > 2) {
                        if (Target.server.name().equals(topicSplited[2].toString())) {
                            return;
                        }
                        // gateway service category topicSplited[5].toString()
                        LOGGER.info(topicSplited[5].toString() + " subStart");
                        if (Category.gateway.name().equals(topicSplited[5].toString())) {
                            ZwaveRequest zwaveRequest = new ZwaveRequest(topicSplited);
                            gateWayService.subscribe(zwaveRequest, payload);
                            LOGGER.info("gateway subEnd");
                        }
                        // zwave service
                        if (Category.zwave.name().equals(topicSplited[5].toString())) {
                            ZwaveRequest zwaveRequest = new ZwaveRequest(topicSplited);
                            if ("alive".equals(topicSplited[6])) {
                                LOGGER.info("MQTT alive topic is not implemented");
                            }
                            if (springMqttCertificationTopicSegment.equals(topicSplited[6])) {
                                zwaveService.subscribe(zwaveRequest, payload);
                            }
                        }
                        if (Category.ir.name().equals(topicSplited[5].toString())) {
                            ZwaveRequest zwaveRequest = new ZwaveRequest(topicSplited);
                            LOGGER.info("messageArrived: Topic=" + topic + ", host=");
                            LOGGER.info("ir subEnd");
                        }
                        else {
                            MqttMessageArrived mqttMessageArrived = new MqttMessageArrived(topic, payload);
                            gateWayService.execute(mqttMessageArrived);
                        }
                        LOGGER.info("zwave subEnd");
                    }
                    if (Category.ir.name().equals(topicSplited[5].toString())) {
                        ZwaveRequest zwaveRequest = new ZwaveRequest(topicSplited);
                        LOGGER.info("messageArrived: Topic=" + topic + ", host=");
                        LOGGER.info("ir subEnd");
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    LOGGER.error("zwave :::::: " + e);
                    e.printStackTrace();
                }

            }
            // @SuppressWarnings("unchecked")
            // @Override
            /*  public void handleMessage(Message<?> message) throws MessagingException {
                String topic = String.valueOf(message.getHeaders().get("mqtt_topic"));
                String payload = String.valueOf(message.getPayload());
                LOGGER.info("messageArrived: Topic=" + topic + ", Payload=" + payload);
                String[] topicSplited = topic.split("/");
               
                if (topicSplited.length > 5) {
                   
                   if ( topicSplited[6].equals(springMqttCertificationTopicSegment)) {
                        ZwaveRequest zwaveRequest = new ZwaveRequest(topicSplited);
                        ZwaveBase<Object> handler = new ZwaveBase<>(
                                beanFactory.getBean(ZwaveDefinedHandler.handlers.get(zwaveRequest.getClassKey())));
            
                        handler.subscribe(zwaveRequest, payload);
                    } else {
                        if ("alive".equals(topicSplited[6])) {
                            LOGGER.info("MQTT alive topic is not implemented");
                            
                        } else {
                            MqttMessageArrived mqttMessageArrived = new MqttMessageArrived(topic, payload);
                            Gateway gateway = gatewayRepository.findBySerial(mqttMessageArrived.getSerial());
                            MqttPayloadExecutor executor = getExecutor(mqttMessageArrived);
                            Object returnData = null;
                            if (!isNull(executor)) {
                                LOGGER.info("MQTT message executor found");
                                try {
                                    returnData = executor.execute(mqttMessageArrived, gateway);
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else {
                                LOGGER.info("MQTT main controller message executor not found");
                            }
                            if (!isNull(returnData)) {
                                LOGGER.info("MQTT topic returnData is not null::"+returnData.toString());
                            }
                        }
                    }
                } else {
                    LOGGER.info("MQTT topic is not implemented");
                }
            }*/

            @SuppressWarnings({ "unchecked", "rawtypes" })
            public MqttPayloadExecutor getExecutor(MqttMessageArrived mqttMessageArrived) {
                MqttPayloadExecutor serviceExecutor = null;
                HashMap<String, Class> executors = new HashMap<>();
                executors.put(mqttTopicManagerNoti, MqttNoticeExecutor.class);
                Class executor = executors.get(mqttMessageArrived.getControllerMethodContext());
                if (executor == null) {
                    executor = executors.get(mqttMessageArrived.getControllerMethod());
                }
                if (executor != null) {
                    serviceExecutor = (MqttPayloadExecutor) beanFactory.getBean(executor);
                }
                return serviceExecutor;
            }

        };
    }

}
