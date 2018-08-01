package com.ht.connected.home.backend.device.category.zwave;

import java.util.HashMap;
import java.util.Properties;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.common.MqttCommon;
import com.ht.connected.home.backend.controller.mqtt.Message;
import com.ht.connected.home.backend.controller.mqtt.ProducerComponent;
import com.ht.connected.home.backend.device.category.CategoryActive;
import com.ht.connected.home.backend.service.mqtt.MqttPayload;
import com.ht.connected.home.backend.service.mqtt.MqttRequest;

@Service
public class ZWaveServiceImpl implements ZWaveService {

    private ZWaveCommonService zWaveCommonService;
    
    private static final Log logging = LogFactory.getLog(ZWaveServiceImpl.class);

    ProducerComponent producerComponent;

    @Autowired
    public ZWaveServiceImpl(
        ZWaveCommonService zWaveCommonService,
        ProducerComponent producerComponent
     ) {
        this.zWaveCommonService = zWaveCommonService;
    }

    

    @Autowired
    @Qualifier("zWaveFunctionProperties")
    Properties zWaveFunctionProperties;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void publish(HashMap<String, Object> req, ZWaveRequest zwaveRequest) throws JsonProcessingException, InterruptedException {
        MqttRequest mqttRequest = new MqttRequest();
        mqttRequest.setSerialNo(zwaveRequest.getSerialNo());
        mqttRequest.setModel(zwaveRequest.getModel());
        mqttRequest.setClassKey(zwaveRequest.getClassKey());
        mqttRequest.setCommandKey(zwaveRequest.getCommandKey());
        mqttRequest.setTarget(zwaveRequest.getTarget());
        mqttRequest.setSetData(req);
        publish(mqttRequest);
    }

    @Transactional
    public void subscribe(ZWaveRequest zwaveRequest, String payload) throws Exception {

        Object resultData = null;

        if (!Common.empty(payload)) {
            MqttPayload mqttPayload = objectMapper.readValue(payload, MqttPayload.class);
            resultData = mqttPayload.getResultData();
        }

        if (CategoryActive.zwave.init.name().equals(zwaveRequest.getCategoryActive())) {
            zWaveCommonService.reportZWaveList(zwaveRequest, (String) resultData);
        }

    }

    private void publish(MqttRequest mqttRequest) throws JsonProcessingException, InterruptedException {

        publish(MqttCommon.getMqttPublishTopic(mqttRequest), mqttRequest.getSetData());
    }

    private void publish(String topic, HashMap<String, Object> publishPayload) throws JsonProcessingException, InterruptedException {
        String payload = objectMapper.writeValueAsString(publishPayload);
        publish(topic, payload);
    }

    private void publish(String topic, String payload) throws InterruptedException {
        Message message = new Message(topic, payload);
        MqttCommon.publish(producerComponent, message);
    }
}
