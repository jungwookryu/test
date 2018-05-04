package com.ht.connected.home.backend.service.impl;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.common.ByteUtil;
import com.ht.connected.home.backend.config.service.MqttConfig.MqttGateway;
import com.ht.connected.home.backend.constants.zwave.commandclass.NetworkManagementInclusionCommandClass;
import com.ht.connected.home.backend.constants.zwave.commandclass.NetworkManagementProxyCommandClass;
import com.ht.connected.home.backend.model.dto.MqttMessageArrived;
import com.ht.connected.home.backend.model.dto.MqttPayload;
import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.model.entity.Certification;
import com.ht.connected.home.backend.model.entity.Gateway;
import com.ht.connected.home.backend.model.entity.UserGateway;
import com.ht.connected.home.backend.model.entity.User;
import com.ht.connected.home.backend.model.entity.Zwave;
import com.ht.connected.home.backend.repository.CertificationRepository;
import com.ht.connected.home.backend.repository.GateWayRepository;
import com.ht.connected.home.backend.repository.UserGatewayRepository;
import com.ht.connected.home.backend.repository.UsersRepository;
import com.ht.connected.home.backend.repository.ZwaveRepository;
import com.ht.connected.home.backend.service.GateWayService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

@Service
public class GateWayServiceImpl extends CrudServiceImpl<Gateway, Integer> implements GateWayService {

    public GateWayServiceImpl(JpaRepository<Gateway, Integer> jpaRepository) {
        super(jpaRepository);
    }
    enum type{
        register
        ,boot
        ,manager
    }
    Logger logger = LoggerFactory.getLogger(ZwaveServiceImpl.class);

    @Autowired
    BeanFactory beanFactory;

    @Autowired
    UserGatewayRepository userGatewayRepository;

    @Autowired
    UsersRepository userRepository;

    @Autowired
    GateWayRepository gatewayRepository;

    // @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    ZwaveRepository zwaveRepository;
    @Autowired
    CertificationRepository certificationRepository;

    /**
     * 호스트 등록/부팅 메세지 executor type 이 register 일 경우만 처리 type 이 boot 일 경우에 대한 디비 저정은 추가될수 있음
     * @param mqttTopicHandler
     * @param gateway
     * @return
     * @throws Exception
     */

    public List getGatewayList(String authUserEmail) {
        List<Gateway> lstGateways = new ArrayList<>();
        List<User> users = userRepository.findByUserEmail(authUserEmail);
        User user = users.get(0);
        List<UserGateway> userGateways = userGatewayRepository.findByUserNo(user.getNo());

        List<Integer> gatewayNos = new ArrayList<Integer>();
        userGateways.stream().forEach(userGateway -> gatewayNos.add(userGateway.getGatewayNo()));
        List<Gateway> gateways = gatewayRepository.findAll(gatewayNos);
        List<UserGateway> userGatewayList = userGatewayRepository.findByGatewayNoIn(gatewayNos);

        gateways.forEach(gateway -> {
            Gateway aGateway = new Gateway();
            aGateway.setSerial(gateway.getSerial());
            aGateway.setModel(gateway.getModel());
            aGateway.setNickname(gateway.getNickname());
            User master = getMasterUserNicknameByGatewayNo(userGatewayList, gateway.getNo());
            aGateway.setUserNickname(master.getNickName());
            aGateway.setUserEmail(master.getUserEmail());
            lstGateways.add(aGateway);
        });
        return lstGateways;
    }

    /**
     * 호스트 등록/부팅 메세지 executor type 이 register 일 경우만 처리 type 이 boot 일 경우에 대한 디비 저정은 추가될수 있음
     * @param mqttTopicHandler
     * @param gateway
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object execute(MqttMessageArrived mqttMessageArrived) throws Exception {
        if (mqttMessageArrived.getStrPayload().length() > 0) {
            Gateway gateway = gatewayRepository.findBySerial(mqttMessageArrived.getSerial());
            HashMap<String, String> map = objectMapper.readValue(mqttMessageArrived.getStrPayload(), HashMap.class);
            if (map.getOrDefault("type","").equals(type.register.name()) && isNull(gateway)) {
                List <User> users = userRepository.findByUserEmail(map.get("user_email"));
                if (users.size() > 0) {
                    User user = users.get(0);
                    gateway = updateGateway(mqttMessageArrived, gateway, map);
                    updateUserGateway(gateway, user.getNo());
                    String topic = String.format("/server/app/%s/%s/manager/noti", gateway.getModel(),
                            gateway.getSerial());
                    MqttPahoMessageHandler messageHandler = (MqttPahoMessageHandler) beanFactory.getBean("MqttOutbound");
                    messageHandler.setDefaultTopic(topic);
                    MqttGateway mqttGateway = beanFactory.getBean(MqttGateway.class);
                    mqttGateway.sendToMqtt("");
                } 
            }
        }
        return null;
    }

    /**
     * 호스트 정보 업데이트
     * @param messageArrived
     * @param gateway
     * @return
     */
    private Gateway updateGateway(MqttMessageArrived mqttMessageArrived, Gateway gateway, HashMap<String, String> map) {
        if (isNull(gateway)) {
            gateway = new Gateway();
        }
        gateway.setSerial(mqttMessageArrived.getSerial());
        gateway.setModel(mqttMessageArrived.getModel());
        gateway.setBssid(map.get("macaddress"));
        gatewayRepository.save(gateway);
        return gateway;
    }

    /**
     * 호스트를 초기 등록할경우 마스터 유저와 호스트 맵핑
     * @param jsonObject
     * @param gateway
     * @param userNo
     */
    private void updateUserGateway(Gateway gateway, int userNo) {
        UserGateway userGateway = userGatewayRepository.findByUserNoAndGatewayNo(userNo, gateway.getNo());
        if (isNull(userGateway)) {
            userGateway = new UserGateway();
        }
        userGateway.setGatewayNo(gateway.getNo());
        userGateway.setUserNo(userNo);
        userGateway.setGroupRole("master");
        userGatewayRepository.save(userGateway);
    }

    private User getMasterUserNicknameByGatewayNo(List<UserGateway> userGatewayList, Integer gatewayNo) {
        UserGateway userGateway = userGatewayList.stream()
                .filter(ug -> ug.getGroupRole().equals("master") && gatewayNo.equals(ug.getGatewayNo()))
                .collect(Collectors.toList()).get(0);
        User user = userRepository.findOne(userGateway.getUserNo());
        return user;
    }

    @Override
    public void subscribe(ZwaveRequest zwaveRequest, String payload) throws JsonParseException, JsonMappingException, IOException {

        MqttPayload mqttPayload = objectMapper.readValue(payload, MqttPayload.class);
        if (zwaveRequest.getClassKey()==NetworkManagementInclusionCommandClass.INT_ID 
                && zwaveRequest.getCommandKey() == NetworkManagementInclusionCommandClass.INT_NODE_ADD_STATUS) {
            /**
             * newNodeId 가 있을경우 등록 성공이고 없을경우 등록완료 전으로 상태 메세지를 확인한다.
             */
            if (!isNull(mqttPayload.getResultData().get("newNodeId"))) {
                zwaveRequest.setClassKey(NetworkManagementProxyCommandClass.INT_ID);
                zwaveRequest.setCommandKey(NetworkManagementProxyCommandClass.INT_NODE_LIST_REPORT);
                zwaveRequest.setNodeId(0);
                zwaveRequest.setEndpointId(0);
                zwaveRequest.setVersion("v1");
                zwaveRequest.setSecurityOption("none");
                addZwaveRegistEvent(zwaveRequest, mqttPayload);
                String topic = getMqttPublishTopic(zwaveRequest, "host");
                publish(topic);
            } else {
                String status = mqttPayload.getResultData().get("status").toString();
                logger.info(String.format("<< SERIAL NO : %s, NODE_ADD_STATUS : %s >>", zwaveRequest.getSerialNo(),
                        status));
            }
        }
        updateCertification(zwaveRequest, payload);

    }

    /**
     * 신규 등록 ZWAVE 기기 디비 저장
     * @param zwaveRequest
     * @param mqttPayload
     */
    private void addZwaveRegistEvent(ZwaveRequest zwaveRequest, MqttPayload mqttPayload) {
        Zwave zwave = new Zwave();
        Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
        zwave.setCmd(Integer.toString(NetworkManagementInclusionCommandClass.INT_ID) + "/" + Integer.toString(NetworkManagementInclusionCommandClass.INT_NODE_ADD_STATUS));
        zwave.setGatewayNo(gateway.getNo());
        zwave.setNodeId((String)mqttPayload.getResultData().get("newNodeId").toString());
        zwave.setEndpointId("00");
        zwave.setEvent("new");
        zwave.setStatus("");
        zwave.setNickname("");
        zwave.setCreratedTime(new Date());
        zwave.setLastModifiedDate(new Date());
        zwaveRepository.save(zwave);
    }

    /**
     * 서비스앱 지원 메세지 없는 publish
     * @param topic
     */
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

    @Override
    public void subscribe(Object zwaveRequest, Object payload) throws com.fasterxml.jackson.core.JsonParseException, com.fasterxml.jackson.databind.JsonMappingException, IOException, Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void publish(Object req, Object zwaveRequest) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ResponseEntity publish(HashMap<String, Object> req, ZwaveRequest zwaveRequest) {
        // TODO Auto-generated method stub
        return null;
    }

}
