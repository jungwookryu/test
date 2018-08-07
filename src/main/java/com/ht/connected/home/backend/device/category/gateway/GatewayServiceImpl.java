package com.ht.connected.home.backend.device.category.gateway;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import javax.transaction.Transactional;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.client.home.Home;
import com.ht.connected.home.backend.client.home.HomeService;
import com.ht.connected.home.backend.client.home.HomeServiceImpl;
import com.ht.connected.home.backend.client.home.sharehome.ShareHomeRepository;
import com.ht.connected.home.backend.client.user.User;
import com.ht.connected.home.backend.client.user.UserService;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.common.MqttCommon;
import com.ht.connected.home.backend.controller.mqtt.Message;
import com.ht.connected.home.backend.controller.mqtt.ProducerComponent;
import com.ht.connected.home.backend.device.category.CategoryActive;
import com.ht.connected.home.backend.device.category.gateway.gatewayCategory.GatewayCategory;
import com.ht.connected.home.backend.device.category.gateway.gatewayCategory.GatewayCategoryRepository;
import com.ht.connected.home.backend.device.category.ir.IRService;
import com.ht.connected.home.backend.device.category.zwave.ZWave;
import com.ht.connected.home.backend.device.category.zwave.ZWaveRepository;
import com.ht.connected.home.backend.device.category.zwave.ZWaveService;
import com.ht.connected.home.backend.device.category.zwave.cmdcls.CmdClsRepository;
import com.ht.connected.home.backend.device.category.zwave.endpoint.Endpoint;
import com.ht.connected.home.backend.device.category.zwave.endpoint.EndpointRepository;
import com.ht.connected.home.backend.service.mqtt.Target;
import com.ht.connected.home.backend.userGateway.UserGateway;
import com.ht.connected.home.backend.userGateway.UserGatewayRepository;

@Service
public class GatewayServiceImpl implements GatewayService {

    enum Type {
        register, wifi_reset, alive
    }

    enum status {
        add, delete
    }

    Logger logger = LoggerFactory.getLogger(GatewayServiceImpl.class);

    UserGatewayRepository userGatewayRepository;
    
    @Autowired
    ProducerComponent producerRestController;

    @Autowired
    UserService userService;

    @Autowired
    GatewayRepository gatewayRepository;

    @Autowired
    HomeService homeService;
    
    @Autowired
    @Qualifier(value = "callbackAckProperties")
    Properties callbackAckProperties;
    
    @Autowired
    ZWaveRepository zwaveRepository;
    
    @Autowired
    EndpointRepository endpointRepository;
    
    @Autowired
    CmdClsRepository cmdClsRepository;
    
    @Autowired
    IRService irService;

    @Autowired
    GatewayCategoryRepository gatewayCategoryRepository;

    @Autowired
    ZWaveService zwaveService;
    
    @Autowired
    ShareHomeRepository shareHomeRepository;
    
    
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
	public List<Gateway> getGatewayList(String status,String authUserEmail) {
        User user = userService.getUser(authUserEmail);
        List<Integer> nos = new ArrayList<>();
        if(Common.empty(status)) {
            List<UserGateway> userGateways = userGatewayRepository.findByUserNo(user.getNo());
            userGateways.forEach(UserGateway -> nos.add(UserGateway.getGatewayNo()));
            return gatewayRepository.findByNoIn(nos);
        }
        else {
            List<UserGateway> userGateways = userGatewayRepository.findByUserNo(user.getNo());
            userGateways.forEach(UserGateway -> nos.add(UserGateway.getGatewayNo()));
            return gatewayRepository.findByNoInAndStatusContaining(nos,status);
        }
    }
    
    @Override
	public List<Gateway> getGatewayListByHome(List<Integer> homeNos) {
		List<Gateway> gateways = gatewayRepository.findByHomeNoIn(homeNos);
		return gateways;
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
     * Host reboot category = "type": "boot" Host regist category = "type": "register" topic alive host 가 마지막으로 부팅되어있는 메세지
     * @param topic
     * @param payload
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws InterruptedException 
     */
    @Override
	public void subscribe(String topic, String payload) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
        String[] topicSplited = topic.trim().replace(".", ";").split(";");
        Gateway responseGateway = new Gateway();
        if(Common.notEmpty(payload)) {
        	responseGateway = objectMapper.readValue(payload, Gateway.class);
        }
        responseGateway.setTargetType(topicSplited[MqttCommon.INT_SOURCE]);
        responseGateway.setModel(topicSplited[MqttCommon.INT_MODEL]);
        responseGateway.setSerial(topicSplited[MqttCommon.INT_SERIAL]);
        responseGateway.setCreatedUserId(responseGateway.getUserEmail());
        if (Type.register.name().equals(topicSplited[6])) {
            registerGateway(responseGateway);
        }
        else if (Type.wifi_reset.name().equals(topicSplited[6])) {
            registerGateway(responseGateway);
        }else if (Type.alive.name().equals(topicSplited[6])) {
        	
        }

    }

    /**
     * 서비스앱 지원 메세지 없는 publish
     * @param topic
     * @throws InterruptedException 
     */
    public void publish(String topic) throws InterruptedException {
        Message message =  new Message(topic, "");
        MqttCommon.publish(producerRestController, message);
    }

    public void publish(Object t, Object t2) throws InterruptedException {
        if (t instanceof String && t2 == null) {
            Message message =  new Message((String) t, "");
            MqttCommon.publish(producerRestController, message);
        }

    }

    @Override
    @Transactional
    public void delete(int no) throws InterruptedException {
        // db 삭제모드 업데이트
        updateDeleteDB(no);
        // host 삭제 모드 요청 publish
        Gateway gateway = findOne(no);
        if(Objects.isNull(gateway)) {
            throw new BadCredentialsException("not found gateway");
        }
        //데이터 삭제
        hostReset(gateway.getSerial());
        String exeTopic = String.format("/" + Target.server.name() + "/" + gateway.getTargetType() + "/%s/%s/manager/reset", gateway.getModel(), gateway.getSerial());
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
            if(Common.notEmpty(gateway.getCreated_user_id())) {
                saveGateway.setCreatedUserId(gateway.getCreated_user_id());
            }
            Gateway rtnGateway = gatewayRepository.save(originGateway);
            return rtnGateway;
        }else {
            return new Gateway(); 
        }
    }


    @Override
    public Gateway findOne(int no) {
        return gatewayRepository.findOne(no);
    }

    @Override
    public void hostReset(String serial) {
        // host 정보삭제
        Gateway gateway = gatewayRepository.findBySerial(serial);
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
    
    private void registerGateway(Gateway gateway) throws JsonGenerationException, JsonMappingException, InterruptedException, IOException {
       
        Gateway exangeGateway = gatewayRepository.findBySerial(gateway.getSerial());
        if(null != exangeGateway && (!exangeGateway.getCreated_user_id().equals(gateway.getCreated_user_id()))) {
            exangeGateway.setLastModifiedTime(new Date());
            exangeGateway.setStatus("failAp");
            updateGateway(exangeGateway);
        }else {
            User user = userService.getUser(gateway.getCreated_user_id());
            if (!Objects.isNull(user)) {
				if (exangeGateway == null) {
                    exangeGateway = gateway;
                    exangeGateway.setCreatedUserId(gateway.getCreated_user_id());
                } else {
                    exangeGateway.setLastModifiedTime(new Date());
                }
                exangeGateway.setStatus("sucessAp");
                exangeGateway.setCreatedTime(new Date());
                exangeGateway.setBssid(gateway.getBssid());
                exangeGateway.setSsid(gateway.getSsid());
                exangeGateway.setLocLatitude(gateway.getLocLatitude());
                exangeGateway.setLocLongitude(gateway.getLocLongitude());
                exangeGateway.setNickname(Common.isNullrtnByobj(gateway.getNickname(), 
                        new StringBuffer().append(gateway.getTargetType()).append("_").append(gateway.getSerial())).toString());
                exangeGateway.setHomeNo(gateway.getHomeNo());
                if(Objects.isNull(gateway.getHomeNo())){
                	List<Home> homes = homeService.getHomeListByEmail(user.getUserEmail(), "", HomeServiceImpl.ShareRole.master.name());
                	exangeGateway.setHomeNo(homes.get(0).getNo());
                }
                updateGateway(exangeGateway);
                Gateway rtnGateway = gatewayRepository.findBySerial(gateway.getSerial());
                MqttCommon.publishNotificationData(producerRestController,callbackAckProperties,"zwave.product.registration", Target.app.name(), gateway.getModel(), gateway.getSerial(), rtnGateway);
            }
        }
            
    }


    @Override
    public Gateway findBySerial(String serial) {
        Gateway gateway = gatewayRepository.findBySerial(serial);
        return gateway;
    }

    
}

