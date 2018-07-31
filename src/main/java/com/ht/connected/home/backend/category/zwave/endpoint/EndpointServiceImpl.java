package com.ht.connected.home.backend.category.zwave.endpoint;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ht.connected.home.backend.category.zwave.ZWave;
import com.ht.connected.home.backend.category.zwave.ZWaveControl;
import com.ht.connected.home.backend.category.zwave.ZWaveRepository;
import com.ht.connected.home.backend.category.zwave.certi.commandclass.BinarySwitchCommandClass;
import com.ht.connected.home.backend.category.zwave.certi.commandclass.CommandClass;
import com.ht.connected.home.backend.category.zwave.certi.commandclass.CommandClassFactory;
import com.ht.connected.home.backend.category.zwave.cmdcls.CmdClsRepository;
import com.ht.connected.home.backend.category.zwave.notification.NotificationRepository;
import com.ht.connected.home.backend.category.zwave.notification.ZwaveCertiNotificationService;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.common.MqttCommon;
import com.ht.connected.home.backend.controller.mqtt.Message;
import com.ht.connected.home.backend.controller.mqtt.ProducerComponent;
import com.ht.connected.home.backend.gateway.Gateway;
import com.ht.connected.home.backend.gateway.GatewayRepository;
import com.ht.connected.home.backend.service.mqtt.MqttRequest;

@Service
public class EndpointServiceImpl implements EndpointService {

    private EndpointRepository endpointRepository;

    @Autowired
    ZWaveRepository zwaveRepository;
    
    @Autowired
    ProducerComponent producerComponent;
    
    @Autowired
    GatewayRepository gatewayRepository;
    
    @Autowired
    CmdClsRepository cmdClsRepository;
    
    @Autowired
    NotificationRepository notificationRepository;
    
    @Autowired
    Properties zWaveProperties;
    
    @Autowired
    public EndpointServiceImpl(EndpointRepository endpointRepository) {
        this.endpointRepository = endpointRepository;
    }

    @Autowired
    ZWaveRepository zWaveRepository;
    
    @Autowired
    ZwaveCertiNotificationService notificationService;
    
    ObjectMapper objectMapper = new ObjectMapper();
    private static final Log logger = LogFactory.getLog(EndpointServiceImpl.class);

    /**
     * @zwave 기기  endpoint 정보수정
     * @author ijlee
     */
    @Transactional
    @Override
    public ZWave modify(int no , Endpoint endpoint) {
        Endpoint saveEndpoint = endpointRepository.findOne(no);
        if(saveEndpoint != null) {
            if(0==saveEndpoint.getEpid()) {
                zWaveRepository.setModifyNicknameForNo(endpoint.getNickname(), saveEndpoint.getZwaveNo());
            }
            saveEndpoint.setNickname(endpoint.getNickname());
            endpointRepository.setModifyNicknameForNo(endpoint.getNickname(), no);
            return zWaveRepository.findOne(saveEndpoint.getZwaveNo());
        }else {
            return new ZWave();
        }
    }
    @Transactional
    @Override
    public void deleteEndpoint(ZWave zWave) {
        List<Endpoint> lstEndpoint = endpointRepository.findByZwaveNo(zWave.getNo());
        for (Endpoint endpoint : lstEndpoint) {
            cmdClsRepository.deleteByEndpointNo(endpoint.getNo());
            notificationService.delete(zWave);
        }
        endpointRepository.deleteByZwaveNo(zWave.getNo());
    }
    
    @Override
    public List<EndpointReportByApp> getEndpoint(ZWave zwave) {
        List<EndpointReportByApp> lstEndpointReportByApp = new ArrayList<>();
        List<Endpoint> lstEndpoint = endpointRepository.findByZwaveNo(zwave.getNo());
        for (int j = 0; j < lstEndpoint.size(); j++) {
            Endpoint endpoint = lstEndpoint.get(j);
            EndpointReportByApp endpointReportByApp = new EndpointReportByApp();
            endpointReportByApp.setEndpointNo(endpoint.getNo());
            endpointReportByApp.setEpStatus(endpoint.getStatus());
            endpointReportByApp.setEpid(endpoint.getEpid());
            endpointReportByApp.setNickname(endpoint.getNickname());
            endpointReportByApp.setFunctionCode(endpoint.getFunctionCode());
            endpointReportByApp.setDeviceTypeCode(endpoint.getGeneric()+"."+endpoint.getSpecific());
            endpointReportByApp.setDeviceTypeName(endpoint.getDeviceTypeName());
            endpointReportByApp.setNotifications(notificationService.getNotification(endpoint));
            
            lstEndpointReportByApp.add(endpointReportByApp);
        }
        return lstEndpointReportByApp;
    }
    /**
     * Zwave 기기제어
     * @author lij
     * @throws InterruptedException 
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    // 제어
    @Override
    public void zwaveControl(ZWaveControl zWaveControl) throws InterruptedException, JsonGenerationException, JsonMappingException, IOException {

        Endpoint endpoint = endpointRepository.findOne(zWaveControl.getEndpoint_no());
        ZWave zwave = zwaveRepository.findOne(endpoint.getZwaveNo());
        Gateway gateway = gatewayRepository.findOne(zwave.getGatewayNo());

        MqttRequest mqttRequest = new MqttRequest();
        mqttRequest.setNodeId(zwave.getNodeId());
        if(!Objects.isNull(endpoint)) {
            mqttRequest.setEndpointId(endpoint.getEpid());
        }
        mqttRequest.setSerialNo(gateway.getSerial());
        mqttRequest.setModel(gateway.getModel());
        zWaveControl.setFunctionCode("0x"+endpoint.getFunctionCode());
        mqttRequest.setClassKey(zWaveControl.getFunctionCode());
        mqttRequest.setCommandKey(zWaveControl.getControlCode());
        mqttRequest.setVersion("v1");
        mqttRequest.setSecurityOption("0");
        mqttRequest.setTarget(gateway.getTargetType());
        HashMap map = new HashMap<>();
        map.put("set_data", zWaveControl.getSetData());
        mqttRequest.setSetData(map);
        String topic = MqttCommon.getMqttPublishTopic(mqttRequest);
        String payload = objectMapper.writeValueAsString(map);
        Message message = new Message(topic, payload);
        MqttCommon.publish(producerComponent, message);
    }
    
    public Endpoint endpointType(Endpoint endpoint) {
        CommandClass commandClass = CommandClassFactory.createCommandClass(BinarySwitchCommandClass.ID);
        commandClass = CommandClassFactory.createSCmdClass(endpoint);
        if (!isNull(commandClass)) {
            endpoint.setDeviceType(commandClass.getDeviceType());
            endpoint.setDeviceNickname(commandClass.getNicknameType());
            endpoint.setDeviceTypeName(Common.zwaveNickname(zWaveProperties, endpoint.getGeneric() + "." + endpoint.getSpecific()));
            endpoint.setDeviceFunctions(commandClass.getFunctionType());
            endpoint.setFunctionCode(commandClass.getFunctionCode());
        }
        return endpoint;
    }

    @Override
    public Endpoint saveEndpoint(Endpoint endpoint) {
        endpoint = endpointType(endpoint);
        endpoint = endpointRepository.save(endpoint);
        return endpoint;
    }
}
