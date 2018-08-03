package com.ht.connected.home.backend.device.category.zwave.certi;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.common.MqttCommon;
import com.ht.connected.home.backend.controller.mqtt.Message;
import com.ht.connected.home.backend.controller.mqtt.ProducerComponent;
import com.ht.connected.home.backend.device.category.gateway.Gateway;
import com.ht.connected.home.backend.device.category.gateway.GatewayRepository;
import com.ht.connected.home.backend.device.category.zwave.ZWave;
import com.ht.connected.home.backend.device.category.zwave.ZWaveCommonService;
import com.ht.connected.home.backend.device.category.zwave.ZWaveReport;
import com.ht.connected.home.backend.device.category.zwave.ZWaveReportByApp;
import com.ht.connected.home.backend.device.category.zwave.ZWaveRepository;
import com.ht.connected.home.backend.device.category.zwave.ZWaveRequest;
import com.ht.connected.home.backend.device.category.zwave.ZWaveService;
import com.ht.connected.home.backend.device.category.zwave.certi.commandclass.NetworkManagementProxyCommandClass;
import com.ht.connected.home.backend.device.category.zwave.endpoint.EndpointReportByApp;
import com.ht.connected.home.backend.device.category.zwave.endpoint.EndpointService;
import com.ht.connected.home.backend.service.mqtt.MqttPayload;
import com.ht.connected.home.backend.service.mqtt.Target;

@Service
public class ZWaveCertiNetworkManagementProxyServiceImpl implements ZWaveCertiNetworkManagementProxyService {

    private static final Log logging = LogFactory.getLog(ZWaveCertiNetworkManagementProxyServiceImpl.class);

    ZWaveRepository zwaveRepository;
    ProducerComponent producerComponent;
    ZWaveService zWaveService;
    EndpointService endpointService;
    GatewayRepository gatewayRepository;
    ZWaveCommonService zWaveCommonService;
    
    @Autowired
    @Qualifier(value = "callbackAckProperties")
    Properties callbackAckProperties;
    
    @Autowired
    public ZWaveCertiNetworkManagementProxyServiceImpl(
            ZWaveCommonService zWaveCommonService,
            ZWaveRepository zwaveRepository,
            ProducerComponent producerComponent,
            ZWaveService zWaveService,
            EndpointService endpointService,
            GatewayRepository gatewayRepository
            ) {
        this.zWaveCommonService = zWaveCommonService;
        this.zwaveRepository = zwaveRepository;
        this.producerComponent = producerComponent;
        this.zWaveService = zWaveService;
        this.endpointService = endpointService;
        this.gatewayRepository = gatewayRepository;
    }

    @Autowired
    @Qualifier("zWaveFunctionProperties")
    Properties zWaveFunctionProperties;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public void subscribe(ZWaveRequest zwaveRequest, String payload) throws JsonParseException, JsonMappingException, IOException, Exception {

        MqttPayload mqttPayload = new MqttPayload();
        Object resultData = null;
        if (!Common.empty(payload)) {
            mqttPayload = objectMapper.readValue(payload, MqttPayload.class);
            resultData = mqttPayload.getResultData();
        }

        // 기기정보
        if ((zwaveRequest.getCommandKey() == NetworkManagementProxyCommandClass.INT_NODE_LIST_REPORT)
                && (!isNull(resultData))) {
            String data = objectMapper.writeValueAsString(resultData);
            // OX02
            /**
             * 기기 리스트 수신시 새로 등록한 기기가 있을경우는 새로 등록 없을 경우는 업데이트함.0x52 0x02 모드일경우
             */
            if (-1 == zwaveRequest.getNodeId() || 0 == zwaveRequest.getNodeId()) {
                zWaveCommonService.reportZWaveList(zwaveRequest, data);
            }
            // 신규 등록 기기 정보
            else {
                Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
                zwaveRequest.setGatewayNo(gateway.getNo());
                ZWaveReport zwaveReport = objectMapper.readValue(data, ZWaveReport.class);
                List<ZWave> lstOriginalZwave = zwaveRepository.findByGatewayNoAndNodeId(zwaveRequest.getGatewayNo(), zwaveRequest.getNodeId());
                if (lstOriginalZwave.size() == 0 && (!isNull(gateway)) && (zwaveReport.getNodelist() != null)) {
                    zWaveCommonService.saveGatewayCategory(zwaveRequest, zwaveRequest.getNodeId());
                    // 기기 리스트에 대한 정보일 경우
                    List<ZWave> nodeListItem = (List<ZWave>) zwaveReport.getNodelist();
                    for (int i = 0; i < nodeListItem.size(); i++) {
                        ZWave nodeItem = nodeListItem.get(i);
                        ZWave zwave = zWaveCommonService.saveZWaveList(zwaveRequest, nodeItem, gateway);
                        String topic = callbackAckProperties.getProperty("zwave.device.registration");
                        String exeTopic = MqttCommon.rtnCallbackAck(topic, Target.app.name(), gateway.getModel(), gateway.getSerial());
                        ZWaveReportByApp zWaveReportByApp = getZwaveReportApp(zwave);
                        String exePayload = objectMapper.writeValueAsString(zWaveReportByApp);
                        publish(exeTopic, exePayload);
                    }
                }
            }

        }
       
    }

    private void publish(String topic, String payload) throws InterruptedException {
        Message message = new Message(topic, payload);
        MqttCommon.publish(producerComponent, message);
    }

    private ZWaveReportByApp getZwaveReportApp(ZWave zwave) {
        ZWaveReportByApp zWaveReportByApp = new ZWaveReportByApp();
        if (zwave.getNodeId() != 1) {
            zWaveReportByApp.setZwaveNo(zwave.getNo());
            zWaveReportByApp.setNodeId(zwave.getNodeId());
            zWaveReportByApp.setNickname(zwave.getNickname());
            zWaveReportByApp.setStatus(zwave.getStatus());

            List<EndpointReportByApp> lstEndpointReportByApp = endpointService.getEndpoint(zwave);
            zWaveReportByApp.setEndpoints(lstEndpointReportByApp);
        }
        return zWaveReportByApp;
    }


}
