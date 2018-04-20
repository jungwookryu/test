package com.ht.connected.home.backend.service.impl.zwave;

import static java.util.Objects.isNull;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.config.service.MqttConfig.MqttGateway;
import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.model.entity.Certification;
import com.ht.connected.home.backend.model.entity.Gateway;
import com.ht.connected.home.backend.repository.CertificationRepository;
import com.ht.connected.home.backend.repository.GateWayRepository;

@Service
public class ZwaveDefault {

    protected static final Log logging = LogFactory.getLog(ZwaveDefault.class);

    protected boolean isCert = false;

    @Autowired
    protected GateWayRepository gatewayRepository;

    @Autowired
    BeanFactory beanFactory;

    @Autowired
    protected CertificationRepository certificationRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    /**
     * 인증앱 지원 Bypass Publish
     * @param req
     * @param zwaveRequest
     * @return
     */
    @SuppressWarnings("rawtypes")
    public ResponseEntity publish(HashMap<String, Object> req, ZwaveRequest zwaveRequest) {
        String topic = getMqttPublishTopic(zwaveRequest, "host");
        publish(topic, getPublishPayload(req));
        return new ResponseEntity(null);
    }

    /**
     * 서비스앱 지원 publish
     * @param topic
     * @param publishPayload
     */
    public void publish(String topic, Object publishPayload) {
        MqttPahoMessageHandler messageHandler = (MqttPahoMessageHandler) beanFactory.getBean("MqttOutbound");
        messageHandler.setDefaultTopic(topic);
        MqttGateway gateway = beanFactory.getBean(MqttGateway.class);
        try {
            String payload = objectMapper.writeValueAsString(publishPayload);
            gateway.sendToMqtt(payload);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
     * 
     * @param certPayload
     */
    public void updateCertification(ZwaveRequest zwaveRequest, String payload) {
        Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
        if(!isNull(gateway)) {
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
     * 
     * @param topicLeadingPath
     * @return
     */
    public String getMqttPublishTopic(ZwaveRequest zwaveRequest, String target) {
        String topic = "";
        Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
        if(!isNull(gateway)) {
            String[] segments = new String[] { "/server", target, gateway.getModel(), gateway.getSerial(), "zwave", "certi",
                    Integer.toString(zwaveRequest.getClassKey()), Integer.toString(zwaveRequest.getCommandKey()), zwaveRequest.getVersion(),
                    getHexString(zwaveRequest.getNodeId()), getHexString(zwaveRequest.getEndpointId()),
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

    /**
     * 호스트에 전달하는 토픽에 Node ID와 Endpoint ID는 헥사값
     * 
     * @param number
     * @return
     */
    private String getHexString(Integer number) {
        return "0x" + String.format("%2s", Integer.toHexString(number)).replace(' ', '0');
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
}
