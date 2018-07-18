package com.ht.connected.home.backend.common;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

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
    public static String STATIC_TARGET = "{target}";
    public static String STATIC_MODEL = "{model}";
    public static String STATIC_SERIAL = "{serial}";
    public static String STATIC_ENDPOINT_NO = "{endpoint_no}";
    public static String STATIC_SEQUENCE = "{sequence}";

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
                        topic += "/v1";
                }
                if(!Common.empty(mqttRequest.getNodeId())){
                    topic += "/"+ ByteUtil.getHexString(nodeId);
                } else {
                    topic += "0x00";
                }
                if(!Common.empty(mqttRequest.getEndpointId())){
                    topic += "/"+ ByteUtil.getHexString(endPointId);
                } else {
                    topic += "/0x00";
                }
                if(!Common.empty(mqttRequest.getSecurityOption())){
                    topic += "/"+ mqttRequest.getSecurityOption();
                } else {
                    topic += "/s0";
                }
        logger.info("====================== ZWAVE PROTO MQTT PUBLISH TOPIC ======================");
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

    public static void publish(ProducerComponent producerRestController, Message message) throws InterruptedException {

        producerRestController.run(message);

    }

    public static void publishNotificationData(ProducerComponent producerRestController, Properties callbackAckProperties, String sAckPropertyName, String target, String model, String serial,
            Object notiData)
            throws InterruptedException, JsonGenerationException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        String topic = callbackAckProperties.getProperty(sAckPropertyName);
        String exeTopic = MqttCommon.rtnCallbackAck(topic, target, model, serial);
        Message message = new Message(exeTopic, objectMapper.writeValueAsString(notiData));
        MqttCommon.publish(producerRestController, message);
    }
    
}

