package com.ht.connected.home.backend.gateway;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.category.zwave.ZWaveRepository;
import com.ht.connected.home.backend.category.zwave.ZWaveService;
import com.ht.connected.home.backend.category.zwave.ZWaveServiceImpl;
import com.ht.connected.home.backend.category.zwave.certification.CertificationRepository;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.config.service.MqttConfig;
import com.ht.connected.home.backend.gatewayCategory.CategoryActive;
import com.ht.connected.home.backend.gatewayCategory.GatewayCategory;
import com.ht.connected.home.backend.gatewayCategory.GatewayCategoryRepository;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;
import com.ht.connected.home.backend.service.mqtt.Target;
import com.ht.connected.home.backend.user.User;
import com.ht.connected.home.backend.user.UserRepository;
import com.ht.connected.home.backend.userGateway.UserGateway;
import com.ht.connected.home.backend.userGateway.UserGatewayRepository;

@Service
public class GatewayServiceImpl extends CrudServiceImpl<Gateway, Integer> implements GatewayService {

    public GatewayServiceImpl(JpaRepository<Gateway, Integer> jpaRepository) {
        super(jpaRepository);
    }

    enum type {
        register, boot, manager
    }

    enum status {
        add, delete
    }

    Logger logger = LoggerFactory.getLogger(ZWaveServiceImpl.class);

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

    @Autowired
    ZWaveRepository zwaveRepository;

    @Autowired
    CertificationRepository certificationRepository;

    @Autowired
    GatewayCategoryRepository gatewayCategoryRepository;

    @Autowired
    ZWaveService zwaveService;

    
    @Autowired
    AmqpTemplate rabbitTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 호스트 등록/부팅 메세지 executor type 이 register 일 경우만 처리 type 이 boot 일 경우에 대한 디비 저정은 추가될수 있음
     * @param mqttTopicHandler
     * @param mqttGateway
     * @return
     * @throws Exception
     */

    public List getGatewayList(String status,String authUserEmail) {
        List<User> users = userRepository.findByUserEmail(authUserEmail);
        User user = users.get(0);
        List<Gateway> gateway = new ArrayList();
        if(Common.empty(status)) {
            gateway = gatewayRepository.findByUsers(user);
        }
        else {
            gateway = gatewayRepository.findByUsersAndStatusContaining(user,status);
        }
        return gateway;
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
        if (users.size() > 0) {
            return users.get(0);
        }
        return new User();
    }

    /**
     * Host reboot category = "type": "boot" Host regist category = "type": "register" topic alive host 가 마지막으로 부팅되어있는 메세지
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
                if(null != exangeGateway && (!exangeGateway.getCreatedUserId().equals(gateway.getCreatedUserId()))) {
                    exangeGateway.setStatus("failAp");
                    exangeGateway.setLastModifiedTime(new Date());
                    updateGateway(exangeGateway);
                }else {
                    List<User> users = userRepository.findByUserEmail(gateway.getUserEmail());
                    gateway.setLastModifiedTime(new Date());
                    if (users.size() > 0) {
                        User user = users.get(0);
                        if (exangeGateway == null) {
                            exangeGateway = gateway;
                            exangeGateway.setCreatedUserId(gateway.getUserEmail());
                        } else {
                            exangeGateway.setLastModifiedTime(new Date());
                        }
                        exangeGateway.setStatus("sucessAp");
                        exangeGateway.setCreatedTime(new Date());
                        exangeGateway.setBssid(gateway.getBssid());
                        exangeGateway.setSsid(gateway.getSsid());
                        exangeGateway.setNickname((String) Common.isNullrtnByobj(gateway.getNickname(), ""));
                        updateGateway(exangeGateway);
                        updateUserGateway(exangeGateway, user.getNo());
                        String exeTopic = String.format("/" + Target.server.name() + "/" + Target.app.name() + "/%s/%s/manager/product/registration", gateway.getModel(),
                                gateway.getSerial());
                        publish(exeTopic, null);
                    }
                }
                
            }

        }
        if (topic.contains("alive")) {
            // TODO Host 마지막 상태 시간 저장 하기
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
        if (t instanceof String && t2 == null) {
            publish((String) t);
        }

    }

    @Override
    @Transactional
    public void delete(int no) {
        // db 삭제모드 업데이트
        updateDeleteDB(no);
        // host 삭제 모드 요청 publish
        Gateway gateway = findOne(no);
        String exeTopic = String.format("/" + Target.server.name() + "/" + Target.app.name() + "/%s/%s/zwave/reset", gateway.getModel(), gateway.getSerial());
        publish(exeTopic, null);
    }

    private void updateDeleteDB(int gatewayNo) {
        gatewayRepository.setModifyStatusForNo(status.delete.name(), gatewayNo);
        userGatewayRepository.setModifyStatusForGatewayNo(status.delete.name(), gatewayNo);
    }

    private void deleteDB(int gatewayNo) {
        gatewayRepository.delete(gatewayNo);
        userGatewayRepository.deleteByGatewayNo(gatewayNo);
    }

    @Override
    public void deleteCategory(GatewayCategory gatewayCategory) {

        gatewayCategoryRepository.deleteByGatewayNoAndCategoryNo(gatewayCategory.getGatewayNo(), gatewayCategory.getCategoryNo());

        if (CategoryActive.gateway.zwave.name().equals(gatewayCategory.getCategory())) {
            int gatewayCategoryCnt = gatewayCategoryRepository.deleteByGatewayNoAndCategoryNo(gatewayCategory.getGatewayNo(), CategoryActive.gateway.zwave.ordinal());
            int zwaveCnt = zwaveRepository.deleteByGatewayNo(gatewayCategory.getGatewayNo());
        }

    }
    
    @Override
    public Gateway modifyGateway(Gateway originGateway, Gateway gateway) {
        Gateway saveGateway = originGateway;
        if(originGateway !=null) {
            if(Common.notEmpty(gateway.getNickname())) {
                saveGateway.setNickname(gateway.getNickname());
            }
            if(Common.notEmpty(gateway.getStatus())) {
                saveGateway.setStatus(gateway.getStatus());
            }
            if(Common.notEmpty(gateway.getCreatedUserId())) {
                saveGateway.setCreatedUserId(gateway.getCreatedUserId());
            }
            if(originGateway != saveGateway) {
                Gateway rtnGateway = gatewayRepository.save(originGateway);
                return rtnGateway;
            }else {
                return originGateway;
            }
        }else {
            return new Gateway(); 
        }
    }


    @Override
    public Gateway findOne(int no) {
        return gatewayRepository.findOne(no);
    }

}
