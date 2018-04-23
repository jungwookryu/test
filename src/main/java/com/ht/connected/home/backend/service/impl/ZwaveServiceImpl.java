package com.ht.connected.home.backend.service.impl;

import static java.util.Objects.isNull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.common.ByteUtil;
import com.ht.connected.home.backend.config.service.ZwaveClassKey;
import com.ht.connected.home.backend.config.service.ZwaveCommandKey;
import com.ht.connected.home.backend.controller.mqtt.ProducerRestController;
import com.ht.connected.home.backend.model.dto.MqttPayload;
import com.ht.connected.home.backend.model.dto.Target;
import com.ht.connected.home.backend.model.dto.ZwaveNodeListReport;
import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.model.entity.Certification;
import com.ht.connected.home.backend.model.entity.Gateway;
import com.ht.connected.home.backend.model.entity.Zwave;
import com.ht.connected.home.backend.model.rabbit.producer.Message;
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
    private RabbitTemplate rabbitTemplate;

    @Autowired
    ProducerRestController producerRestController;

    private ObjectMapper objectMapper = new ObjectMapper();
    @Value("${spring.activemq.queueName}")
    String activemqQueueName;

    @Override
    public ResponseEntity execute(HashMap<String, Object> req, ZwaveRequest zwaveRequest, boolean isCert) throws JsonProcessingException {
        logger.info("zwaveRequest.getClassKey()::::" + zwaveRequest.getClassKey());

        if (ZwaveClassKey.NETWORK_MANAGEMENT_PROXY == zwaveRequest.getClassKey() ||
                ZwaveClassKey.BASIC == zwaveRequest.getClassKey()) {
            if ((zwaveRequest.getCommandKey() == ZwaveCommandKey.NODE_LIST_REPORT)
                    || (zwaveRequest.getCommandKey() == ZwaveCommandKey.NODE_INFO_CACHED_REPORT)) {
                return getPayload(req, zwaveRequest);
            }
        }

        if (ZwaveClassKey.NETWORK_MANAGEMENT_INCLUSION == zwaveRequest.getClassKey()) {
            if ((zwaveRequest.getCommandKey() == ZwaveCommandKey.NODE_ADD_STATUS)
                    || (zwaveRequest.getCommandKey() == ZwaveCommandKey.NODE_REMOVE_STATUS)
                    || (zwaveRequest.getCommandKey() == ZwaveCommandKey.FAILED_NODE_REMOVE_STATUS)
                    || (zwaveRequest.getCommandKey() == ZwaveCommandKey.FAILED_NODE_REPLACE_STATUS)
                    || (zwaveRequest.getCommandKey() == ZwaveCommandKey.NODE_NEIGHBOR_UPDATE_STATUS)
                    || (zwaveRequest.getCommandKey() == ZwaveCommandKey.RETURN_ROUTE_ASSIGN_COMPLETE)
                    || (zwaveRequest.getCommandKey() == ZwaveCommandKey.RETURN_ROUTE_DELETE_COMPLETE)) {
                return getPayload(req, zwaveRequest);
            }
            if (zwaveRequest.getCommandKey() == ZwaveCommandKey.NODE_ADD ||
                    zwaveRequest.getCommandKey() == ZwaveCommandKey.NODE_STOP) {
                HashMap map = new HashMap<>();
                map.put("mode", zwaveRequest.getCommandKey());
                ObjectMapper mapper = new ObjectMapper();
                String str = mapper.writeValueAsString(map);
                req.put("set_data", map);
            }

        }
        if (ZwaveClassKey.BASIC == zwaveRequest.getClassKey()) {
            if (zwaveRequest.getCommandKey() == ZwaveCommandKey.BASIC_REPORT) {
                return getPayload(req, zwaveRequest);
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
        String payload = objectMapper.writeValueAsString(publishPayload);
        producerRestController.onSend(payload);
    }

    public void publish(String topic, int classKey) throws JsonProcessingException {
        // 불필요한 publish를 제거함.
        publish(topic, null);
    }

    public void publish(String topic) throws JsonProcessingException {
        publish("", null);
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
            certification.setMethod(Integer.toString(zwaveRequest.getClassKey()));
            certification.setContext(Integer.toString(zwaveRequest.getCommandKey()));
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
            String[] segments = new String[] { "/server", target, gateway.getModel(), gateway.getSerial(), "zwave", "certi",
                    ByteUtil.getHexString(zwaveRequest.getClassKey()), ByteUtil.getHexString(zwaveRequest.getCommandKey()), zwaveRequest.getVersion(),
                    ByteUtil.getHexString(zwaveRequest.getNodeId()), ByteUtil.getHexString(zwaveRequest.getEndpointId()),
                    zwaveRequest.getSecurityOption() };
            topic = String.join("/", segments);
            logger.info("====================== ZWAVE PROTO MQTT PUBLISH TOPIC ======================");
            logger.info(topic);
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
    public Zwave execute(Zwave req, Zwave zwaveRequest, Integer isCert) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Zwave subscribe(Zwave zwaveRequest, Integer payload) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Zwave publish(Zwave req, Zwave zwaveRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void subscribe(ZwaveRequest zwaveRequest, String payload) {
        if (zwaveRequest.getClassKey() == ZwaveClassKey.BASIC) {
            if (zwaveRequest.getCommandKey() == ZwaveCommandKey.BASIC_REPORT) {
                updateCertification(zwaveRequest, payload);
            }
        }

        if (zwaveRequest.getClassKey() == ZwaveClassKey.NETWORK_MANAGEMENT_PROXY) {
            try {
                MqttPayload mqttPayload = objectMapper.readValue(payload, MqttPayload.class);
                Object resultData = mqttPayload.getResultData();
                String data = "";
                if (!isNull(resultData)) {
                    data = objectMapper.writeValueAsString(resultData);
                    if (zwaveRequest.getCommandKey() == ZwaveCommandKey.NODE_LIST_REPORT) {
                        /**
                         * 기기 리스트 수신시 새로 등록한 기기가 있을경우
                         */
                        Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
                        if (!isNull(gateway)) {
                            Zwave zwave = zwaveRepository.findByGatewayNoAndCmd(gateway.getNo(),
                                    Integer.toString(ZwaveClassKey.NETWORK_MANAGEMENT_INCLUSION) + "+" + Integer.toString(ZwaveCommandKey.NODE_ADD_STATUS));
                            if (!isNull(zwave)) {
                                List<Certification> certification = certificationRepository.findBySerialAndMethodAndContext(
                                        zwaveRequest.getSerialNo(), Integer.toString(ZwaveClassKey.NETWORK_MANAGEMENT_PROXY),
                                        Integer.toString(ZwaveCommandKey.NODE_LIST_REPORT));
                                String nodeListPayload = certification.get(0).getPayload();
                                ZwaveNodeListReport zwaveNodeListReport = objectMapper.readValue(nodeListPayload,
                                        ZwaveNodeListReport.class);
                                ZwaveNodeListReport.NodeListItem nodeListItem = zwaveNodeListReport.getNodelist().stream()
                                        .filter(node -> node.getNodeid().equals(String.valueOf(zwave.getNodeId())))
                                        .collect(Collectors.toList()).get(0);
                                nodeListPayload = objectMapper.writeValueAsString(nodeListItem);
                                // MqttPayload mqttMessage = new MqttPayload();
                                HashMap<String, Object> nodeListMap = objectMapper.readValue(nodeListPayload, HashMap.class);
                                // mqttMessage.setResultData(nodeListMap);
                                String topic = String.format("/server/app/%s/%s/zwave/certi/%s/%s/v1", gateway.getModel(),
                                        gateway.getSerial(), ZwaveClassKey.NETWORK_MANAGEMENT_INCLUSION, ZwaveCommandKey.NODE_ADD);
                                publish(topic, nodeListMap);
                                zwaveRepository.delete(zwave);
                            }
                        } else {
                            logger.info(String.format("Gateway Serial Number(%s) is not registered", zwaveRequest.getSerialNo()));;
                        }
                    }
                }
                updateCertification(zwaveRequest, data);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (zwaveRequest.getClassKey() == ZwaveClassKey.NETWORK_MANAGEMENT_INCLUSION) {
            try {
                MqttPayload mqttPayload = objectMapper.readValue(payload, MqttPayload.class);
                if (zwaveRequest.getCommandKey() == ZwaveCommandKey.NODE_ADD_STATUS) {
                    /**
                     * newNodeId 가 있을경우 등록 성공이고 없을경우 등록완료 전으로 상태 메세지를 확인한다.
                     */
                    if (!isNull(mqttPayload.getResultData().get("newNodeId"))) {
                        zwaveRequest.setClassKey(0x52);
                        zwaveRequest.setCommandKey(0x02);
                        zwaveRequest.setNodeId(0);
                        zwaveRequest.setEndpointId(0);
                        zwaveRequest.setVersion("v1");
                        zwaveRequest.setSecurityOption("none");
                        addZwaveRegistEvent(zwaveRequest, mqttPayload);
                        String topic = getMqttPublishTopic(zwaveRequest, "host");
                        publish(topic, zwaveRequest.getClassKey());
                    } else {
                        String status = mqttPayload.getResultData().get("status").toString();
                        logger.info(String.format("<< SERIAL NO : %s, NODE_ADD_STATUS : %s >>", zwaveRequest.getSerialNo(),
                                status));
                    }
                }
                updateCertification(zwaveRequest, payload);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 신규 등록 ZWAVE 기기 디비 저장
     * @param zwaveRequest
     * @param mqttPayload
     */
    private void addZwaveRegistEvent(ZwaveRequest zwaveRequest, MqttPayload mqttPayload) {
        Zwave zwave = new Zwave();
        Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
        zwave.setCmd(Integer.toString(ZwaveClassKey.NETWORK_MANAGEMENT_INCLUSION) + "/" + Integer.toString(ZwaveCommandKey.NODE_ADD_STATUS));
        zwave.setGatewayNo(gateway.getNo());
        zwave.setNodeId(Integer.valueOf(mqttPayload.getResultData().get("newNodeId").toString()));
        zwave.setEndpointId(0);
        zwave.setEvent("new");
        zwave.setStatus("");
        zwave.setNickname("");
        zwave.setCreratedTime(new Date());
        zwave.setLastModifiedDate(new Date());
        zwaveRepository.save(zwave);
    }

}
