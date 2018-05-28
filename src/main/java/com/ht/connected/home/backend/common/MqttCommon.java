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

    /**
     * mqtt publish 토픽 생성
     * @param topicLeadingPath
     * @return
     */
    public static String getMqttPublishTopic(MqttRequest mqttRequest, String target) {
        String topic = "";
        int nodeId = 0;
        int endPointId = 0;
        if (!Objects.isNull(mqttRequest.getNodeId())) {
            nodeId = mqttRequest.getNodeId();
        }
        if (!Objects.isNull(mqttRequest.getEndpointId())) {
            endPointId = mqttRequest.getEndpointId();
        }
        
        topic = "/server"
                +"/"+ target 
                +"/"+ mqttRequest.getModel()
                +"/"+ mqttRequest.getSerialNo()
                +"/"+ "zwave"
                +"/"+ "certi"
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
                                topic += "/none";
                            }
                        } else {
                            topic += "/none";
                        }
                    } else {
                        topic += "/none";
                    }
                }else {
                    topic += "/none";
                }
        logger.info("====================== ZWAVE PROTO MQTT PUBLISH TOPIC ======================");
        logger.info(topic);
        return topic;
    }
    public static String getMqttPublishTopic(MqttRequest mqttRequest) {
        String topic = "";
        int nodeId = 0;
        int endPointId = 0;
        if (!Objects.isNull(mqttRequest.getNodeId())) {
            nodeId = mqttRequest.getNodeId();
        }
        if (!Objects.isNull(mqttRequest.getEndpointId())) {
            endPointId = mqttRequest.getEndpointId();
        }
        
        topic = "/server"
                +"/"+ Target.host.name() 
                +"/"+ mqttRequest.getModel()
                +"/"+ mqttRequest.getSerialNo()
                +"/"+ "zwave"
                +"/"+ "certi"
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
                                topic += "/none";
                            }
                        } else {
                            topic += "/none";
                        }
                    } else {
                        topic += "/none";
                    }
                }else {
                    topic += "/none";
                }
        logger.info("====================== ZWAVE PROTO MQTT PUBLISH TOPIC ======================");
        logger.info(topic);
        return topic;
    }
}
