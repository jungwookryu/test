package com.ht.connected.home.backend.common;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.connected.home.backend.model.dto.MqttRequest;

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
        String[] segments = new String[] { "/server", target, mqttRequest.getModel(),mqttRequest.getSerialNo(), "zwave", "certi",
                ByteUtil.getHexString(mqttRequest.getClassKey()), ByteUtil.getHexString(mqttRequest.getCommandKey()), mqttRequest.getVersion(),
                ByteUtil.getHexString(nodeId), ByteUtil.getHexString(endPointId),
                mqttRequest.getSecurityOption() };
        topic = String.join("/", segments);
        logger.info("====================== ZWAVE PROTO MQTT PUBLISH TOPIC ======================");
        logger.info(topic);
        return topic;
    }

}
