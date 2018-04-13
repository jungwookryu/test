package com.ht.connected.home.backend.config.service;

import static java.util.Objects.isNull;

import java.util.HashMap;

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

import com.ht.connected.home.backend.model.dto.MqttMessageArrived;
import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.model.entity.Gateway;
import com.ht.connected.home.backend.repository.GateWayRepository;
import com.ht.connected.home.backend.service.base.ZwaveBase;
import com.ht.connected.home.backend.service.impl.zwave.ZwaveDefinedHandler;
import com.ht.connected.home.backend.service.mqtt.MqttNoticeExecutor;
import com.ht.connected.home.backend.service.mqtt.MqttPayloadExecutor;

/**
 * 스프링 mqtt 설정 클래스
 * 
 * @author 구정화
 *
 */
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
    @Value("${spring.mqtt.channel.server}")
    String springMqttChannelServer;
    @Value("${spring.mqtt.certification.topic-segment}")
    String springMqttCertificationTopicSegment;

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private GateWayRepository gatewayRepository;

    @SuppressWarnings("rawtypes")
    static private HashMap<String, Class> executors = new HashMap<>();

    static {
        executors.put("app/manager/noti", MqttNoticeExecutor.class);
    }

    /**
     * MQTT 클라언트 생성
     * 
     * @return
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setServerURIs(springMqttBrokerUrl);
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
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * publisher가 사용할 채널
     * 
     * @return
     */
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * Subscriber
     * 
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
     * 
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
     * 
     * @author 구정화
     *
     */
    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    public interface MqttGateway {
        void sendToMqtt(String data);
    }

    /**
     * Subscribe 메시지 핸들링
     * 
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {

            @SuppressWarnings("unchecked")
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = String.valueOf(message.getHeaders().get("mqtt_topic"));
                String payload = String.valueOf(message.getPayload());
                LOGGER.info("messageArrived: Topic=" + topic + ", Payload=" + payload);
                String[] topicSplited = topic.split("/");
                if (topicSplited.length > 5) {
                    if (topicSplited[6].equals(springMqttCertificationTopicSegment)) {
                        ZwaveRequest zwaveRequest = new ZwaveRequest(topicSplited);
                        ZwaveBase<Object> handler = new ZwaveBase<>(
                                beanFactory.getBean(ZwaveDefinedHandler.handlers.get(zwaveRequest.getClassKey())));

                        handler.subscribe(zwaveRequest, payload);
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

                        }
                    }
                } else {
                    LOGGER.info("MQTT topic is not implemented");
                }
            }

            @SuppressWarnings({ "unchecked", "rawtypes" })
            public MqttPayloadExecutor getExecutor(MqttMessageArrived mqttMessageArrived) {
                MqttPayloadExecutor serviceExecutor = null;
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
