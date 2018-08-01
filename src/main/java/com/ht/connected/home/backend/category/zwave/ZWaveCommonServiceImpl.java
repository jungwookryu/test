package com.ht.connected.home.backend.category.zwave;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.category.gateway.Gateway;
import com.ht.connected.home.backend.category.gateway.GatewayRepository;
import com.ht.connected.home.backend.category.zwave.certi.ZWaveCertiService;
import com.ht.connected.home.backend.category.zwave.certi.commandclass.BasicCommandClass;
import com.ht.connected.home.backend.category.zwave.cmdcls.CmdCls;
import com.ht.connected.home.backend.category.zwave.cmdcls.CmdClsRepository;
import com.ht.connected.home.backend.category.zwave.endpoint.Endpoint;
import com.ht.connected.home.backend.category.zwave.endpoint.EndpointReportByApp;
import com.ht.connected.home.backend.category.zwave.endpoint.EndpointRepository;
import com.ht.connected.home.backend.category.zwave.endpoint.EndpointService;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.common.MqttCommon;
import com.ht.connected.home.backend.controller.mqtt.Message;
import com.ht.connected.home.backend.controller.mqtt.ProducerComponent;
import com.ht.connected.home.backend.gatewayCategory.CategoryActive;
import com.ht.connected.home.backend.gatewayCategory.GatewayCategory;
import com.ht.connected.home.backend.gatewayCategory.GatewayCategoryRepository;
import com.ht.connected.home.backend.service.mqtt.MqttPayload;
import com.ht.connected.home.backend.service.mqtt.MqttRequest;
import com.ht.connected.home.backend.user.User;
import com.ht.connected.home.backend.user.UserRepository;
import com.ht.connected.home.backend.userGateway.UserGateway;
import com.ht.connected.home.backend.userGateway.UserGatewayRepository;

@Service
public class ZWaveCommonServiceImpl implements ZWaveCommonService {

    private ZWaveRepository zwaveRepository;
    private UserRepository userRepository;
    private GatewayRepository gatewayRepository;
    private UserGatewayRepository userGatewayRepository;
    private EndpointRepository endpointRepository;
    private CmdClsRepository cmdClsRepository;
    private EndpointService endpointService;
    private ZWaveCertiService zWaveCertiService;
    private GatewayCategoryRepository gatewayCategoryRepository;
    private Properties zWaveProperties;
    
    private static final Log logging = LogFactory.getLog(ZWaveCommonServiceImpl.class);

    @Autowired
    @Qualifier(value = "callbackAckProperties")
    Properties callbackAckProperties;

    @Autowired
    ProducerComponent producerComponent;

    enum status {
        add, delete, active, failed
    }

    @Autowired
    public ZWaveCommonServiceImpl(
            ZWaveRepository zwaveRepository,
            UserRepository userRepository,
            GatewayRepository gatewayRepository,
            UserGatewayRepository userGatewayRepository,
            EndpointRepository endpointRepository,
            CmdClsRepository cmdClsRepository,
            EndpointService endpointService,
            @Lazy ZWaveCertiService zWaveCertiService,
            GatewayCategoryRepository gatewayCategoryRepository,
            Properties zWaveProperties
     ) {
        this.zwaveRepository = zwaveRepository;
        this.userRepository = userRepository;
        this.gatewayRepository = gatewayRepository;
        this.userGatewayRepository = userGatewayRepository;
        this.endpointRepository = endpointRepository;
        this.cmdClsRepository = cmdClsRepository;
        this.endpointService = endpointService;
        this.zWaveCertiService = zWaveCertiService;
        this.gatewayCategoryRepository = gatewayCategoryRepository;
        this.zWaveProperties = zWaveProperties;
    }

    @Autowired
    @Qualifier("zWaveFunctionProperties")
    Properties zWaveFunctionProperties;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void reportZWaveList(ZWaveRequest zwaveRequest, String data) throws JsonParseException, JsonMappingException, IOException, JSONException, InterruptedException {

        Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
        zwaveRequest.setGatewayNo(gateway.getNo());
        List<ZWave> lstZwave = zwaveRepository.findByGatewayNo(gateway.getNo());
        ZWaveReport zwaveReport = objectMapper.readValue(data, ZWaveReport.class);
        if (!isNull(gateway) && !Objects.isNull(zwaveReport.getNodelist())) {
            List<ZWave> nodeListItem = (List<ZWave>) zwaveReport.getNodelist();
            // 추가
            for (ZWave nodeItem : nodeListItem) {
                int nodeId = nodeItem.getNodeId();
                boolean bInsert = false;
                for (ZWave zWave : lstZwave) {
                    if (nodeId == zWave.getNodeId()) {
                        bInsert = true;
                    }
                }
                // 등록안되어있고 node의 status가 delete가 아닐경우 일경우 insert함.
                if (!bInsert) {
                    saveZWaveList(zwaveRequest, nodeItem, gateway);
                }
            }
            // host에는 있고 server에는 없을경우 삭제한다.
            for (ZWave zwave : lstZwave) {
                boolean bDelete = true;
                for (ZWave nodeItem : nodeListItem) {
                    // host 노드리스트와 DB 노드리스트를 비교하여 없으면 delete 시킴
                    if (nodeItem.getNodeId() == zwave.getNodeId()) {
                        bDelete = false;
                    }
                }
                // 등록되어있고 node가 없을경우 삭제함
                if (bDelete) {
                    // zwave nodeId Category 별삭제함.
                    deleteZwave(zwaveRequest.getGatewayNo(), zwave.getNodeId());
                }
            }
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

    @Override
    public int getByUserEmailAndNo(String userEmail, int no) {
        List<Integer> lstGatewayNos = new ArrayList<>();
        List<User> user = userRepository.findByUserEmail(userEmail);
        List<UserGateway> lstUserGateway = userGatewayRepository.findByUserNo(user.get(0).getNo());
        lstUserGateway.forEach(userGateway -> {
            lstGatewayNos.add(userGateway.getGatewayNo());
        });
        List<ZWave> lstZwave = zwaveRepository.findByNoAndGatewayNoIn(no, lstGatewayNos);
        return lstZwave.size();
    }

    @Override
    public ZWaveReport getZWaveList(int gatewayNo) {
        ZWaveReport zWaveReport = new ZWaveReport();
        List<ZWave> lstZWave = zwaveRepository.findByGatewayNo(gatewayNo);
        zWaveReport.setNodelist(lstZWave);
        return zWaveReport;
    }

    @Override
    public Map getZWaveListApp(int gatewayNo) {
        Map map = new HashMap();
        List<ZWave> lstZWave = zwaveRepository.findByGatewayNo(gatewayNo);
        List<ZWaveReportByApp> rtnList = new ArrayList<>();
        for (int i = 0; i < lstZWave.size(); i++) {
            ZWave zwave = lstZWave.get(i);
            if (zwave.getNodeId() != 1) {
                ZWaveReportByApp zWaveReportByApp = getZwaveReportApp(zwave);
                rtnList.add(zWaveReportByApp);
            }

        }
        map.put("nodelist", rtnList);
        map.put("nodeCnt", lstZWave.size() - 1);
        return map;
    }

    // 삭제 토픽
    @Override
    @Transactional
    public int deleteByNo(int no) throws JsonProcessingException, InterruptedException {

        ZWave zwave = zwaveRepository.getOne(no);
        Gateway gateway = gatewayRepository.getOne(zwave.getGatewayNo());
        int iRtn = zwaveRepository.setFixedStatusForNo(status.delete.name(), no);
        MqttRequest mqttRequest = new MqttRequest();

        mqttRequest.setSerialNo(gateway.getSerial());
        mqttRequest.setModel(gateway.getModel());
        mqttRequest.setNodeId(zwave.getNodeId());
        mqttRequest.setTarget(gateway.getTargetType());
        zWaveCertiService.publishDelete(mqttRequest);
        return iRtn;

    }

    /**
     * Zwave 기기제어
     * @author lij
     * @throws JsonProcessingException
     * @throws InterruptedException
     */
    // 제어
    @Override
    public void zwaveBasicControl(ZWaveControl zWaveControl) throws JsonProcessingException, InterruptedException {

        Gateway gateway = gatewayRepository.findOne(zWaveControl.getGateway_no());
        Endpoint endpoint = endpointRepository.findOne(zWaveControl.getEndpoint_no());
        ZWave zwave = zwaveRepository.findOne(endpoint.getZwaveNo());

        MqttRequest mqttRequest = new MqttRequest();
        mqttRequest.setNodeId(zwave.getNodeId());
        if (!Objects.isNull(endpoint)) {
            mqttRequest.setEndpointId(endpoint.getEpid());
        }
        mqttRequest.setSerialNo(gateway.getSerial());
        mqttRequest.setModel(gateway.getModel());
        zWaveControl.setFunctionCode(BasicCommandClass.functionCode);
        mqttRequest.setClassKey(zWaveControl.getFunctionCode());
        mqttRequest.setCommandKey(zWaveControl.getControlCode());
        mqttRequest.setTarget(gateway.getTargetType());
        zwaveBasicControl(mqttRequest);
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

    @Override
    public ZWave saveZWaveList(ZWaveRequest zwaveRequest, ZWave nodeItem, Gateway gateway) {
        saveGatewayCategory(zwaveRequest, nodeItem.getNodeId());

        nodeItem.setGatewayNo(gateway.getNo());
        nodeItem.setCreratedTime(new Date());
        String nodeKey = nodeItem.getGeneric() + "." + nodeItem.getSpecific();
        nodeItem.setNickname(Common.zwaveNickname(zWaveProperties, nodeKey));
        ZWave saveZwave = zwaveRepository.save(nodeItem);
        List<Endpoint> newEndpoints = nodeItem.getEndpoint();
        for (int iE = 0; iE < newEndpoints.size(); iE++) {
            Endpoint endpoint = newEndpoints.get(iE);
            endpoint.setZwaveNo(saveZwave.getNo());
            endpoint.setCmdCls(endpoint.getScmdClses(endpoint.getCmdClses()));
            String endpointKey = endpoint.getGeneric() + "." + endpoint.getSpecific();
            endpoint.setNickname(Common.zwaveNickname(zWaveProperties, endpointKey));
            Endpoint saveEndpoint = endpointService.saveEndpoint(endpoint);
            List<CmdCls> newCmdCls = newEndpoints.get(iE).getCmdClses();
            for (int iCmdCls = 0; iCmdCls < newCmdCls.size(); iCmdCls++) {
                CmdCls cmdcls = newCmdCls.get(iCmdCls);
                cmdcls.setRptCmd(cmdcls.getSrptCmd(cmdcls.getRptCmds()));
                cmdcls.setEndpointNo(saveEndpoint.getNo());
                cmdClsRepository.save(newCmdCls.get(iCmdCls));
            }
        }
        return saveZwave;
    }

    /**
     * 신규 등록 ZWAVE 기기 디비 저장 신규 기기일 경우 {"result_data": {"newNodeId": xx}}
     * @param zwaveRequest
     * @param mqttPayload
     */
    public void saveGatewayCategory(ZWaveRequest zwaveRequest, int nodeId) {
        Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
        List<GatewayCategory> gatewayCategorys = gatewayCategoryRepository.findByGatewayNoAndNodeIdAndCategory(gateway.getNo(), nodeId, CategoryActive.gateway.zwave.name());
        if (gatewayCategorys.size() == 0) {
            GatewayCategory gatewayCategory = new GatewayCategory();
            gatewayCategory.setGatewayNo(gateway.getNo());
            gatewayCategory.setCategory(CategoryActive.gateway.zwave.name());
            gatewayCategory.setCategoryNo(CategoryActive.gateway.zwave.ordinal());
            gatewayCategory.setNodeId(nodeId);
            gatewayCategory.setStatus(status.add.name());
            gatewayCategory.setCreatedTime(new Date());
            gatewayCategory.setLastmodifiedTime(new Date());
            gatewayCategoryRepository.save(gatewayCategory);
        }
    }

    @Override
    public List deleteZwave(int gatewayNo, int nodeId) {
        // zwave 정보삭제
        gatewayCategoryRepository.deleteByGatewayNoAndNodeId(gatewayNo, nodeId);
        List rtnList = new ArrayList();
        List<ZWave> lstZWave = zwaveRepository.findByGatewayNoAndNodeId(gatewayNo, nodeId);
        for (ZWave zWave : lstZWave) {

            endpointService.deleteEndpoint(zWave);
            rtnList.add(zWave.getNo());
        }
        zwaveRepository.deleteByGatewayNoAndNodeId(gatewayNo, nodeId);
        return rtnList;
    }
    
    /**
     * Zwave 기기제어
     * @author lij
     * @throws JsonProcessingException
     * @throws InterruptedException
     */
    // 제어
    @Override
    public void zwaveBasicControl(MqttRequest mqttRequest) throws JsonProcessingException, InterruptedException {

        mqttRequest.setVersion("v1");
        mqttRequest.setSecurityOption("0");
        HashMap map = new HashMap<>();
        HashMap map1 = new HashMap<>();
        map1.put("set_data", map);
        mqttRequest.setSetData(map1);
        publish(mqttRequest);
    }

}
