package com.ht.connected.home.backend.device.category.zwave.certi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import com.ht.connected.home.backend.device.category.zwave.ZWave;
import com.ht.connected.home.backend.device.category.zwave.ZWaveRepository;
import com.ht.connected.home.backend.device.category.zwave.ZWaveRequest;
import com.ht.connected.home.backend.device.category.zwave.certi.commandclass.NetworkManagementBasicCommandClass;
import com.ht.connected.home.backend.device.category.zwave.cmdcls.CmdClsRepository;
import com.ht.connected.home.backend.device.category.zwave.endpoint.Endpoint;
import com.ht.connected.home.backend.device.category.zwave.endpoint.EndpointRepository;
import com.ht.connected.home.backend.device.category.zwave.endpoint.EndpointService;
import com.ht.connected.home.backend.service.mqtt.MqttPayload;
import com.ht.connected.home.backend.service.mqtt.MqttRequest;
import com.ht.connected.home.backend.service.mqtt.Target;
/**
 * 0x4D // NetworkManagementBasic Protocal Service
 * @author COM
 *
 */
@Service
public class ZWaveCertiNetworkManagementBasicServiceImpl implements ZWaveCertiNetworkManagementBasicService {

    ZWaveRepository zwaveRepository;
    EndpointRepository endpointRepository;
    CmdClsRepository cmdClsRepository;
    EndpointService endpointService;
    GatewayRepository gatewayRepository;
    Properties zWaveProperties;
    ProducerComponent producerComponent;

    private static final Log logging = LogFactory.getLog(ZWaveCertiNetworkManagementBasicServiceImpl.class);

    @Autowired
    @Qualifier(value = "callbackAckProperties")
    Properties callbackAckProperties;

    @Autowired
    public ZWaveCertiNetworkManagementBasicServiceImpl(
            ZWaveRepository zwaveRepository,
            EndpointRepository endpointRepository,
            CmdClsRepository cmdClsRepository,
            EndpointService endpointService,
            GatewayRepository gatewayRepository,
            Properties zWaveProperties,
            ProducerComponent producerComponent) {
        this.zwaveRepository = zwaveRepository;
        this.endpointRepository = endpointRepository;
        this.cmdClsRepository = cmdClsRepository;
        this.endpointService = endpointService;
        this.gatewayRepository = gatewayRepository;
        this.zWaveProperties = zWaveProperties;
        this.producerComponent = producerComponent;
    }

    @Autowired
    ZWaveCertiNetworkManagementInclusionService zWaveCertiNetworkManagementInclusionService;

    @Autowired
    @Qualifier("zWaveFunctionProperties")
    Properties zWaveFunctionProperties;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    @SuppressWarnings("rawtypes")
    public void subscribe(ZWaveRequest zwaveRequest, String payload) throws JsonParseException, JsonMappingException, IOException, Exception {

    	MqttPayload mqttPayload = new MqttPayload();
    	if(Common.notEmpty(payload)) {
    		mqttPayload = objectMapper.readValue(payload, MqttPayload.class);
    	}
        // 기기 초기화 결과 0x4D/0x07 기기상태값모드 받은 경우
        if (zwaveRequest.getCommandKey() == NetworkManagementBasicCommandClass.DEFAULT_SET_COMPLETE &&
        		(!Objects.isNull(mqttPayload.getResultData())) && ((int)mqttPayload.getResultData().getOrDefault("status", 255)==0)){
            // 해당기기의 정보를 모두 삭제한다.
            zwaveReset(zwaveRequest);
            String topic = callbackAckProperties.getProperty("manager.product.remove");
            String exeTopic = MqttCommon.rtnCallbackAck(topic, Target.app.name(), zwaveRequest.getModel(), zwaveRequest.getSerialNo());
            publish(exeTopic, new HashMap());

        }
        
        else if (zwaveRequest.getCommandKey() == NetworkManagementBasicCommandClass.LEARN_MODE_SET) {
            String topic = callbackAckProperties.getProperty("manager.product.remove");
            String exeTopic = MqttCommon.rtnCallbackAck(topic, Target.app.name(), zwaveRequest.getModel(), zwaveRequest.getSerialNo());
            publish(exeTopic, new HashMap());
            
        }

    }

    @Override
    public void setLearnMode(Gateway gateway, int mode) throws JsonProcessingException, InterruptedException {
    	MqttRequest mqttRequest = new MqttRequest(gateway);
    	mqttRequest.setTarget(gateway.getTargetType());
    	mqttRequest.setClassKey(NetworkManagementBasicCommandClass.INT_ID);
    	mqttRequest.setCommandKey(NetworkManagementBasicCommandClass.INT_LEARN_MODE_SET);
    	
    	String topic = MqttCommon.getMqttPublishTopic(mqttRequest);
    	HashMap<String, Object> publishPayload = new HashMap<>();
    	HashMap map = new HashMap<>();
        map.put("mode", mode);
        publishPayload.put("set_data", map);
        mqttRequest.setSetData(publishPayload);
    	publish(topic, mqttRequest.getSetData());
    }
    
    @Override
    public void setZWaveResetMode(Gateway gateway) throws JsonProcessingException, InterruptedException {
    	MqttRequest mqttRequest = new MqttRequest(gateway);
    	mqttRequest.setTarget(gateway.getTargetType());
    	mqttRequest.setClassKey(NetworkManagementBasicCommandClass.INT_ID);
    	mqttRequest.setCommandKey(NetworkManagementBasicCommandClass.INT_DEFAULT_SET);
    	
    	String topic = MqttCommon.getMqttPublishTopic(mqttRequest);
    	HashMap<String, Object> publishPayload = new HashMap<>();
    	publish(topic, publishPayload);
    }    
    
    
    private void publish(String topic, HashMap<String, Object> publishPayload) throws JsonProcessingException, InterruptedException {
        String payload = objectMapper.writeValueAsString(publishPayload);
        Message message = new Message(topic, payload);
        MqttCommon.publish(producerComponent, message);
    }

    @Transactional
    private void zwaveReset(ZWaveRequest zwaveRequest) {
        // host 정보삭제
    	//TODO Service 호출을 통한 한번에 지우는 로직으로 수정하도록 함. 
        Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());

        List<ZWave> lstZWave = zwaveRepository.findByGatewayNo(gateway.getNo());
        for (ZWave zWave : lstZWave) {
            List<Endpoint> lstEndpoint = endpointRepository.findByZwaveNo(zWave.getNo());
            for (Endpoint endpoint : lstEndpoint) {
                cmdClsRepository.deleteByEndpointNo(endpoint.getNo());
            }
            endpointRepository.deleteByZwaveNo(zWave.getNo());
        }
        zwaveRepository.deleteByGatewayNo(gateway.getNo());

    }

}
