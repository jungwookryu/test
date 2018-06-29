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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.category.ir.IRService;
import com.ht.connected.home.backend.category.zwave.certification.CertificationRepository;
import com.ht.connected.home.backend.category.zwave.certification.CertificationService;
import com.ht.connected.home.backend.category.zwave.cmdcls.CmdCls;
import com.ht.connected.home.backend.category.zwave.cmdcls.CmdClsRepository;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.AlarmCommandClass;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.BasicCommandClass;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.BinarySwitchCommandClass;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.CommandClass;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.CommandClassFactory;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.NetworkManagementBasicCommandClass;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.NetworkManagementInclusionCommandClass;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.NetworkManagementProxyCommandClass;
import com.ht.connected.home.backend.category.zwave.endpoint.Endpoint;
import com.ht.connected.home.backend.category.zwave.endpoint.EndpointReportByApp;
import com.ht.connected.home.backend.category.zwave.endpoint.EndpointRepository;
import com.ht.connected.home.backend.common.ByteUtil;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.common.MqttCommon;
import com.ht.connected.home.backend.config.service.MqttConfig;
import com.ht.connected.home.backend.gateway.Gateway;
import com.ht.connected.home.backend.gateway.GatewayRepository;
import com.ht.connected.home.backend.gatewayCategory.CategoryActive;
import com.ht.connected.home.backend.gatewayCategory.GatewayCategory;
import com.ht.connected.home.backend.gatewayCategory.GatewayCategoryRepository;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;
import com.ht.connected.home.backend.service.mqtt.MqttPayload;
import com.ht.connected.home.backend.service.mqtt.MqttRequest;
import com.ht.connected.home.backend.service.mqtt.Target;
import com.ht.connected.home.backend.user.User;
import com.ht.connected.home.backend.user.UserRepository;
import com.ht.connected.home.backend.userGateway.UserGateway;
import com.ht.connected.home.backend.userGateway.UserGatewayRepository;

@Service
public class ZWaveServiceImpl extends CrudServiceImpl<ZWave, Integer> implements ZWaveService {

    private ZWaveRepository zwaveRepository;

    enum event {
        delete, active, failed
    }

    enum status {
        add, delete, active, failed
    }

    @Autowired
    public ZWaveServiceImpl(ZWaveRepository zwaveRepository) {
        super(zwaveRepository);
        this.zwaveRepository = zwaveRepository;
    }

    private static final Log logging = LogFactory.getLog(ZWaveServiceImpl.class);

    Logger logger = LoggerFactory.getLogger(ZWaveServiceImpl.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    GatewayRepository gatewayRepository;

    @Autowired
    CertificationRepository certificationRepository;

    @Autowired
    UserGatewayRepository userGatewayRepository;

    @Autowired
    EndpointRepository endpointRepository;

    @Autowired
    CmdClsRepository cmdClsRepository;

    @Autowired
    IRService irService;

    @Autowired
    GatewayCategoryRepository gatewayCategoryRepository;

    @Autowired
    CertificationService certificationService;
    @Autowired
    MqttConfig.MqttGateway mqttGateway;

    @Autowired
    @Qualifier(value = "MqttOutbound")
    MqttPahoMessageHandler messageHandler;

    @Autowired
    Properties zWaveProperties;
    
    @Autowired
    @Qualifier("zWaveFunctionProperties")
    Properties zWaveFunctionProperties;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ResponseEntity execute(HashMap<String, Object> req, ZWaveRequest zwaveRequest, boolean isCert) throws JsonProcessingException {
        logging.info("zwaveRequest.getClassKey()::::" + zwaveRequest.getClassKey());
        return publish(req, zwaveRequest);
    }

    @Override
    public ResponseEntity publish(HashMap<String, Object> req, ZWaveRequest zwaveRequest) throws JsonProcessingException {

        ResponseEntity response = new ResponseEntity(HttpStatus.BAD_REQUEST);;
        String topic = getMqttPublishTopic(zwaveRequest);
        if (topic.length() > 0) {
            publish(topic, getPublishPayload(req));
            response = new ResponseEntity(HttpStatus.OK);
        }
        return response;
    }

    @Override
    public void subscribe(Object zwaveRequest, Object payload) throws Exception {
        if (zwaveRequest instanceof ZWaveRequest && payload instanceof String) {
            ZWaveRequest reqZwaveRequest = (ZWaveRequest) zwaveRequest;
            subscribe(reqZwaveRequest, (String) payload);
        }
    }

    @Transactional
    public void subscribe(ZWaveRequest zwaveRequest, String payload) throws JsonParseException, JsonMappingException, IOException, Exception {

        MqttPayload mqttPayload = new MqttPayload();
        if (!Common.empty(payload)) {
            mqttPayload = objectMapper.readValue(payload, MqttPayload.class);
        }
        if (zwaveRequest.getClassKey() == BasicCommandClass.INT_ID) {
            if (zwaveRequest.getCommandKey() == BasicCommandClass.INT_BASIC_REPORT) {
                certificationService.updateCertification(zwaveRequest, payload);
            }
        }
        // 0X52
        if (zwaveRequest.getClassKey() == NetworkManagementProxyCommandClass.INT_ID) {
            Object resultData = mqttPayload.getResultData();
            String data = "";
            if (!isNull(resultData)) {
                data = objectMapper.writeValueAsString(resultData);
                // OX02
                /**
                 * 기기 리스트 수신시 새로 등록한 기기가 있을경우는 새로 등록 없을 경우는 업데이트함.0x52 0x02 모드일경우
                 */
                if (zwaveRequest.getCommandKey() == NetworkManagementProxyCommandClass.INT_NODE_LIST_REPORT) {
                    if (-1 == zwaveRequest.getNodeId() || 0 == zwaveRequest.getNodeId()) {
                        reportZWaveList(zwaveRequest, data);
                    }
                    // 신규 등록 기기 정보
                    else {
                        Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
                        zwaveRequest.setGatewayNo(gateway.getNo());
                        List<ZWave> lstOriginalZwave = zwaveRepository.findByGatewayNoAndNodeId(zwaveRequest.getGatewayNo(), zwaveRequest.getNodeId());
                        if(lstOriginalZwave.size()==0) {
                            saveGatewayCategory(zwaveRequest, zwaveRequest.getNodeId());
                            if (!isNull(gateway)) {
                                ZWaveReport zwaveReport = objectMapper.readValue(data, ZWaveReport.class);
                                // 기기 리스트에 대한 정보일 경우
                                if (zwaveReport.getNodelist() != null) {
                                    List<ZWave> nodeListItem = (List<ZWave>) zwaveReport.getNodelist();
                                    for (int i = 0; i < nodeListItem.size(); i++) {
                                        ZWave nodeItem = nodeListItem.get(i);
                                        saveZWaveList(zwaveRequest, nodeItem, gateway);
                                        // syncZWaveList(zwaveRequest, nodeItem, gateway);
                                        String exeTopic = String.format("/" + Target.server.name() + "/" + Target.app.name() + "/%s/%s/zwave/device/registration", gateway.getModel(),
                                                gateway.getSerial());
                                        publish(exeTopic);
                                    }
                                }
                            }
                        }

                    }
                    certificationService.updateCertification(zwaveRequest, data);
                }

            }
        }
        // 기기삭제 모드 0x34/0x04 결과
        if (zwaveRequest.getClassKey() == NetworkManagementInclusionCommandClass.INT_ID) {
            // 기기삭제 상태값 받은 경우
            if (zwaveRequest.getCommandKey() == NetworkManagementInclusionCommandClass.INT_NODE_REMOVE_STATUS ||
                    zwaveRequest.getCommandKey() == NetworkManagementInclusionCommandClass.INT_FAILED_NODE_STATUS) {
                HashMap resultMapData = mqttPayload.getResultData();
                int nodeId = -1;
                if (resultMapData != null) {
                    int status = (int) resultMapData.getOrDefault("status", -1);
                    if (status == 0) {
                        nodeId = (int) resultMapData.getOrDefault("newNodeId", zwaveRequest.getNodeId());
                    }
                }
                if (nodeId != -1) {
                    Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
                    deleteZwave(gateway.getNo(),nodeId);
                    String exeTopic = String.format("/" + Target.server.name() + "/" + Target.app.name() + "/%s/%s/zwave/device/remove", zwaveRequest.getModel(),
                            zwaveRequest.getSerialNo());
                    publish(exeTopic);
                }

            }

        }
        // 기기 초기화 결과 0x4D/0x07
        if (zwaveRequest.getClassKey() == NetworkManagementBasicCommandClass.INT_ID) {
            // 기기상태값모드 받은 경우
            if (zwaveRequest.getCommandKey() == NetworkManagementBasicCommandClass.DEFAULT_SET_COMPLETE) {
                // 해당기기의 정보를 모두 삭제한다.
                hostReset(zwaveRequest);
                String exeTopic = String.format("/" + Target.server.name() + "/" + Target.app.name() + "/%s/%s/manager/product/remove", zwaveRequest.getModel(),
                        zwaveRequest.getSerialNo());
                publish(exeTopic);
                
            }
        }
        // 기기 상태 결과
        if (zwaveRequest.getClassKey() == AlarmCommandClass.INT_ID) {
            // 기기상태값모드 받은 경우
            if (zwaveRequest.getCommandKey() == AlarmCommandClass.ALARM_REPORT) {
                // 해당기기의 정보를 모두 삭제한다.
                notificationZWave(zwaveRequest);
                String exeTopic = String.format("/" + Target.server.name() + "/" + Target.app.name() + "/%s/%s/manager/product/remove", zwaveRequest.getModel(),
                        zwaveRequest.getSerialNo());
                publish(exeTopic);
                
            }
        }

    }

    // 제어
    @Override
    public void execute(Map map, boolean isCert) throws JsonProcessingException {
        String topic = getZwaveTopic(map);
        HashMap map1 = getPublishPayload((HashMap) map);
        publish(topic, map1);
    }

    /**
     * Zwave 기기제어
     * @author lij
     * @throws JsonProcessingException
     */
    // 제어
    @Override
    public void zwaveControl(ZWaveControl zWaveControl) throws JsonProcessingException {

        Gateway gateway = gatewayRepository.findOne(zWaveControl.getGateway_no());
        ZWave zwave = zwaveRepository.findOne(zWaveControl.getZwave_no());
        Endpoint endpoint = endpointRepository.findOne(zWaveControl.getEndpoint_no());

        MqttRequest mqttRequest = new MqttRequest();
        mqttRequest.setNodeId(zwave.getNodeId());
        if(endpoint!=null) {
            mqttRequest.setEndpointId(endpoint.getEpid());
        }
        mqttRequest.setSerialNo(gateway.getSerial());
        mqttRequest.setModel(gateway.getModel());
        mqttRequest.setClassKey(BasicCommandClass.INT_ID);
        mqttRequest.setCommandKey(BasicCommandClass.INT_BASIC_SET);
        mqttRequest.setVersion("v1");
        mqttRequest.setSecurityOption("0");

        HashMap map = new HashMap<>();
        map.put("value", zWaveControl.getValue());
        // map.put("currentValue", zWaveControl.getCurrentValue());
        // map.put("duration", zWaveControl.getDuration());
        HashMap map1 = new HashMap<>();
        map1.put("set_data", map);
        mqttRequest.setSetData(map1);
        publish(mqttRequest);
        if(endpoint!=null) {
            endpoint.setStatus(zWaveControl.getValue());
        }
    }

    /**
     * mqtt publish 토픽 생성
     * @param topicLeadingPath //0 none, 1 crc
     * @return
     */
    public String getZwaveTopic(Map map) {
        String topic = "";
        String nodeId = ByteUtil.getHexString((Integer) map.getOrDefault("nodeId", 0));;
        String endPointId = ByteUtil.getHexString((Integer) map.getOrDefault("endpointId", 0));
        String serial = (String) map.getOrDefault("serial", "01234567");
        String commandKey = (String) map.getOrDefault("cmdkey", "0x00");
        String classkey = (String) map.getOrDefault("classkey", "0x00");
        String version = (String) map.getOrDefault("version", "v1");
        String option = Integer.toString((int) map.getOrDefault("option", 0));
        String model = (String) map.getOrDefault("model", "");
        String[] segments = new String[] { "/server", Target.host.name(), model, serial, "zwave", "certi",
                classkey, commandKey, version, nodeId, endPointId, option };
        topic = String.join("/", segments);
        logging.info("====================== ZWAVE PROTO MQTT PUBLISH TOPIC ======================");
        logging.info(topic);
        return topic;

    }

    private void reportZWaveList(ZWaveRequest zwaveRequest, String data) throws JsonParseException, JsonMappingException, IOException, JSONException {

        Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
        zwaveRequest.setGatewayNo(gateway.getNo());
        if (!isNull(gateway)) {
            //DB 데이터
            List<ZWave> lstZwave = zwaveRepository.findByGatewayNo(gateway.getNo());
            ZWaveReport zwaveReport = objectMapper.readValue(data, ZWaveReport.class);
            // 기기 리스트에 대한 정보일 경우
            if (zwaveReport.getNodelist() != null) {
                //HOST 데이터
                List<ZWave> nodeListItem = (List<ZWave>) zwaveReport.getNodelist();
                // 추가
                for (int i = 0; i < nodeListItem.size(); i++) {
                    ZWave nodeItem = nodeListItem.get(i);
                    //HOST node ID
                    int nodeId = nodeItem.getNodeId();
                    boolean bInsert = false;
                    for (int j = 0; j < lstZwave.size(); j++) {
                        ZWave zwave = lstZwave.get(j);
                        // host 노드리스트와 DB 노드리스트를 비교하여 없으면 insert시킴
                        // int nodeId = (int) nodeItem.getOrDefault("nodeId", 0);
                        // node status "" 경우에는 node 가 신규로 들어왔으나 host 에서 신규노드로 확인이 안된경 node status add일 경우 신규 등록 기기
                        //HOST ID == DB node ID
                        if (nodeId == zwave.getNodeId()) {
                            bInsert = true;
                        }
                    }
                    // 등록안되어있고 node의 status가 delete가 아닐경우 일경우 insert함.
                    if (!bInsert) {
                        // zwave nodeId Category 별 저장함.
                        saveZWaveList(zwaveRequest, nodeItem, gateway);
                        String exeTopic = String.format("/" + Target.server.name() + "/" + Target.app.name() + "/%s/%s/zwave/device/registration", gateway.getModel(),
                                gateway.getSerial());
                        publish(exeTopic);
                    }
                }
                // host에는 있고 server에는 없을경우 삭제한다.
                // 추가
                for (int j = 0; j < lstZwave.size(); j++) {
                    boolean bDelete = true;
                    ZWave zwave = lstZwave.get(j);
                    for (int i = 0; i < nodeListItem.size(); i++) {
                        ZWave nodeItem = nodeListItem.get(i);
                        int nodeId = nodeItem.getNodeId();
                        // host 노드리스트와 DB 노드리스트를 비교하여 없으면 delete 시킴
                        if (nodeId == zwave.getNodeId()) {
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

        } else {
            logging.info(String.format("Gateway Serial Number(%s) is not registered", zwaveRequest.getSerialNo()));;
        }
    }

    @Override
    public void publish(Object req, Object zwaveRequest) {
        // TODO Auto-generated method stub
    }

    // 삭제 토픽
    @Override
    @Transactional
    public int deleteByNo(int no) throws JsonProcessingException {
        // TODO DB 에서 기기삭제 status 로 update
        ZWave zwave = zwaveRepository.getOne(no);
        Gateway gateway = gatewayRepository.getOne(zwave.getGatewayNo());
        int iRtn = zwaveRepository.setFixedStatusForNo(status.delete.name(), no);
        MqttRequest mqttRequest = new MqttRequest();
        mqttRequest.setSerialNo(gateway.getSerial());
        mqttRequest.setModel(gateway.getModel());
        mqttRequest.setClassKey(NetworkManagementInclusionCommandClass.INT_ID);
        mqttRequest.setCommandKey(NetworkManagementInclusionCommandClass.INT_NODE_REMOVE);
        mqttRequest.setTarget(Target.host.name());
        // TODO Gateway host version
        // mqttRequest.setVersion(gateway.getVersion());
        HashMap map = new HashMap<>();
        map.put("mode", 1);
        HashMap map1 = new HashMap<>();
        map1.put("set_data", map);
        mqttRequest.setSetData(map1);
        publish(mqttRequest);
        return iRtn;

    }

    public void publish(MqttRequest mqttRequest) throws JsonProcessingException {

        publish(MqttCommon.getMqttPublishTopic(mqttRequest), mqttRequest.getSetData());
    }

    public void publish(String topic) throws JsonProcessingException {
        publish(topic, new HashMap());
    }

    public void publish(String topic, HashMap<String, Object> publishPayload) throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(publishPayload);
        publish(topic, payload);
    }

    public void publish(String topic, String payload) {
        messageHandler.setDefaultTopic(topic);
        logger.info("publish topic:::::::::::" + topic);
        mqttGateway.sendToMqtt(payload);
    }

    @Override
    public int getByUserEmailAndNo(String userEmail, int no) {
        // TODO Auto-generated method stub
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
    public void subscribeInit(Gateway gateway) throws JsonProcessingException {
        // TODO 기기리스트 가져오기 topic
        MqttRequest mqttRequest = new MqttRequest(gateway);
        mqttRequest.setClassKey(NetworkManagementProxyCommandClass.INT_ID);
        mqttRequest.setCommandKey(NetworkManagementProxyCommandClass.INT_NODE_LIST_GET);
        mqttRequest.setVersion("v1");
        mqttRequest.setNodeId(00);
        mqttRequest.setEndpointId(00);
        String requestTopic = MqttCommon.getMqttPublishTopic(mqttRequest, Target.host.name());
        publish(requestTopic);
        // TODO 기기 상태정보 가져오기
    }

    @Override
    public int deleteByGatewayNo(int gatewayNo) {
        // int zwaveUpdateCnt =zwaveRepository.setFixedStatusForGatewayNo(status.delete.name(), gatewayNo);
        // return zwaveUpdateCnt;
        return 0;
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
                ZWaveReportByApp zWaveReportByApp = new ZWaveReportByApp();
                zWaveReportByApp.setZwaveNo(zwave.getNo());
                zWaveReportByApp.setNodeId(zwave.getNodeId());
                zWaveReportByApp.setNicname(zwave.getNickname());
                zWaveReportByApp.setStatus(zwave.getStatus());

                List<EndpointReportByApp> lstEndpointReportByApp = new ArrayList<>();
                List<Endpoint> lstEndpoint = endpointRepository.findByZwaveNo(zwave.getNo());
                for (int j = 0; j < lstEndpoint.size(); j++) {
                    Endpoint endpoint = lstEndpoint.get(j);
                    EndpointReportByApp endpointReportByApp = new EndpointReportByApp();
                    endpointReportByApp.setEndpointNo(endpoint.getNo());
                    endpointReportByApp.setEpStatus(endpoint.getStatus());
                    endpointReportByApp.setEpid(endpoint.getEpid());
                    endpointReportByApp.setNickname(endpoint.getNickname());
                    lstEndpointReportByApp.add(endpointReportByApp);
                }
                zWaveReportByApp.setEndpoints(lstEndpointReportByApp);
                rtnList.add(zWaveReportByApp);
            }

        }
        map.put("nodelist", rtnList);
        map.put("nodeCnt", lstZWave.size() - 1);
        return map;
    }

    private String getDefaultNickName(String basic, String generic, String specific) {
        return "";
    }

    /**
     * mqtt publish 토픽 생성
     * @param topicLeadingPath
     * @return
     */
    public String getMqttPublishTopic(ZWaveRequest zwaveRequest) {
        return getMqttPublishTopic(zwaveRequest, Target.host.name());
    }

    /**
     * mqtt publish 토픽 생성
     * @param topicLeadingPath
     * @return
     */
    public String getMqttPublishTopic(ZWaveRequest zwaveRequest, String target) {
        String topic = "";
        int nodeId = 0;
        int endPointId = 0;
        if (!Objects.isNull(zwaveRequest.getNodeId())) {
            nodeId = zwaveRequest.getNodeId();
        }
        if (!Objects.isNull(zwaveRequest.getEndpointId())) {
            endPointId = zwaveRequest.getEndpointId();
        }
        String[] segments = new String[] { "/server", target, zwaveRequest.getModel(), zwaveRequest.getSerialNo(), "zwave", "certi",
                ByteUtil.getHexString(zwaveRequest.getClassKey()), ByteUtil.getHexString(zwaveRequest.getCommandKey()), zwaveRequest.getVersion(),
                ByteUtil.getHexString(nodeId), ByteUtil.getHexString(endPointId),
                zwaveRequest.getSecurityOption() };
        topic = String.join("/", segments);
        logging.info("====================== ZWAVE PROTO MQTT PUBLISH TOPIC ======================");
        logging.info(topic);
        return topic;
    }

    private HashMap<String, Object> getPublishPayload(HashMap<String, Object> req) {
        HashMap<String, Object> payload = new HashMap<>();
        Object payloadData = req.get("get_data");
        if (!isNull(payloadData)) {
            payload.put("get_data", payloadData);
        }
        payloadData = req.get("set_data");
        if (!isNull(payloadData)) {
            payload.put("set_data", payloadData);
        }
        return payload;
    }

    private void deleteZwave(int gatewayNo, int nodeId) {
        // zwave 정보삭제
        gatewayCategoryRepository.deleteByGatewayNoAndNodeId(gatewayNo, nodeId);
        List<ZWave> lstZWave = zwaveRepository.findByGatewayNoAndNodeId(gatewayNo, nodeId);
        for (ZWave zWave : lstZWave) {
            List<Endpoint> lstEndpoint = endpointRepository.findByZwaveNo(zWave.getNo());
            for (Endpoint endpoint : lstEndpoint) {
                cmdClsRepository.deleteByEndpointNo(endpoint.getNo());
            }
            endpointRepository.deleteByZwaveNo(zWave.getNo());
        }
        zwaveRepository.deleteByGatewayNoAndNodeId(gatewayNo, nodeId);
    }

    private void hostReset(ZWaveRequest zwaveRequest) {
        // host 정보삭제
        Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
        gatewayRepository.delete(gateway.getNo());
        userGatewayRepository.deleteByGatewayNo(gateway.getNo());
        // zwaveNo
        gatewayCategoryRepository.deleteByGatewayNo(gateway.getNo());
        List<ZWave> lstZWave = zwaveRepository.findByGatewayNo(gateway.getNo());
        for (ZWave zWave : lstZWave) {
            List<Endpoint> lstEndpoint = endpointRepository.findByZwaveNo(zWave.getNo());
            for (Endpoint endpoint : lstEndpoint) {
                cmdClsRepository.deleteByEndpointNo(endpoint.getNo());
            }
            endpointRepository.deleteByZwaveNo(zWave.getNo());
        }
        zwaveRepository.deleteByGatewayNo(gateway.getNo());
        irService.deleteIrs(gateway.getNo(), "");

    }

    private void saveZWaveList(ZWaveRequest zwaveRequest, ZWave nodeItem, Gateway gateway) {
        saveGatewayCategory(zwaveRequest, nodeItem.getNodeId());

        nodeItem.setGatewayNo(gateway.getNo());
        nodeItem.setCreratedTime(new Date());
        String nodeKey = nodeItem.getGeneric() + "." + nodeItem.getSpecific();
        nodeItem.setNickname(zwaveNickname(zWaveProperties, nodeKey));
        ZWave saveZwave = zwaveRepository.save(nodeItem);
        List<Endpoint> newEndpoints = nodeItem.getEndpoint();
        for (int iE = 0; iE < newEndpoints.size(); iE++) {
            Endpoint endpoint = newEndpoints.get(iE);
            endpoint.setZwaveNo(saveZwave.getNo());
            endpoint.setCmdCls(endpoint.getScmdClses(endpoint.getCmdClses()));
            String endpointKey = endpoint.getGeneric() + "." + endpoint.getSpecific();
            endpoint.setNickname(zwaveNickname(zWaveProperties, endpointKey));
            Endpoint saveEndpoint = saveEndpoint(endpoint);
            List<CmdCls> newCmdCls = newEndpoints.get(iE).getCmdClses();
            for (int iCmdCls = 0; iCmdCls < newCmdCls.size(); iCmdCls++) {
                CmdCls cmdcls = newCmdCls.get(iCmdCls);
                cmdcls.setRptCmd(cmdcls.getSrptCmd(cmdcls.getRptCmds()));
                cmdcls.setEndpointNo(saveEndpoint.getNo());
                cmdClsRepository.save(newCmdCls.get(iCmdCls));
            }
        }
    }

    /**
     * 신규 등록 ZWAVE 기기 디비 저장 신규 기기일 경우 {"result_data": {"newNodeId": xx}}
     * @param zwaveRequest
     * @param mqttPayload
     */
    private void saveGatewayCategory(ZWaveRequest zwaveRequest, int nodeId) {
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
    
    private String zwaveNickname(Properties properties, String key) {
        
        String rtnNickname = "Device";
        if(null!=properties) {
            if(!StringUtils.isEmpty(properties.getProperty(key))) {
                rtnNickname = properties.getProperty(key).replace("SPECIFIC_TYPE_","").replace("_BINARY","").replace("_"," ").toLowerCase();
            }
        }
        return rtnNickname;
    }
    private Endpoint endpointType(Endpoint endpoint) {
        CommandClass commandClass = CommandClassFactory.createCommandClass(BinarySwitchCommandClass.ID);
        String deviceType = "";
        String nicknameType = "";
        String functionType = "";
        List<CmdCls> cmdCls  = endpoint.getCmdClses();
            
        commandClass = CommandClassFactory.createSCmdClass(endpoint);
        
        endpoint.setDeviceType(commandClass.getDeviceType());
        endpoint.setDeviceNickname(commandClass.getNicknameType());
        endpoint.setDeviceFunctions(commandClass.getFunctionType());
        nicknameType = commandClass.getNicknameType();
        functionType = commandClass.getFunctionType(); 
        return endpoint;
    }

    private Endpoint saveEndpoint(Endpoint endpoint) {
        endpoint = endpointType(endpoint);
        endpointRepository.save(endpoint);
        return null;
    }
    
    private void notificationZWave(ZWaveRequest zwaveRequest) {
        
    }

}
