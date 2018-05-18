package com.ht.connected.home.backend.service.impl;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.common.MqttCommon;
import com.ht.connected.home.backend.config.service.MqttConfig;
import com.ht.connected.home.backend.constants.zwave.commandclass.NetworkManagementProxyCommandClass;
import com.ht.connected.home.backend.model.dto.MqttRequest;
import com.ht.connected.home.backend.model.dto.Target;
import com.ht.connected.home.backend.model.entity.Gateway;
import com.ht.connected.home.backend.model.entity.User;
import com.ht.connected.home.backend.model.entity.UserGateway;
import com.ht.connected.home.backend.repository.CertificationRepository;
import com.ht.connected.home.backend.repository.GatewayRepository;
import com.ht.connected.home.backend.repository.UserGatewayRepository;
import com.ht.connected.home.backend.repository.UserRepository;
import com.ht.connected.home.backend.repository.ZwaveRepository;
import com.ht.connected.home.backend.service.GateWayService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

@Service
public class GateWayServiceImpl extends CrudServiceImpl<Gateway, Integer> implements GateWayService {

    public GateWayServiceImpl(JpaRepository<Gateway, Integer> jpaRepository) {
        super(jpaRepository);
    }

    enum type {
        register, boot, manager
    }
    
    enum status {
        add, delete
    }

    Logger logger = LoggerFactory.getLogger(ZwaveServiceImpl.class);

    @Autowired
    UserGatewayRepository userGatewayRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GatewayRepository gatewayRepository;
    @Autowired
    MqttConfig.MqttGateway mqttGateway;

    @Autowired
    @Qualifier(value = "MqttOutbound")
    MqttPahoMessageHandler messageHandler;

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    ZwaveRepository zwaveRepository;
    @Autowired
    CertificationRepository certificationRepository;

    @Autowired
    AmqpTemplate rabbitTemplate;
    /**
     * 호스트 등록/부팅 메세지 executor type 이 register 일 경우만 처리 type 이 boot 일 경우에 대한 디비 저정은 추가될수 있음
     * @param mqttTopicHandler
     * @param mqttGateway
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
        List<Gateway> gateways = gatewayRepository.findByNoIn(gatewayNos);

        gateways.forEach(gateway -> {
            gateway.setSerial(gateway.getSerial());
            gateway.setModel(gateway.getModel());
            gateway.setNickname((String)Common.isNullrtnByobj(gateway.getNickname(),""));
            User master = getMasterUserNicknameByGatewayNo(gateway.getCreatedUserId());
            gateway.setUserNickname((String)Common.isNullrtnByobj(master.getNickName(),""));
            gateway.setUserEmail((String)Common.isNullrtnByobj(master.getUserEmail(),""));
            lstGateways.add(gateway);
        });
        return lstGateways;
    }

    /**
     * 호스트 정보 업데이트
     * @param messageArrived
     * @param gateway
     * @return
     */
    private Gateway updateGateway(Gateway gateway) {
        if (!isNull(gateway)) {
            gatewayRepository.save(gateway);
        }
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

    private User getMasterUserNicknameByGatewayNo(String createdUserId) {
        List<User> users = userRepository.findByUserEmail(createdUserId);
        if(users.size()>0) {
            return users.get(0);
        }
        return new User();
    }

    /**
     * Host reboot category =  "type": "boot"
     * Host regist category =  "type": "register"
     * topic alive host 가 마지막으로 부팅되어있는 메세지
     * @param topic
     * @param payload
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public void subscribe(String topic, String payload) throws JsonParseException, JsonMappingException, IOException {
        String[] topicSplited = topic.split("/");
        if (topic.contains("noti")) {
            Gateway gateway = objectMapper.readValue(payload, Gateway.class);
            gateway.setSerial(topicSplited[4]);
            gateway.setModel(topicSplited[3]);
            gateway.setStatus(gateway.getType());
            if (type.register.name().equals(gateway.getType())) {
                Gateway exangeGateway = gatewayRepository.findBySerial(gateway.getSerial());
                List<User> users = userRepository.findByUserEmail(gateway.getUserEmail());
                gateway.setLastModifiedTime(new Date());
                if (users.size() > 0) {
                    User user = users.get(0);
                    if (exangeGateway == null) {
                        exangeGateway = gateway;
                        exangeGateway.setCreatedUserId(gateway.getUserEmail());
                        exangeGateway.setCreatedTime(new Date());
                    }else {
                        exangeGateway.setLastModifiedTime(new Date());
                    }
                    exangeGateway.setBssid(gateway.getBssid());
                    exangeGateway.setNickname((String)Common.isNullrtnByobj(gateway.getNickname(), ""));
                    updateGateway(exangeGateway);
                    updateUserGateway(exangeGateway, user.getNo());
                    String exeTopic = String.format("/" + Target.server.name() + "/" + Target.app.name() + "/%s/%s/manager/noti", gateway.getModel(),
                            gateway.getSerial());
                    publish(exeTopic, null);
                }
            }

        }
        if (topic.contains("alive")) {
            //TODO Host 마지막 상태 시간 저장 하기
        }

    }

    /**
     * 서비스앱 지원 메세지 없는 publish
     * @param topic
     */
    public void publish(String topic) {
        messageHandler.setDefaultTopic(topic);
        mqttGateway.sendToMqtt("");
    }

    @Override
    public void subscribe(Object t, Object p) throws com.fasterxml.jackson.core.JsonParseException, com.fasterxml.jackson.databind.JsonMappingException, IOException, Exception {
        if (t instanceof String && p instanceof String) {
            subscribe((String) t, (String) p);
        }

    }

    @Override
    public void publish(Object t, Object t2) {
        if(t instanceof String && t2==null) {
            publish((String)t);
        }

    }

    @Override
    @Transactional
    public void delete(int no) {
        //db 삭제
        updateDeleteDB(no);
        //host 삭제 모드 요청 publish
        
    }
    
    private void updateDeleteDB(int gatewayNo) {
        gatewayRepository.setModifyStatusForNo(status.delete.name(), gatewayNo);
        userGatewayRepository.setModifyStatusForGatewayNo(status.delete.name(), gatewayNo);
    }
    
    private void deleteDB(int gatewayNo) {
        gatewayRepository.delete(gatewayNo);
        userGatewayRepository.deleteByGatewayNo(gatewayNo);
    }

}
