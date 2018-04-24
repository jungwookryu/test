
//package com.ht.connected.home.backend.service.impl.zwave.handler;
//
//import static java.util.Objects.isNull;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import com.ht.connected.home.backend.config.service.ZwaveClassKey;
//import com.ht.connected.home.backend.config.service.ZwaveCommandKey;
//import com.ht.connected.home.backend.model.dto.MqttPayload;
//import com.ht.connected.home.backend.model.dto.ZwaveNodeListReport;
//import com.ht.connected.home.backend.model.dto.ZwaveRequest;
//import com.ht.connected.home.backend.model.entity.Certification;
//import com.ht.connected.home.backend.model.entity.Gateway;
//import com.ht.connected.home.backend.model.entity.Zwave;
//import com.ht.connected.home.backend.repository.ZwaveRepository;
//import com.ht.connected.home.backend.service.ZwaveService;
//import com.ht.connected.home.backend.service.impl.zwave.ZwaveDefault;
//
//@Service
//@Scope(value = "prototype")
//public class NetworkManagementProxy extends ZwaveDefault implements ZwaveService {
//
//    @Autowired
//    ZwaveRepository zwaveRepository;
//
//    @Override
//    public ResponseEntity execute(HashMap<String, Object> req, ZwaveRequest zwaveRequest, boolean isCert) {
//        this.isCert = isCert;
//        if ((zwaveRequest.getCommandKey()==ZwaveCommandKey.NODE_LIST_REPORT)
//                || (zwaveRequest.getCommandKey()==ZwaveCommandKey.NODE_INFO_CACHED_REPORT)) {
//            return getPayload(req, zwaveRequest);
//        } else {
//            return publish(req, zwaveRequest);
//        }
//    }
//
//    @Override
//    public void subscribe(ZwaveRequest zwaveRequest, String payload) {
//        try {
//            MqttPayload mqttPayload = objectMapper.readValue(payload, MqttPayload.class);
//            Object resultData = mqttPayload.getResultData();
//            String data = "";
//            if (!isNull(resultData)) {
//                data = objectMapper.writeValueAsString(resultData);
//                if (zwaveRequest.getCommandKey()==ZwaveCommandKey.NODE_LIST_REPORT) {
//                    /**
//                     * 기기 리스트 수신시  새로 등록한 기기가 있을경우
//                     */
//                    Gateway gateway = gatewayRepository.findBySerial(zwaveRequest.getSerialNo());
//                    if(!isNull(gateway)) {
//                        Zwave zwave = zwaveRepository.findByGatewayNoAndCmd(gateway.getNo(),
//                                Integer.toString(ZwaveClassKey.NETWORK_MANAGEMENT_INCLUSION) + "+" +Integer.toString(ZwaveCommandKey.NODE_ADD_STATUS));
//                        if (!isNull(zwave)) {
//                            List<Certification> certification = certificationRepository.findBySerialAndMethodAndContext(
//                                    zwaveRequest.getSerialNo(),  Integer.toString(ZwaveClassKey.NETWORK_MANAGEMENT_PROXY),
//                                    Integer.toString(ZwaveCommandKey.NODE_LIST_REPORT));
//                            String nodeListPayload = certification.get(0).getPayload();
//                            ZwaveNodeListReport zwaveNodeListReport = objectMapper.readValue(nodeListPayload,
//                                    ZwaveNodeListReport.class);
//                            ZwaveNodeListReport.NodeListItem nodeListItem = zwaveNodeListReport.getNodelist().stream()
//                                    .filter(node -> node.getNodeid().equals(String.valueOf(zwave.getNodeId())))
//                                    .collect(Collectors.toList()).get(0);
//                            nodeListPayload = objectMapper.writeValueAsString(nodeListItem);
//                            MqttPayload mqttMessage = new MqttPayload();
//                            HashMap<String,Object> nodeListMap = objectMapper.readValue(nodeListPayload, HashMap.class);
//                            mqttMessage.setResultData(nodeListMap);
//                            String topic = String.format("/server/app/%s/%s/zwave/certi/%s/%s/v1", gateway.getModel(),
//                                    gateway.getSerial(), ZwaveClassKey.NETWORK_MANAGEMENT_INCLUSION, ZwaveCommandKey.NODE_ADD);
//                            publish(topic, mqttMessage);
//                            zwaveRepository.delete(zwave);
//                        }
//                    }else {
//                        logging.info(String.format("Gateway Serial Number(%s) is not registered", zwaveRequest.getSerialNo()));; 
//                    }
//                }
//            }
//            updateCertification(zwaveRequest, data);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//}
