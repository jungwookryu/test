package com.ht.connected.home.backend.common;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.connected.home.backend.service.mqtt.MqttRequest;
import com.ht.connected.home.backend.service.mqtt.Target;

/**
 * @author ijlee
 *         <p>
 *         create by 2017.06.21
 */
public class MqttCommon {

    private static Logger logger = LoggerFactory.getLogger(MqttCommon.class);
    private static String STATIC_TARGET ="{target}";
    private static String STATIC_MODEL ="{model}";
    private static String STATIC_SERIAL ="{serial}";
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
                    if(!Common.empty(mqttRequest.getNodeId())){
                        topic += "/"+ ByteUtil.getHexString(nodeId);
                        if(!Common.empty(mqttRequest.getEndpointId())){
                            topic += "/"+ ByteUtil.getHexString(endPointId);
                            if(!Common.empty(mqttRequest.getSecurityOption())){
                                topic += "/"+ mqttRequest.getSecurityOption();
                            } else {
                                topic += "/s0";
                            }
                        } else {
                            topic += "/0x00";
                        }
                    } else {
                        topic += "0x00";
                    }
                }else {
                    topic += "/v1";
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
        
        if(Objects.nonNull(ack)) {
            ack = ack.replace(STATIC_TARGET, target);
            ack = ack.replace(STATIC_MODEL, model);
            ack = ack.replace(STATIC_SERIAL, model);
            return ack;
        }
        return ack;
    }

    
}
