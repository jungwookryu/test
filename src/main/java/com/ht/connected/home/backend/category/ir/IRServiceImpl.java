package com.ht.connected.home.backend.category.ir;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import javax.transaction.Transactional;

import org.json.JSONException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.category.gateway.GatewayRepository;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.common.MqttCommon;
import com.ht.connected.home.backend.controller.mqtt.Message;
import com.ht.connected.home.backend.controller.mqtt.ProducerComponent;
import com.ht.connected.home.backend.gatewayCategory.CategoryActive;
import com.ht.connected.home.backend.gatewayCategory.GatewayCategoryRepository;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;
import com.ht.connected.home.backend.service.mqtt.Target;
import com.ht.connected.home.backend.zapp.AppController;

@Service
public class IRServiceImpl extends CrudServiceImpl<IR, Integer> implements IRService {

    Logger logger = LoggerFactory.getLogger(IRServiceImpl.class);

    public enum Type {
        add, control
    }

    @Autowired
    IRRepository irRepository;

    @Autowired
    GatewayRepository gatewayRepository;

    @Autowired
    GatewayCategoryRepository gatewayCategoryRepository;

    @Autowired
    ProducerComponent producerComponent;

    @Autowired
    @Qualifier(value = "callbackAckProperties")
    Properties callbackAckProperties;
    
    public IRServiceImpl(IRRepository irRepository) {
        super(irRepository);
        this.irRepository = irRepository;
    }

    @Override
    public List<IR> getIRByUser(String userEmail, String serial) {
        return irRepository.findByUserEmailContainingAndStatusAndSerial(userEmail, Type.add.name(), serial);
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public void delete(List<Integer> nos) {
        for (int i = 0; i < nos.size(); i++) {
            delete(nos.get(i).intValue());
        }
    }

    @Override
    @Transactional
    public void delete(int no) {
        // 삭제모드 없음.
        if (irRepository.exists(no)) {
            // 기기 종류 등록모드 삭제
            irRepository.delete(no);
        }
        // 기기 종류 등록모드에 파생되어있는 기기 삭제
        if (irRepository.getBySubNumber(no).size() > 0) {
            irRepository.deleteBySubNumber(no);
        }
    }

    @Override
    @Transactional
    public void deleteIrs(int gatewayNo, String userEmail) {
        // TODO irCategory 삭제
        gatewayCategoryRepository.deleteByGatewayNoAndCategoryNo(gatewayNo, CategoryActive.gateway.ir.ordinal());
        irRepository.deleteByUserEmailContainingAndGatewayNo(userEmail, gatewayNo);
    }

    @Override
    @Transactional
    public void studyIR(IR ir) throws JsonProcessingException, InterruptedException {

        if (AppController.Command.start.name().equals(ir.getStatus()) || AppController.Command.stop.name().equals(ir.getStatus())) {
            // publish
            String topic = getMqttPublishTopic(ir, Target.host.name(), Target.server.name());
            HashMap<String, Object> publishPayload = new HashMap<String, Object>();
            publishPayload.put("type", Type.add.name());
            publishPayload.put("request", CategoryActive.gateway.ir.name());
            publishPayload.put("action", ir.getStatus());
            publish(topic, publishPayload);
            if (AppController.Command.stop.name().equals(ir.getStatus())) {
                irRepository.deleteByUserEmailContainingAndGatewayNoAndStatus(ir.getUserEmail(), ir.getGatewayNo(), "");
            }
        }

        if (ir.getStatus().isEmpty()) {
            List<IR> lst = irRepository.findByUserEmailContainingAndSubNumberAndAction(ir.getUserEmail(), ir.getSubNumber(), ir.getAction());
            if (lst.size() == 0) {
                irRepository.save(ir);
            } else {
                lst.get(0).setStatus("");
                lst.get(0).setLastmodifiedTime(new Date());
                irRepository.save(lst.get(0));
            }
        }

    }

    @Override
    public void subscribe(String[] topicSplited, String payload) throws JsonParseException, JsonMappingException, IOException, JSONException, InterruptedException {
        if (topicSplited.length > 4) {
            String targetType = topicSplited[1];
            String model = topicSplited[3];
            String serial = topicSplited[4];
            if (Common.notEmpty(payload)) {
                HashMap<String, Object> map = objectMapper.readValue(payload, HashMap.class);
                if (Type.add.name().equals((String) map.getOrDefault("type", ""))
                        && !AppController.Command.stop.name().equals(map.getOrDefault("action", ""))) {
                    HashMap rtnMap = (HashMap) map.getOrDefault("response", new HashMap());
                    List<IR> irs = irRepository.findBySerialAndStatusAndModel(serial, "", model);
                    if (irs.size() != 0) {
                        IR ir = irs.get(0);
                        List lst = (List) rtnMap.getOrDefault("value", new ArrayList<>());
                        int gap = (int) rtnMap.getOrDefault("gap", 0);
                        String format = (String) rtnMap.getOrDefault("format", "");
                        int rptcnt = (int) rtnMap.getOrDefault("rptcnt", 0);
                        ir.setStatus("active");
                        ir.setValue(objectMapper.writeValueAsString(lst));
                        ir.setFormat(format);
                        ir.setGap(gap);
                        ir.setRptcnt(rptcnt);
                        ir.setLastmodifiedTime(new Date());
                        irRepository.save(ir);
                        String topic = callbackAckProperties.getProperty("ir.study.complete");
                        String exeTopic = MqttCommon.rtnCallbackAck(topic, Target.app.name(), model, serial);
                        publish(exeTopic, new HashMap<>());
                    }
                }
            }
        }
    }

    @Override
    public void controlIR(IR reqIr) throws JsonProcessingException, ParseException, InterruptedException {
        HashMap<String, Object> publishPayload = new HashMap<String, Object>();
        List<IR> irs  = new ArrayList<>();
        if(reqIr.getNo()!=0) {
            irs = irRepository.findByUserEmailContainingAndNo(reqIr.getUserEmail(), reqIr.getNo());
            if (irs.size() > 0) {
                IR ir = irs.get(0);
                String topic = getMqttPublishTopic(ir, Target.host.name(), Target.server.name());
                HashMap requestMap = new HashMap<>();
                requestMap.put("format", ir.getFormat());
                requestMap.put("rptcnt", ir.getRptcnt());
                requestMap.put("gap", ir.getGap());
                JSONParser parser = new JSONParser();
                Object obj = parser.parse("{}");
                if(!Common.empty(ir.getValue())) {
                    obj = parser.parse(ir.getValue());
                }
                requestMap.put("value", obj);
                publishPayload.put("type", Type.control.name());
                publishPayload.put("request", requestMap);
                publish(topic, publishPayload);
            }
        }
    }
    @Override
    public void modifyIRs(List<IR> irs) {
        irs.forEach(IR->{irRepository.setModifyIrNameForNo(IR.getIrName(), IR.getNo());});
    }
    /**
     * mqtt publish 토픽 생성
     * @param topicLeadingPath
     * @return
     */
    public String getMqttPublishTopic(IR ir, String target, String source) {
        String topic = "";
        if (!Objects.isNull(ir.getStatus())) {
            String[] segments = new String[] { "/" + source, target, ir.getModel(), ir.getSerial(), CategoryActive.gateway.ir.name() };
            topic = String.join("/", segments);
            logger.info("====================== IR PROTO MQTT PUBLISH TOPIC ======================");
            logger.info(topic);
        }
        return topic;
    }

    public void publish(String topic, HashMap<String, Object> publishPayload) throws JsonProcessingException, InterruptedException {
        String payload = objectMapper.writeValueAsString(publishPayload);
        Message message =  new Message(topic, payload);
        MqttCommon.publish(producerComponent, message);
    }

}
