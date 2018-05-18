package com.ht.connected.home.backend.controller.rest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sound.sampled.LineEvent.Type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ht.connected.home.backend.controller.rest.IRController.Devicetype;
import com.ht.connected.home.backend.model.dto.CategoryActive;
import com.ht.connected.home.backend.model.entity.IR;
import com.ht.connected.home.backend.service.impl.IRServiceImpl;
import com.rabbitmq.client.Command;

@RestController
@RequestMapping("/app")
public class AppController extends CommonController {

    IRController iRController;

    @Autowired
    public AppController(IRController iRController) {
        this.iRController = iRController;
    }

    public enum Command {
        start, stop
    }

    /**
     * 2.2.10 리모컨 기기 추가/삭제 IR 리모컨 기기 추가 및 삭제시 (command 값으로 구분) 호출되는 API. (이벤트 상태값은 host와 주기적인 통신 후 저장된 최종값을 어플로 전달)
     * @param requestDto
     * @return
     */
    @PostMapping("/irAddDel")
    public ResponseEntity irAddDel(@RequestBody HashMap hashMap) {
        HashMap<String, Object> rtnMap = new HashMap();
        rtnMap.put("result_code", "200");
        rtnMap.put("result_msg", "Success");
        String command = (String) hashMap.getOrDefault("command", "");
        ResponseEntity rss = null;
        if (null != hashMap.get("irindex")) {
            //subnumber
            int irindex = (int) hashMap.get("irindex");
            if ("del".equals(command)) {
                iRController.deleteIR(irindex);

            }
            if ("add".equals(command)) {
                String serial = (String) hashMap.getOrDefault("serial", "");
                String model = (String) hashMap.getOrDefault("model", "");
                String irnickname = (String) hashMap.getOrDefault("irnickname", "");
                int irchannel = (int) hashMap.getOrDefault("irchannel", 99);
                IR ir = new IR();
                ir.setSerial(serial);
                ir.setModel(model);
                ir.setIrName(irnickname);
                ir.setAction("add");
                ir.setStatus("add");
                ir.setDevType("irchannel::" + Integer.toString(irchannel));
                ir.setIrType(irindex);
                ir.setIrType(irindex);
                rss = iRController.createIR(ir);
            }
        }
        if (rss != null && rss.getStatusCodeValue() != 200) {
            rtnMap.replace("result_code", rss.getStatusCodeValue());
            rtnMap.replace("result_msg", rss.getHeaders().get("message"));
        }
        return new ResponseEntity<>(rtnMap, HttpStatus.OK);
    }

    /**
     * IR 리모컨 리스트 앱과 월패드 공통 사용 앱 - 2.2.7 월패드 - 2.2.5 IR 리모컨 관련 기기의 목록을 가져오는 공통 API (월패드의 경우 에어컨만 지원)
     * @param requestDto
     * @return
     */
    @PostMapping("/irDeviceList")
    public ResponseEntity irDeviceList() {
        ResponseEntity<HashMap<String, ?>> rss = iRController.getIR();
        HashMap<String, Object> rtnMap = new HashMap();
        HashMap<String, Object> rtnIrcategoryMap = new HashMap();
        rtnMap.put("result_code", "200");
        rtnMap.put("result_msg", "Success");
        List rtnList = new ArrayList();
        if (rss.getStatusCodeValue() == 200) {
            HashMap<String, List<?>> map = (HashMap<String, List<?>>) rss.getBody();
            List<IR> lstIR = (List<IR>) map.getOrDefault("list", new ArrayList());
            HashMap ircategoryMap = new HashMap();
            List lstCategory1 = new ArrayList();
            List lstCategory2 = new ArrayList();
            List lstCategory3 = new ArrayList();
            for (int i = 0; i < lstIR.size(); i++) {
                IR iR = lstIR.get(i);
                if (iR.getIrType() == 1) {
                    HashMap mapCategory1 = new HashMap();
                    mapCategory1.put("irname", iR.getIrName());
                    mapCategory1.put("irindex", Integer.toString(iR.getNo()));
                    lstCategory1.add(mapCategory1);
                }
                if (iR.getIrType() == 2) {
                    HashMap mapCategory2 = new HashMap();
                    mapCategory2.put("irname", iR.getIrName());
                    mapCategory2.put("irindex", Integer.toString(iR.getNo()));
                    lstCategory2.add(mapCategory2);
                }
                if (iR.getIrType() == 3) {
                    HashMap mapCategory3 = new HashMap();
                    mapCategory3.put("irname", iR.getIrName());
                    mapCategory3.put("irindex", Integer.toString(iR.getNo()));
                    lstCategory3.add(mapCategory3);
                }
            }
            if (lstCategory1.size() > 0) {
                HashMap rtnListMap = new HashMap();
                rtnListMap.put("categoryname", Devicetype.aircon.name());
                rtnListMap.put("list", lstCategory1);
                rtnList.add(rtnListMap);
            }
            if (lstCategory2.size() > 0) {
                HashMap rtnListMap = new HashMap();
                rtnListMap.put("categoryname", Devicetype.tv.name());
                rtnListMap.put("list", lstCategory2);
                rtnList.add(rtnListMap);
            }
            if (lstCategory3.size() > 0) {
                HashMap rtnListMap = new HashMap();
                rtnListMap.put("categoryname", Devicetype.fan.name());
                rtnListMap.put("list", lstCategory3);
                rtnList.add(rtnListMap);
            }
        }
        rtnIrcategoryMap.put("ircategory", rtnList);
        rtnMap.put("result_data", rtnIrcategoryMap);
        return new ResponseEntity<>(rtnMap, HttpStatus.OK);
    }

    /**
     * 2.2.35. 리모컨 팝업 화면 학습이 완료된 리모컨의 경우 학습된 버튼값을 받아와서 UI를 업데이트 해주기위한 API
     * @param requestDto
     * @return
     * @throws UnsupportedEncodingException
     * @throws IllegalArgumentException
     */
    @PostMapping(value = "/uiIRpopup")
    public ResponseEntity getIRPopup(@RequestBody HashMap hashMap) throws IllegalArgumentException, UnsupportedEncodingException {
        HashMap<String, Object> rtnMap = new HashMap();
        HashMap<String, Object> rtnDataMap = new HashMap();
        List rtnIrbuttonList = new ArrayList();
        rtnMap.put("result_code", "200");
        rtnMap.put("result_msg", "Success");
        if (hashMap.get("ir_type") == null) {
            rtnMap.replace("result_msg", "No Contents");
        } else {
            if (hashMap.get("ir_type") != null) {
                int irType = (int) hashMap.get("ir_type");
                ResponseEntity<List<IR>> rss = iRController.getIRType(irType, hashMap);
                if (rss.getStatusCodeValue() == 200) {
                    List<IR> irs = rss.getBody();
                    for (int i = 0; i < irs.size(); i++) {
                        IR ir = irs.get(i);
                        if(!"add".equals(ir.getAction())) {
                            rtnIrbuttonList.add(ir.getAction());
                        }
                    }
                } else {
                    rtnMap.put("result_msg", rss.getHeaders().get("message"));
                }
            }
        }
        rtnDataMap.put("ir_button", rtnIrbuttonList);
        rtnMap.put("result_data", rtnDataMap);
        return new ResponseEntity<>(rtnMap, HttpStatus.OK);
    }

    /**
     * 2.2.11 IR 리모컨 학습 모드 요청 리모컨 학습전에 학습모드 전환을 유도하기 위한 함수. 학습시작 전과 학습 완료후 2번 호출된다.
     * @param requestDto
     * @return
     * @throws JsonProcessingException
     * @throws Exception
     */
    @PostMapping(value = { "/irMode" })
    public ResponseEntity startOrEndLearnRemoteControl(@RequestBody HashMap hashMap) throws JsonProcessingException {
        HashMap<String, Object> rtnMap = new HashMap();
        rtnMap.put("result_code", "200");
        rtnMap.put("result_msg", "Success");
        String command = (String) hashMap.getOrDefault("command", "");
        String serial = (String) hashMap.getOrDefault("serial", "");
        String model = (String) hashMap.getOrDefault("model", "");
        if("end".equals(command)) {
            command=Command.stop.name();
        }
        IR ir = new IR();
        ir.setSerial(serial);
        ir.setModel(model);
        ir.setAction(command);
        ir.setStatus(command);
        iRController.createStudyIR(ir);
        return new ResponseEntity<>(rtnMap, HttpStatus.OK);
    }

    /**
     * 2.2.12. IR 리모컨 학습 처리. 리모컨 학습시 공통적으로 사용되는 함수. 리모컨의 경우 선풍기, 에어컨등 각각의 대표적인 탬플릿 이미지가 있고 해당 이미지 버튼을 자율적으로 사용자가 맵핑후 사용.
     * @param requestDto
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/irStudy")
    public ResponseEntity learnRemoteControl(@RequestBody HashMap hashMap) throws JsonProcessingException {

        HashMap<String, Object> rtnMap = new HashMap();
        rtnMap.put("result_code", "200");
        rtnMap.put("result_msg", "Success");
        String actionname = (String) hashMap.getOrDefault("actionname", "");
        String serial = (String) hashMap.getOrDefault("serial", "");
        String model = (String) hashMap.getOrDefault("model", "");
        IR ir = new IR();
        ir.setSerial(serial);
        ir.setModel(model);
        ir.setAction(actionname);
        ir.setStatus("");
        ir.setIrType((int) hashMap.getOrDefault("irtype", 99));
        ir.setSubNumber((int) hashMap.getOrDefault("irtype", 99));
        ir.setAction((String) hashMap.getOrDefault("actionname", ""));
        iRController.createStudyIR(ir);
        return new ResponseEntity<>(rtnMap, HttpStatus.OK);
    }

    /**
     * IR 리모컨 제어 처리 앱과 월패드 공통 사용 학습 후 학습된 버튼의 기능을 처리
     * @param requestDto
     * @return
     * @throws JsonProcessingException
     * @throws Exception
     */
    @PostMapping("/irControl")
    public ResponseEntity irControl(@RequestBody HashMap hashMap, HttpServletRequest request) throws JsonProcessingException {
        HashMap<String, Object> rtnMap = new HashMap();
        HashMap<String, Object> rtnDataMap = new HashMap();
        List rtnIrbuttonList = new ArrayList();
        rtnMap.put("result_code", "200");
        rtnMap.put("result_msg", "Success");
        if(hashMap.get("irindex")!=null){
            String serial = (String) hashMap.getOrDefault("serial", "");
            String model = (String) hashMap.getOrDefault("model", "");
            IR ir = new IR();
            ir.setSerial(serial);
            ir.setModel(model);
            ir.setSubNumber((int) hashMap.get("irindex"));
            ir.setAction((String) hashMap.getOrDefault("actionname", ""));
            ir.setStatus("control");
            ResponseEntity<IR> rss = iRController.controlIR(ir.getSubNumber(), ir);
            if (rss.getStatusCodeValue() == 200) {
            } else {
                rtnMap.put("result_msg", rss.getHeaders().get("message"));
            }
        }
        return new ResponseEntity<>(rtnMap, HttpStatus.OK);
    }

    /**
     * IR 리모컨 제어 처리 앱과 월패드 공통 사용 학습 후 학습된 버튼의 기능을 처리
     * @param requestDto
     * @return
     * @throws JsonProcessingException
     * @throws Exception
     */
    @PostMapping("/uiIrInfo")
    public ResponseEntity uiIrInfo() throws JsonProcessingException {
        HashMap<String, Object> rtnMap = new HashMap();
        HashMap<String, Object> rtnDataMap = new HashMap();
        List rtnIrbuttonList = new ArrayList();
        rtnMap.put("result_code", "200");
        rtnMap.put("result_msg", "Success");
        rtnMap.put("result_data", new HashMap());
        ResponseEntity rss = iRController.getIRTypeInfo();
        if (rss.getStatusCodeValue() == 200) {
            rtnMap.replace("result_data", rss.getBody());
        } else {
            rtnMap.put("result_msg", rss.getHeaders().get("message"));
        }
        return new ResponseEntity<>(rtnMap, HttpStatus.OK);
    }

}
