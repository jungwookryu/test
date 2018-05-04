package com.ht.connected.home.backend.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.javascript.host.Map;
import com.ht.connected.home.backend.config.service.MqttConfig;
import com.ht.connected.home.backend.config.service.MqttConfig.MqttGateway;
import com.ht.connected.home.backend.controller.rest.AppController;
import com.ht.connected.home.backend.model.dto.Category;
import com.ht.connected.home.backend.model.dto.Target;
import com.ht.connected.home.backend.model.entity.IR;
import com.ht.connected.home.backend.model.entity.Zwave;
import com.ht.connected.home.backend.repository.IRRepository;
import com.ht.connected.home.backend.service.IRService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

@Service
public class IRServiceImpl extends CrudServiceImpl<IR, Integer> implements IRService {
    public IRServiceImpl(JpaRepository<IR, Integer> jpaRepository) {
        super(jpaRepository);
        // TODO Auto-generated constructor stub
    }

    Logger logger = LoggerFactory.getLogger(IRServiceImpl.class);

    public enum Type {
        add, control
    }

    @Autowired
    IRRepository irRepository;
    
    @Autowired
    MqttConfig.MqttGateway mqttGateway;
    
    
    @Autowired
    @Qualifier(value="MqttOutbound")
    MqttPahoMessageHandler  messageHandler;
    @Override
    public List<IR> getIRByUser(String userEmail) {
        return irRepository.findByUserEmailAndStatus(userEmail, Type.add.name());
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public void delete(int no) {
        // 삭제모드 없음.
        irRepository.delete(no);
    }

    @Override
    public void studyIR(IR ir) throws JsonProcessingException {

        if (AppController.Command.start.name().equals(ir.getStatus()) || AppController.Command.stop.name().equals(ir.getStatus())) {
            // publish
            String topic = getMqttPublishTopic(ir, Target.host.name(), Target.server.name());
            HashMap<String, Object> publishPayload = new HashMap<String, Object>();
            publishPayload.put("type", Type.add.name());
            publishPayload.put("request", Category.ir.name());
            publishPayload.put("action", ir.getStatus());
            publish(topic, publishPayload);
            if (AppController.Command.stop.equals(ir.getStatus())) {
                // 등록모드 기본기기정보삭제
                // List<IR> irs = irRepository.findBySerialAndStatusAndModelOrUserEmail(ir.getSerial(), Type.add.name(), ir.getModel(), ir.getUserEmail());
                // irRepository.delete(irs);
            }
        }

        if (ir.getStatus().isEmpty()) {
            irRepository.save(ir);
        }

    }

    @Override
    public void subscribe(String[] topicSplited, String payload) throws JsonParseException, JsonMappingException, IOException, JSONException {
        if (topicSplited.length > 4) {
            String model = topicSplited[3];
            String serial = topicSplited[4];
            HashMap<String, Object> map = objectMapper.readValue(payload, HashMap.class);
            if (Type.add.name().equals((String) map.getOrDefault("type", ""))) {
                HashMap rtnMap = (HashMap) map.getOrDefault("response", new HashMap());
                List<IR> irs = irRepository.findBySerialAndStatusAndModel(serial, "", model);
                if(irs.size()>0) {
                    IR ir = irs.get(0);
                    List lst = (List) rtnMap.getOrDefault("value", new ArrayList<>());
                    int gap = (int) rtnMap.getOrDefault("gap", 0);
                    String format = (String) rtnMap.getOrDefault("format", "");
                    int rptcnt = (int) rtnMap.getOrDefault("rptcnt", 0);
                    for (int i = 0; i < 1; i++) {// 0번째만 저장해보자.
                        HashMap rtnMap2 = (HashMap) lst.get(i);
                        int length = (int) rtnMap2.getOrDefault("length", 0);
                        String data = (String) rtnMap2.getOrDefault("data", "");
                        ir.setNo(ir.getNo());
                        ir.setStatus("active");
                        ir.setFormat(format);
                        ir.setLength(length);
                        ir.setData(data);
                        ir.setGap(gap);
                        ir.setRptcnt(rptcnt);;
                        irRepository.save(ir);
                    }
                }
            }
        }

    }

    @Override
    public void controlIR(IR reqIr) throws JsonProcessingException {
        String topic = getMqttPublishTopic(reqIr, Target.host.name(), Target.server.name());
        HashMap<String, Object> publishPayload = new HashMap<String, Object>();
        List value = new ArrayList<>();
        // IR ir = irRepository.findOne(reqIr.getNo());
        List<IR> irs = irRepository.findByUserEmailAndSubNumberAndAction(reqIr.getUserEmail(), reqIr.getSubNumber(), reqIr.getAction());
        if (irs.size() > 0) {
            IR ir = irs.get(0);
            HashMap map = new HashMap();
            map.put("length", ir.getLength());
            map.put("data", ir.getData());
            value.add(map);
            HashMap requestMap = new HashMap<>();
            requestMap.put("format", ir.getFormat());
            requestMap.put("rptcnt", ir.getRptcnt());
            requestMap.put("gap", ir.getGap());
            requestMap.put("value", value);
            publishPayload.put("type", Type.control.name());
            publishPayload.put("request", requestMap);
            publish(topic, publishPayload);
        }
    }

    /**
     * mqtt publish 토픽 생성
     * @param topicLeadingPath
     * @return
     */
    public String getMqttPublishTopic(IR ir, String target, String source) {
        String topic = "";
        if (!Objects.isNull(ir.getStatus())) {
            String[] segments = new String[] { "/" + source, target, ir.getModel(), ir.getSerial(), Category.ir.name() };
            topic = String.join("/", segments);
            logger.info("====================== IR PROTO MQTT PUBLISH TOPIC ======================");
            logger.info(topic);
        }
        return topic;
    }

    public void publish(String topic, HashMap<String, Object> publishPayload) throws JsonProcessingException {

        messageHandler.setDefaultTopic(topic);
        String payload = objectMapper.writeValueAsString(publishPayload);
        logger.info("publish topic:::::::::::" + topic);
        mqttGateway.sendToMqtt(payload);
    }

    @Override
    public void subscribe(Object request, Object payload) throws JsonParseException, JsonMappingException, IOException, Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void publish(Object req, Object zwaveRequest) {
        // TODO Auto-generated method stub

    }

}
