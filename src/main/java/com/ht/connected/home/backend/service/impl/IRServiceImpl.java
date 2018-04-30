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
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.javascript.host.Map;
import com.ht.connected.home.backend.config.service.MqttConfig.MqttGateway;
import com.ht.connected.home.backend.controller.rest.AppController;
import com.ht.connected.home.backend.model.dto.Category;
import com.ht.connected.home.backend.model.dto.Target;
import com.ht.connected.home.backend.model.entity.IR;
import com.ht.connected.home.backend.repository.IRRepository;
import com.ht.connected.home.backend.service.IRService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

@Service
public class IRServiceImpl extends CrudServiceImpl<IR, Integer> implements IRService {
    private IRRepository irRepository;
    Logger logger = LoggerFactory.getLogger(IRServiceImpl.class);

    public enum Type {
        add, control
    }

    @Autowired
    BeanFactory beanFactory;

    @Autowired
    public IRServiceImpl(IRRepository irRepository) {
        super(irRepository);
        this.irRepository = irRepository;
    }

    @Override
    public List<IR> getIRByUser(String userEmail) {
        return irRepository.findByUserEmail(userEmail);
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

        if (AppController.Command.start.name().equals(ir.getStatus()) || AppController.Command.start.name().equals(ir.getStatus())) {
            // publish
            String topic = getMqttPublishTopic(ir, Target.host.name(), Target.server.name());
            HashMap<String, Object> publishPayload = new HashMap<String, Object>();
            publishPayload.put("type", Type.add.name());
            publishPayload.put("request", Category.ir.name());
            publishPayload.put("action", ir.getStatus());
            publish(topic, publishPayload);
            if(AppController.Command.stop.equals(ir.getStatus())) {
                //등록모드 기본기기정보삭제
                List<IR> irs = irRepository.findBySerialAndStatusAndModelOrUserEmail(ir.getSerial(), Type.add.name(), ir.getModel(), ir.getUserEmail());
                irRepository.delete(irs);
            }
        }
        
        if(ir.getStatus().isEmpty()) {
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
                List<IR> irs = irRepository.findBySerialAndStatusAndModelOrUserEmail(serial, Type.add.name(), model,"");
                IR ir = irs.get(0);
                IR saveIr = new IR(ir.getDevType(), ir.getAction(), ir.getIrType());
                List lst = (List)rtnMap.getOrDefault("value", new ArrayList<>());
                int gap = (int) rtnMap.getOrDefault("gap",0);
                int rptcnt = (int) rtnMap.getOrDefault("rptcnt",0);
                for (int i = 0; i < lst.size(); i++) {
                    JSONObject json = (JSONObject) lst.get(i);
                    int length = json.getInt("length");
                    String data = json.getString("data");
                    saveIr.setStatus("active");
                    saveIr.setLength(length);
                    saveIr.setData(data);
                    saveIr.setGap(gap);
                    saveIr.setRptcnt(rptcnt);;
                    irRepository.saveAndFlush(ir);
                }
            }
        }

    }
    
    
    @Override
    public void controlIR(IR ir) throws JsonProcessingException {
        String topic = getMqttPublishTopic(ir, Target.host.name(), Target.server.name());
        HashMap<String, Object> publishPayload = new HashMap<String, Object>();
        List value = new ArrayList<>();
        List<IR> list = irRepository.findByIrTypeAndSerialAndActionAndModelOrUserEmail(ir.getIrType(), ir.getSerial(), ir.getAction(), ir.getModel(), ir.getUserEmail());
        for (int i = 0; i < list.size(); i++) {
            HashMap map = new HashMap();
            map.put("length", list.get(i).getLength());
            map.put("data", list.get(i).getData());
            value.add(map);
        }
        HashMap requestMap = new HashMap<>();
        requestMap.put("format", ir.getFormat());
        requestMap.put("rptcnt", ir.getFormat());
        requestMap.put("gap", ir.getFormat());
        requestMap.put("value", ir.getFormat());
        publishPayload.put("type", Type.control.name());
        publishPayload.put("request", requestMap);
        publish(topic, publishPayload);
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

        MqttPahoMessageHandler messageHandler = (MqttPahoMessageHandler) beanFactory.getBean("MqttOutbound");
        messageHandler.setDefaultTopic(topic);
        MqttGateway gateway = beanFactory.getBean(MqttGateway.class);
        String payload = objectMapper.writeValueAsString(publishPayload);
        logger.info("publish topic:::::::::::" + topic);
        gateway.sendToMqtt(payload);
    }


}
