package com.ht.connected.home.backend.device.category.zwave.certi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.common.MqttCommon;
import com.ht.connected.home.backend.controller.mqtt.Message;
import com.ht.connected.home.backend.controller.mqtt.ProducerComponent;
import com.ht.connected.home.backend.device.category.gateway.Gateway;
import com.ht.connected.home.backend.device.category.gateway.GatewayRepository;
import com.ht.connected.home.backend.device.category.zwave.ZWaveCommonService;
import com.ht.connected.home.backend.device.category.zwave.ZWaveRequest;
import com.ht.connected.home.backend.device.category.zwave.ZWaveService;
import com.ht.connected.home.backend.device.category.zwave.certi.commandclass.NetworkManagementInclusionCommandClass;
import com.ht.connected.home.backend.device.category.zwave.certi.commandclass.NetworkManagementProxyCommandClass;
import com.ht.connected.home.backend.device.category.zwave.endpoint.EndpointRepository;
import com.ht.connected.home.backend.service.mqtt.MqttPayload;
import com.ht.connected.home.backend.service.mqtt.MqttRequest;
import com.ht.connected.home.backend.service.mqtt.Target;

@Service
public class ZWaveCertiNetworkManagementInclusionServiceImpl implements ZWaveCertiNetworkManagementInclusionService {

    private static final Log logging = LogFactory.getLog(ZWaveCertiNetworkManagementInclusionServiceImpl.class);

    @Autowired
    @Qualifier(value = "callbackAckProperties")
    Properties callbackAckProperties;
    ProducerComponent producerComponent;
    GatewayRepository gatewayRepository;
    EndpointRepository endpointRepository;
    ZWaveService zWaveService;
    ZWaveCommonService zWaveCommonService;
  
    @Autowired
    public ZWaveCertiNetworkManagementInclusionServiceImpl(
            ZWaveCommonService zWaveCommonService,
            ProducerComponent producerComponent,
            GatewayRepository gatewayRepository,
            EndpointRepository endpointRepository,
            ZWaveService zWaveService
            ) {
        this.zWaveCommonService = zWaveCommonService;
        this.producerComponent = producerComponent;
        this.gatewayRepository = gatewayRepository;
        this.endpointRepository = endpointRepository;
        this.zWaveService = zWaveService;
    }
    
    @Autowired
    @Qualifier("zWaveFunctionProperties")
    Properties zWaveFunctionProperties;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public void subscribe(ZWaveRequest zwaveRequest, String payload) throws JsonParseException, JsonMappingException, IOException, Exception {

        MqttPayload mqttPayload = new MqttPayload();
        if (!Common.empty(payload)) {
            mqttPayload = objectMapper.readValue(payload, MqttPayload.class);
        }

        // 기기 모드 0x34결과
        if (zwaveRequest.getClassKey() == NetworkManagementInclusionCommandClass.INT_ID) {
            // 기기삭제 상태값 받은 경우 기기삭제 모드 0x34/0x04 결과
            if (zwaveRequest.getCommandKey() == NetworkManagementInclusionCommandClass.INT_NODE_REMOVE_STATUS ||
                    zwaveRequest.getCommandKey() == NetworkManagementInclusionCommandClass.INT_FAILED_NODE_STATUS ||
                    zwaveRequest.getCommandKey() == NetworkManagementInclusionCommandClass.INT_FAILED_NODE_REPLACE) {
                HashMap result = mqttPayload.getResultData();
                if (0 == (Integer) result.getOrDefault("status", -1)) {
                    deleteZwaveSubscribe(zwaveRequest, mqttPayload);
                }
            }
            // 기기등록 상태값 받은 경우 기기 등록 모드 0x34/0x02 결과, 기기등록 상태값 받은 경우 기기 등록 모드 0x34/0x01 결과
            else if ((zwaveRequest.getCommandKey() == NetworkManagementInclusionCommandClass.INT_NODE_ADD_STATUS ||
                    zwaveRequest.getCommandKey() == NetworkManagementInclusionCommandClass.INT_NODE_ADD) &&
                    (!Objects.isNull(mqttPayload.getResultData()))) {
                String topic = callbackAckProperties.getProperty("zwave.device.registration");
                String exeTopic = MqttCommon.rtnCallbackAck(topic, Target.app.name(), zwaveRequest.getModel(), zwaveRequest.getSerialNo());
                publish(exeTopic, objectMapper.writeValueAsString(mqttPayload));
            }
            // 기기등록 상태값 받은 경우 기기 등록 모드 0x34/0x02 결과, 기기등록 상태값 받은 경우 기기 등록 모드 0x34/0x01 결과 s2spin
            else if ((zwaveRequest.getCommandKey() == NetworkManagementInclusionCommandClass.INT_NODE_ADD) &&
                    (!Objects.isNull(mqttPayload.getSetData()))) {
                Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
                Map map = new HashMap();
                map.put("set_data", mqttPayload.getSetData());
                map.put("no", gateway.getNo());
                String topic = callbackAckProperties.getProperty("zwave.device.registrationS2");
                String exeTopic = MqttCommon.rtnCallbackAck(topic, Target.app.name(), zwaveRequest.getModel(), zwaveRequest.getSerialNo());
                publish(exeTopic, objectMapper.writeValueAsString(map));
            }
        }

    }

    private void publish(String topic, String payload) throws InterruptedException {
        Message message = new Message(topic, payload);
        MqttCommon.publish(producerComponent, message);
    }

    private void deleteZwaveSubscribe(ZWaveRequest zwaveRequest, MqttPayload mqttPayload) throws JsonProcessingException, InterruptedException {
        Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
        List lst = new ArrayList();
        HashMap resultMapData = mqttPayload.getResultData();
        int nodeId = (int) resultMapData.getOrDefault("newNodeId", zwaveRequest.getNodeId());
        if (nodeId == 0) {
            MqttRequest mqttRequest = new MqttRequest(gateway);
            mqttRequest.setClassKey(NetworkManagementProxyCommandClass.INT_ID);
            mqttRequest.setCommandKey(NetworkManagementProxyCommandClass.INT_NODE_LIST_GET);
            String exeTopic = MqttCommon.getMqttPublishTopic(mqttRequest, zwaveRequest.getSource());
            Message message = new Message(exeTopic, null);
            producerComponent.run(message);
        } else {
            lst = zWaveCommonService.deleteZwave(gateway.getNo(), nodeId);
            resultMapData.put("zwave_nos", lst);
        }
        String topic = callbackAckProperties.getProperty("zwave.device.remove");
        String exeTopic = MqttCommon.rtnCallbackAck(topic, Target.app.name(), zwaveRequest.getModel(), zwaveRequest.getSerialNo());
        String pushPayload = objectMapper.writeValueAsString(resultMapData);
        publish(exeTopic, pushPayload);
    }

}
