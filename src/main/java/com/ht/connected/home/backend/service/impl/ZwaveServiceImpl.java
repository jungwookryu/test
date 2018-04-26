package com.ht.connected.home.backend.service.impl;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.common.ByteUtil;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.config.service.MqttConfig.MqttGateway;
import com.ht.connected.home.backend.config.service.ZwaveClassKey;
import com.ht.connected.home.backend.config.service.ZwaveCommandKey;
import com.ht.connected.home.backend.model.dto.MqttPayload;
import com.ht.connected.home.backend.model.dto.Target;
import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.model.entity.Certification;
import com.ht.connected.home.backend.model.entity.Gateway;
import com.ht.connected.home.backend.model.entity.Zwave;
import com.ht.connected.home.backend.repository.CertificationRepository;
import com.ht.connected.home.backend.repository.GateWayRepository;
import com.ht.connected.home.backend.repository.UserGatewayRepository;
import com.ht.connected.home.backend.repository.UsersRepository;
import com.ht.connected.home.backend.repository.ZwaveRepository;
import com.ht.connected.home.backend.service.GateWayService;
import com.ht.connected.home.backend.service.ZwaveService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

/**
 * Rest API Zwave 요청/응답 처리 서비스 구현
 * @author 구정화
 */
@Service
public class ZwaveServiceImpl extends CrudServiceImpl<Zwave, Integer> implements ZwaveService {

    private ZwaveRepository zwaveRepository;

    @Autowired
    public ZwaveServiceImpl(ZwaveRepository zwaveRepository) {
        super(zwaveRepository);
        this.zwaveRepository = zwaveRepository;
    }

    private static final Log logging = LogFactory.getLog(ZwaveServiceImpl.class);

    Logger logger = LoggerFactory.getLogger(ZwaveServiceImpl.class);
    @Autowired
    UsersRepository userRepository;

    @Autowired
    GateWayRepository gatewayRepository;

    @Autowired
    CertificationRepository certificationRepository;

    @Autowired
    UserGatewayRepository userGatewayRepository;
    @Autowired
    GateWayService gateWayService;

    @Autowired
    BeanFactory beanFactory;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ResponseEntity execute(HashMap<String, Object> req, ZwaveRequest zwaveRequest, boolean isCert) throws JsonProcessingException {
        logging.info("zwaveRequest.getClassKey()::::" + zwaveRequest.getClassKey());

        if (ZwaveClassKey.NETWORK_MANAGEMENT_PROXY == zwaveRequest.getClassKey() ||
                ZwaveClassKey.BASIC == zwaveRequest.getClassKey()) {
            if ((zwaveRequest.getCommandKey() == ZwaveCommandKey.NODE_LIST_REPORT)
                    || (zwaveRequest.getCommandKey() == ZwaveCommandKey.NODE_INFO_CACHED_REPORT)) {
                return getPayload(req, zwaveRequest);
            }

        }
        if (ZwaveClassKey.NETWORK_MANAGEMENT_INCLUSION == zwaveRequest.getClassKey()) {
            // if ((zwaveRequest.getCommandKey() == ZwaveCommandKey.NODE_ADD_STATUS)
            // || (zwaveRequest.getCommandKey() == ZwaveCommandKey.NODE_REMOVE_STATUS)
            // || (zwaveRequest.getCommandKey() == ZwaveCommandKey.FAILED_NODE_REMOVE_STATUS)
            // || (zwaveRequest.getCommandKey() == ZwaveCommandKey.FAILED_NODE_REPLACE_STATUS)
            // || (zwaveRequest.getCommandKey() == ZwaveCommandKey.NODE_NEIGHBOR_UPDATE_STATUS)
            // || (zwaveRequest.getCommandKey() == ZwaveCommandKey.RETURN_ROUTE_ASSIGN_COMPLETE)
            // || (zwaveRequest.getCommandKey() == ZwaveCommandKey.RETURN_ROUTE_DELETE_COMPLETE)) {
            getPayload(req, zwaveRequest);
            // }
            HashMap map = new HashMap<>();
            map.put("mode", 1);
            ObjectMapper mapper = new ObjectMapper();
            String str = mapper.writeValueAsString(map);
            req.put("set_data", map);

        }
        if (ZwaveClassKey.BASIC == zwaveRequest.getClassKey()) {
            if (zwaveRequest.getCommandKey() == ZwaveCommandKey.BASIC_REPORT) {
                getPayload(req, zwaveRequest);
            }
        }
        return publish(req, zwaveRequest);
    }

    @Override
    public ResponseEntity publish(HashMap<String, Object> req, ZwaveRequest zwaveRequest) throws JsonProcessingException {

        ResponseEntity response = new ResponseEntity(HttpStatus.BAD_REQUEST);;
        String topic = getMqttPublishTopic(zwaveRequest);
        if (topic.length() > 0) {
            publish(topic, getPublishPayload(req));
            response = new ResponseEntity(HttpStatus.OK);
        }
        return response;
    }

    public void publish(String topic, HashMap<String, Object> publishPayload) throws JsonProcessingException {

        MqttPahoMessageHandler messageHandler = (MqttPahoMessageHandler) beanFactory.getBean("MqttOutbound");
        messageHandler.setDefaultTopic(topic);
        MqttGateway gateway = beanFactory.getBean(MqttGateway.class);
        String payload = objectMapper.writeValueAsString(publishPayload);
        logger.info("publish topic:::::::::::" + topic);
        gateway.sendToMqtt(payload);
    }

    public void publish(String topic, int classKey) {
        // 불필요한 publish를 제거함.
        publish(topic);
    }

    public void publish(String topic) {
        MqttPahoMessageHandler messageHandler = (MqttPahoMessageHandler) beanFactory.getBean("MqttOutbound");
        messageHandler.setDefaultTopic(topic);
        MqttGateway gateway = beanFactory.getBean(MqttGateway.class);
        gateway.sendToMqtt("");
    }

    /**
     * 인증프로토콜의 경우 디비에 JSON을 저장하는 기능
     * @param certPayload
     */
    public void updateCertification(ZwaveRequest zwaveRequest, String payload) {
        Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
        if (!isNull(gateway)) {
            Certification certification = new Certification();
            certification.setPayload(payload);
            certification.setController("zwave");
            certification.setSerial(zwaveRequest.getSerialNo());
            certification.setModel(gateway.getModel());
            certification.setMethod(ByteUtil.getHexString(zwaveRequest.getClassKey()));
            certification.setContext(ByteUtil.getHexString(zwaveRequest.getCommandKey()));
            List<Certification> certPayloadExistList = certificationRepository.findBySerialAndMethodAndContext(
                    certification.getSerial(), certification.getMethod(), certification.getContext());
            if (certPayloadExistList.size() > 0) {
                certificationRepository.delete(certPayloadExistList);
            }
            certificationRepository.save(certification);
        }
    }

    /**
     * mqtt publish 토픽 생성
     * @param topicLeadingPath
     * @return
     */
    public String getMqttPublishTopic(ZwaveRequest zwaveRequest) {
        return getMqttPublishTopic(zwaveRequest, Target.host.name());
    }

    /**
     * mqtt publish 토픽 생성
     * @param topicLeadingPath
     * @return
     */
    public String getMqttPublishTopic(ZwaveRequest zwaveRequest, String target) {
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

    @SuppressWarnings("rawtypes")
    public ResponseEntity getPayload(HashMap<String, Object> req, ZwaveRequest zwaveRequest) {
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
        return responseEntity;
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
        if (zwaveRequest instanceof ZwaveRequest && payload instanceof String) {
            ZwaveRequest reqZwaveRequest = (ZwaveRequest) zwaveRequest;
            subscribe(reqZwaveRequest, (String) payload);
        }
    }

    public void subscribe(ZwaveRequest zwaveRequest, String payload) throws JsonParseException, JsonMappingException, IOException, Exception {
        MqttPayload mqttPayload= new MqttPayload(); 
        if(!Common.empty(payload)){
            mqttPayload = objectMapper.readValue(payload, MqttPayload.class);
        }
        if (zwaveRequest.getClassKey() == ZwaveClassKey.BASIC) {
            if (zwaveRequest.getCommandKey() == ZwaveCommandKey.BASIC_REPORT) {
                updateCertification(zwaveRequest, payload);
            }
        }
        // 0X52
        if (zwaveRequest.getClassKey() == ZwaveClassKey.NETWORK_MANAGEMENT_PROXY) {
            Object resultData = mqttPayload.getResultData();
            String data = "";
            if (!isNull(resultData)) {
                data = objectMapper.writeValueAsString(resultData);
                // OX02
                /**
                 * 기기 리스트 수신시 새로 등록한 기기가 있을경우는 새로 등록 없을 경우는 업데이트함.
                 */
                if (zwaveRequest.getCommandKey() == ZwaveCommandKey.NODE_LIST_REPORT) {
                    try {
                        Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
                        if (!isNull(gateway)) {
                            List<Certification> certification = certificationRepository.findBySerialAndMethodAndContext(
                                    zwaveRequest.getSerialNo(), ByteUtil.getHexString((int) ZwaveClassKey.NETWORK_MANAGEMENT_PROXY),
                                    ByteUtil.getHexString(ZwaveCommandKey.NODE_LIST_REPORT));
                            for (int i = 0; i < certification.size(); i++) {
                                List<Zwave> lstZwave = zwaveRepository.findByGatewayNoAndCmd(gateway.getNo(),
                                        Integer.toString(ZwaveClassKey.NETWORK_MANAGEMENT_INCLUSION) + "/" + Integer.toString(ZwaveCommandKey.NODE_ADD_STATUS));
                                for (int j = 0; j < lstZwave.size(); j++) {
                                    Zwave zwave = lstZwave.get(j);
                                    String nodeListPayload = certification.get(i).getPayload();
                                    HashMap zwaveNodeListReport = objectMapper.readValue(nodeListPayload, HashMap.class);
                                    List<HashMap> nodeListItem = (List<HashMap>) zwaveNodeListReport.get("nodelist");
                                    for (int k = 0; k < nodeListItem.size(); k++) {
                                        int node = (int) nodeListItem.get(k).getOrDefault("nodeid", 99);
                                        if (node == Integer.parseInt(zwave.getNodeId())) {
                                            String nodeListPayload2 = nodeListPayload;
                                            if (Common.empty(zwave.getStatus())) {
                                                HashMap nodeItem = (HashMap) nodeListItem.get(k);
                                                try {
                                                    nodeItem.toString();
                                                    nodeListPayload2 = objectMapper.writeValueAsString(nodeItem);
                                                } catch (JsonProcessingException e1) {
                                                    // TODO Auto-generated catch block
                                                    e1.printStackTrace();
                                                }
                                                Map req = new HashMap();
                                                req.put("serial",gateway.getSerial());
                                                req.put("nodeId",node);
                                                req.put("endpointId", 0);
                                                req.put("option", nodeItem.getOrDefault("security", ""));
                                                ZwaveRequest appZwaveRequest = new ZwaveRequest((HashMap<String, Object>) req, ZwaveClassKey.NETWORK_MANAGEMENT_INCLUSION, ZwaveCommandKey.NODE_ADD,"v1");
                                                String topic = getMqttPublishTopic(appZwaveRequest,Target.app.name());

                                                try {
                                                    // publish(topic, nodeListMap);
                                                    publish(topic, nodeItem);
                                                } catch (JsonProcessingException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }

                                                zwave.setStatus("0x01");
                                                zwaveRepository.save(zwave);
                                            }
                                        }

                                    }

                                }
                            }

                        } else {
                            logging.info(String.format("Gateway Serial Number(%s) is not registered", zwaveRequest.getSerialNo()));;
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            updateCertification(zwaveRequest, data);
        }
        // 기기등록 모드
        if (zwaveRequest.getClassKey() == ZwaveClassKey.NETWORK_MANAGEMENT_INCLUSION) {
            // 기기상태값모드 받은 경우
            if (zwaveRequest.getCommandKey() == ZwaveCommandKey.NODE_ADD_STATUS) {
                // newNodeId 가 있을경우 등록 성공이고 없을경우 등록완료 전으로 상태 메세지를 확인한다.
                if (!Objects.isNull(mqttPayload.getResultData())) {
                    if (!isNull(mqttPayload.getResultData().get("newNodeId"))) {
                        // 0x52
                        zwaveRequest.setClassKey(zwaveRequest.getClassKey());
                        // 0x02
                        zwaveRequest.setCommandKey(zwaveRequest.getCommandKey());
                        zwaveRequest.setNodeId(0);
                        zwaveRequest.setEndpointId(0);
                        zwaveRequest.setVersion("v1");
                        zwaveRequest.setSecurityOption("none");
                        saveZwaveRegist(zwaveRequest, mqttPayload);
                    }
                    if ("0".equals(mqttPayload.getResultData().getOrDefault("status", "9").toString())) {
                        // 서버에 기기리스트를 요청함.
                        zwaveRequest.setClassKey(ZwaveClassKey.NETWORK_MANAGEMENT_PROXY);
                        zwaveRequest.setCommandKey(ZwaveCommandKey.NODE_ADD);
                        String topic = getMqttPublishTopic(zwaveRequest, "host");
                        publish(topic, zwaveRequest.getClassKey());
                    }
                }
                String status = "status null ";
                if (!Objects.isNull(mqttPayload.getResultData())) {
                    status = mqttPayload.getResultData().getOrDefault("status", 0).toString();
                }
                logging.info(String.format("<< SERIAL NO : %s, NODE_ADD_STATUS : %s >>", zwaveRequest.getSerialNo(), status));
                updateCertification(zwaveRequest, payload);

            } else {
                if (!isNull(mqttPayload.getResultData())) {
                    if (isNull(mqttPayload.getResultData().get("result_code"))) {
                        String topic = getMqttPublishTopic(zwaveRequest, "host");
                        if (!topic.contains(Target.host.name() + "/" + Target.server.name())) {
                            publish(topic, zwaveRequest.getClassKey());
                        }
                    }
                }

            }
        }
    }

    /**
     * 신규 등록 ZWAVE 기기 디비 저장
     * @param zwaveRequest
     * @param mqttPayload
     */
    private void saveZwaveRegist(ZwaveRequest zwaveRequest, MqttPayload mqttPayload) {
        Zwave zwave = new Zwave();
        Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
        zwave.setCmd(Integer.toString(ZwaveClassKey.NETWORK_MANAGEMENT_INCLUSION) + "/" + Integer.toString(ZwaveCommandKey.NODE_ADD_STATUS));
        zwave.setGatewayNo(gateway.getNo());
        if (!Objects.isNull(mqttPayload.getResultData())) {

            // TODO DB에는 String이므로 String또는 int로 통일 할것.
            if (!isNull(mqttPayload.getResultData().get("newNodeId"))) {
                Object obj = mqttPayload.getResultData().get("newNodeId");
                if (obj instanceof Integer) {
                    zwave.setNodeId(obj.toString());
                }
                if (obj instanceof String) {
                    zwave.setNodeId(obj.toString());
                }
                zwave.setNodeId(String.format("%2s", obj.toString()));
            }
        }
        zwave.setEndpointId("00");
        zwave.setEvent("new");
        zwave.setStatus("");
        zwave.setNickname("");
        zwave.setCreratedTime(new Date());
        zwave.setLastModifiedDate(new Date());
        zwaveRepository.save(zwave);
    }

    @Override
    public Object execute(Object req, Object zwaveRequest, Object isCert) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object publish(Object req, Object zwaveRequest) {
        // TODO Auto-generated method stub
        return null;
    }
    //제어
    @Override
    public void execute(Map map, boolean isCert) throws JsonProcessingException {
        String topic = getZwaveTopic(map);
        HashMap map1 = getPublishPayload((HashMap)map);
        publish(topic, map1);
    }
    /**
     * mqtt publish 토픽 생성
     * @param topicLeadingPath //0 none, 1 crc
     * @return
     */
    public String getZwaveTopic(Map map) {
        String topic = "";
        String nodeId = ByteUtil.getHexString((Integer) map.getOrDefault("nodeId",0));;
        String endPointId =  ByteUtil.getHexString((Integer) map.getOrDefault("endpointId",0));
        String serial = (String)map.getOrDefault("serial","01234567");
        String commandKey = (String)map.getOrDefault("cmdkey","0x00");
        String classkey = (String)map.getOrDefault("classkey","0x00");
        String version = (String)map.getOrDefault("version","v1");
        String option = Integer.toString((int) map.getOrDefault("option", 0));
        String model = (String) map.getOrDefault("model", 0);
        String[] segments = new String[] { "/server", Target.host.name(), model, serial, "zwave", "certi",
                classkey, commandKey, version,nodeId, endPointId, option};
        topic = String.join("/", segments);
        logging.info("====================== ZWAVE PROTO MQTT PUBLISH TOPIC ======================");
        logging.info(topic);
        return topic;
    }

    @Override
    public void execute(ZwaveRequest zwaveRequest, boolean isCert) {
        // TODO Auto-generated method stub
        
    }
}
