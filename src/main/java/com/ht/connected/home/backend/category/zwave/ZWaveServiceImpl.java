package com.ht.connected.home.backend.category.zwave;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.category.zwave.certification.CertificationRepository;
import com.ht.connected.home.backend.category.zwave.certification.CertificationService;
import com.ht.connected.home.backend.category.zwave.cmdcls.CmdCls;
import com.ht.connected.home.backend.category.zwave.cmdcls.CmdClsRepository;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.BasicCommandClass;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.NetworkManagementInclusionCommandClass;
import com.ht.connected.home.backend.category.zwave.constants.commandclass.NetworkManagementProxyCommandClass;
import com.ht.connected.home.backend.category.zwave.endpoint.Endpoint;
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

/**
 * Rest API Zwave 요청/응답 처리 서비스 구현
 * @author 구정화
 */
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
    GatewayCategoryRepository gatewayCategoryRepository;

    @Autowired
    CertificationService certificationService;
    @Autowired
    MqttConfig.MqttGateway mqttGateway;

    @Autowired
    @Qualifier(value = "MqttOutbound")
    MqttPahoMessageHandler messageHandler;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ResponseEntity execute(HashMap<String, Object> req, ZWaveRequest zwaveRequest, boolean isCert) throws JsonProcessingException {
        logging.info("zwaveRequest.getClassKey()::::" + zwaveRequest.getClassKey());

        if (NetworkManagementInclusionCommandClass.INT_ID == zwaveRequest.getClassKey()) {
            HashMap map = new HashMap<>();
            map.put("mode", 1);
            req.put("set_data", map);
        }
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
        Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
        if (!isNull(gateway)) {
            int nodeId = 0;
            int endPointId = 0;
            if (!Objects.isNull(zwaveRequest.getNodeId())) {
                nodeId = zwaveRequest.getNodeId();
            }
            if (!Objects.isNull(zwaveRequest.getEndpointId())) {
                endPointId = zwaveRequest.getEndpointId();
            }
            String[] segments = new String[] { "/server", target, gateway.getModel(), gateway.getSerial(), "zwave", "certi",
                    ByteUtil.getHexString(zwaveRequest.getClassKey()), ByteUtil.getHexString(zwaveRequest.getCommandKey()), zwaveRequest.getVersion(),
                    ByteUtil.getHexString(nodeId), ByteUtil.getHexString(endPointId),
                    zwaveRequest.getSecurityOption() };
            topic = String.join("/", segments);
            logging.info("====================== ZWAVE PROTO MQTT PUBLISH TOPIC ======================");
            logging.info(topic);
        }
        return topic;
    }

    protected HashMap<String, Object> getPublishPayload(HashMap<String, Object> req) {
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
                    reportZWaveList(zwaveRequest, data);
                }
                certificationService.updateCertification(zwaveRequest, data);
            }

        }
        // 기기등록 모드
        if (zwaveRequest.getClassKey() == NetworkManagementInclusionCommandClass.INT_ID) {
            // 기기상태값모드 받은 경우
            if (zwaveRequest.getCommandKey() == NetworkManagementInclusionCommandClass.INT_NODE_ADD_STATUS) {
                // newNodeId 가 있을경우 등록 성공이고 없을경우 등록완료 전으로 상태 메세지를 확인한다.
                if (!Objects.isNull(mqttPayload.getResultData())) {
                    if (!isNull(mqttPayload.getResultData().get("newNodeId"))) {
                        // 0x52
                        int newNodeId = (int) mqttPayload.getResultData().get("newNodeId");
                        zwaveRequest.setClassKey(zwaveRequest.getClassKey());
                        // 0x02
                        zwaveRequest.setCommandKey(zwaveRequest.getCommandKey());
                        zwaveRequest.setNodeId(newNodeId);
                        zwaveRequest.setEndpointId(0);
                        zwaveRequest.setVersion("v1");
                        zwaveRequest.setSecurityOption("none");
                        saveGatewayCategory(zwaveRequest, newNodeId);
                    }
                    // 등록완료일경우 NodeGet.명령어 호출
                    // 호스트의 노드 리스트 호출을 한다.
                    if ("0".equals(mqttPayload.getResultData().getOrDefault("status", "9").toString())) {
                        // 서버에 기기리스트를 요청함.
                        zwaveRequest.setClassKey(NetworkManagementProxyCommandClass.INT_ID);
                        zwaveRequest.setCommandKey(NetworkManagementProxyCommandClass.INT_NODE_LIST_GET);
                        String topic = getMqttPublishTopic(zwaveRequest, "host");
                        publish(topic);

                    }
                }
                String status = "status null ";
                if (!Objects.isNull(mqttPayload.getResultData())) {
                    status = mqttPayload.getResultData().getOrDefault("status", 0).toString();
                }
                logging.info(String.format("<< SERIAL NO : %s, NODE_ADD_STATUS : %s >>", zwaveRequest.getSerialNo(), status));
                certificationService.updateCertification(zwaveRequest, payload);

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

    // 제어
    @Override
    public void execute(Map map, boolean isCert) throws JsonProcessingException {
        String topic = getZwaveTopic(map);
        HashMap map1 = getPublishPayload((HashMap) map);
        publish(topic, map1);
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
        if (!isNull(gateway)) {
            List<ZWave> lstZwave = zwaveRepository.findByGatewayNo(gateway.getNo());
            ZWaveReport zwaveReport = objectMapper.readValue(data, ZWaveReport.class);
            // 기기 리스트에 대한 정보일 경우
            if (zwaveReport.getNodelist() != null) {
                List<ZWave> nodeListItem = (List<ZWave>) zwaveReport.getNodelist();
                for (int i = 0; i < nodeListItem.size(); i++) {
                    
                    ZWave nodeItem = nodeListItem.get(i);
                    int nodeId = nodeItem.getNodeId();
                    boolean bInsert= false;
                    for (int j = 0; j < lstZwave.size(); j++) {
                        ZWave zwave = lstZwave.get(j);
                        //host 노드리스트와 DB 노드리스트를 비교하여 없으면 insert시킴
                        //int nodeId = (int) nodeItem.getOrDefault("nodeId", 0);
                        // node status "" 경우에는 node 가 신규로 들어왔으나 host 에서 신규노드로 확인이 안된경 node status 0x01일 경우 신규 등록 기기
                        if (nodeId == zwave.getNodeId()) {
                            bInsert=true;
                            if(Common.empty(zwave.getStatus())) {
                                Map req = new HashMap();
                                req.put("serial", gateway.getSerial());
                                req.put("nodeId", nodeId);
                                req.put("option", nodeItem.getSecurity());
                                ZWaveRequest appZwaveRequest = new ZWaveRequest((HashMap<String, Object>) req, NetworkManagementInclusionCommandClass.INT_ID,
                                        NetworkManagementInclusionCommandClass.INT_NODE_ADD, "v1");
                                String topic = getMqttPublishTopic(appZwaveRequest, Target.app.name());
                                publish(topic, nodeItem);
                                zwave.setStatus("0x01");
                                zwaveRepository.save(zwave);
                            }
                        }
                    }
                    /**
                    int nodeId = (int) ((HashMap<String, Object>) nodeItem).getOrDefault("nodeid", 0);
                    boolean bInsert= false;
                    //신규노드 아이디에대한 app 노티를 해줌.
                    for (int j = 0; j < lstZwave.size(); j++) {
                        Zwave zwave = lstZwave.get(j);
                        //host 노드리스트와 DB 노드리스트를 비교하여 없으면 insert시킴
                        //TODO nodeId로 변경하기로함
                        //int nodeId = (int) nodeItem.getOrDefault("nodeId", 0);
                        // node status "" 경우에는 node 가 신규로 들어왔으나 host 에서 신규노드로 확인이 안된경 node status 0x01일 경우 신규 등록 기기
                        if (nodeId == zwave.getNodeId()) {
                            bInsert=true;
                            if(Common.empty(zwave.getStatus())) {
                                Map req = new HashMap();
                                req.put("serial", gateway.getSerial());
                                req.put("nodeId", nodeId);
                                req.put("option", nodeItem.getOrDefault("security", ""));
                                ZwaveRequest appZwaveRequest = new ZwaveRequest((HashMap<String, Object>) req, NetworkManagementInclusionCommandClass.INT_ID,
                                        NetworkManagementInclusionCommandClass.INT_NODE_ADD, "v1");
                                String topic = getMqttPublishTopic(appZwaveRequest, Target.app.name());
                                publish(topic, nodeItem);
                                zwave.setStatus("0x01");
                                zwaveRepository.save(zwave);
                            }
                        }
                    }
                    */
                    //등록안된 node 일경우 insert함.
                    if(!bInsert) {
                        nodeItem.setGatewayNo(gateway.getNo());
                        nodeItem.setCreratedTime(new Date());
                        ZWave saveZwave = zwaveRepository.save(nodeItem);
                        List<Endpoint> newEndpoints = nodeItem.getEndpoint();
                        for(int iE = 0; iE<newEndpoints.size();iE++) {
                            Endpoint endpoint = newEndpoints.get(iE);
                            endpoint.setZwaveNo(saveZwave.getNo());
                            Endpoint saveEndpoint = endpointRepository.save(endpoint);
                            List<CmdCls> newCmdCls = newEndpoints.get(iE).getCmdclss();
                            for(int iCmdCls = 0; iE< newCmdCls.size();iCmdCls++) {
                                CmdCls cmdcls = newCmdCls.get(iCmdCls);
                                cmdcls.setEndpointNo(saveEndpoint.getNo());
                                cmdClsRepository.save(newCmdCls.get(iCmdCls));
                            }   
                        }
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
        int iRtn = zwaveRepository.setFixedEventAndStatusForNo(event.delete.name(), status.delete.name(), no);
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
        mqttRequest.setSetData((HashMap<String, Object>) new HashMap().put("set_data", map));
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

}
