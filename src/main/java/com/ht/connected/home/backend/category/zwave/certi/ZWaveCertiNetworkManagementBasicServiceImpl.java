package com.ht.connected.home.backend.category.zwave.certi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import com.ht.connected.home.backend.category.zwave.ZWave;
import com.ht.connected.home.backend.category.zwave.ZWaveRepository;
import com.ht.connected.home.backend.category.zwave.ZWaveRequest;
import com.ht.connected.home.backend.category.zwave.certi.commandclass.NetworkManagementBasicCommandClass;
import com.ht.connected.home.backend.category.zwave.cmdcls.CmdClsRepository;
import com.ht.connected.home.backend.category.zwave.endpoint.Endpoint;
import com.ht.connected.home.backend.category.zwave.endpoint.EndpointRepository;
import com.ht.connected.home.backend.category.zwave.endpoint.EndpointService;
import com.ht.connected.home.backend.common.MqttCommon;
import com.ht.connected.home.backend.controller.mqtt.Message;
import com.ht.connected.home.backend.controller.mqtt.ProducerComponent;
import com.ht.connected.home.backend.gateway.Gateway;
import com.ht.connected.home.backend.gateway.GatewayRepository;
import com.ht.connected.home.backend.service.mqtt.Target;
/**
 * 0x4D // NetworkManagementBasic Protocal Service
 * @author COM
 *
 */
@Service
public class ZWaveCertiNetworkManagementBasicServiceImpl implements ZWaveCertiNetworkManagementBasicService {

    private ZWaveRepository zwaveRepository;
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
            ProducerComponent ProducerComponent) {
        this.zwaveRepository = zwaveRepository;
        this.endpointRepository = endpointRepository;
        this.cmdClsRepository = cmdClsRepository;
        this.endpointService = endpointService;
        this.gatewayRepository = gatewayRepository;
        this.zWaveProperties = zWaveProperties;
        this.producerComponent = ProducerComponent;
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

        // 기기 초기화 결과 0x4D/0x07 기기상태값모드 받은 경우
        if (zwaveRequest.getCommandKey() == NetworkManagementBasicCommandClass.DEFAULT_SET_COMPLETE) {
            // 해당기기의 정보를 모두 삭제한다.
            zwaveReset(zwaveRequest);
            String topic = callbackAckProperties.getProperty("manager.product.remove");
            String exeTopic = MqttCommon.rtnCallbackAck(topic, Target.app.name(), zwaveRequest.getModel(), zwaveRequest.getSerialNo());
            publish(exeTopic, new HashMap());

        }

    }

    private void publish(String topic, HashMap<String, Object> publishPayload) throws JsonProcessingException, InterruptedException {
        String payload = objectMapper.writeValueAsString(publishPayload);
        Message message = new Message(topic, payload);
        MqttCommon.publish(producerComponent, message);
    }

    private void zwaveReset(ZWaveRequest zwaveRequest) {
        // host 정보삭제
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
