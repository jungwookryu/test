package com.ht.connected.home.backend.common;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.connected.home.backend.controller.mqtt.Message;
import com.ht.connected.home.backend.controller.mqtt.ProducerComponent;
import com.ht.connected.home.backend.service.mqtt.MqttRequest;

/**
 * @author ijlee
 *         <p>
 *         create by 2017.06.21
 */
public class MqttCommon {

    private static Logger logger = LoggerFactory.getLogger(MqttCommon.class);
    
    public final static String STATIC_TARGET = "{target}";
    public final static String STATIC_MODEL = "{model}";
    public final static String STATIC_SERIAL = "{serial}";
    public final static String STATIC_ENDPOINT_NO = "{endpoint_no}";
    public final static String STATIC_SEQUENCE = "{sequence}";
    
    public final static int INT_SOURCE = 1;
    public final static int INT_TARGET = 2;
    public final static int INT_MODEL = 3;
    public final static int INT_SERIAL = 4;
    public final static int INT_CATEGORY = 5;
    /**
     * mqtt publish 토픽 생성
     * @param topicLeadingPath
     * @return
     */
    public static String getMqttPublishTopic(MqttRequest mqttRequest, String target) {
        String topic = "";
        int nodeId = 0;
        int endPointId = 0;
        String category = "zwave";
        String function = "certi";
        
        if (!Objects.isNull(mqttRequest.getNodeId())) {
            nodeId = mqttRequest.getNodeId();
        }
        if (!Objects.isNull(mqttRequest.getEndpointId())) {
            endPointId = mqttRequest.getEndpointId();
        }
        if (!Objects.isNull(mqttRequest.getCategory())) {
            category = mqttRequest.getCategory();
        }
        if (!Objects.isNull(mqttRequest.getFunction())) {
            category = mqttRequest.getFunction();
        }      
        topic = "/server"
                +"/"+ target 
                +"/"+ mqttRequest.getModel()
                +"/"+ mqttRequest.getSerialNo()
                +"/"+ category
                +"/"+ function
                +"/"+ ByteUtil.getHexString(mqttRequest.getClassKey())
                +"/"+ ByteUtil.getHexString(mqttRequest.getCommandKey());
                if(!Common.empty(mqttRequest.getVersion())){
                    topic += "/"+ mqttRequest.getVersion();
                }else {
                        topic += "/v0";
                }
                if(!Common.empty(mqttRequest.getNodeId())){
                    topic += "/"+ ByteUtil.getHexString(nodeId);
                    if(!Common.empty(mqttRequest.getEndpointId())){
                        topic += "/"+ ByteUtil.getHexString(endPointId);
                        if(!Common.empty(mqttRequest.getSecurityOption())){
                            topic += "/"+ mqttRequest.getSecurityOption();
                        }
                    }
                }
        logger.info("====================== ZWAVE PUBLISH TOPIC ======================");
        logger.info(topic);
        return topic;
    }

    public static String getMqttPublishTopic(MqttRequest mqttRequest) {
        String topic = getMqttPublishTopic(mqttRequest, mqttRequest.getTarget());
        return topic;
    }

    public static String rtnCallbackAck(String ack, String target, String model, String serial) {

        ack = (String) Common.isNullrtnByobj(ack, "topic is null");

        if (Objects.nonNull(ack)) {
            ack = ack.replace(STATIC_TARGET, target);
            ack = ack.replace(STATIC_MODEL, model);
            ack = ack.replace(STATIC_SERIAL, serial);
            return ack;
        }
        return ack;
    }

    public static void publish(ProducerComponent producerComponent, Message message) throws InterruptedException {
        logger.info("amqp publish -t "+ message.getMessageType()+" -m "+ message.getMessageBody());
        producerComponent.run(message);

    }

    public static void publishNotificationData(ProducerComponent producerComponent, Properties callbackAckProperties, String sAckPropertyName, String target, String model, String serial,
            Object notiData)
            throws InterruptedException, JsonGenerationException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        String topic = callbackAckProperties.getProperty(sAckPropertyName);
        String exeTopic = MqttCommon.rtnCallbackAck(topic, target, model, serial);
        Message message = new Message(exeTopic, objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(notiData));
        MqttCommon.publish(producerComponent, message);
    }
    
}

