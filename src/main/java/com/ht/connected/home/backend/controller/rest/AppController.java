package com.ht.connected.home.backend.controller.rest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
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

	public enum Command{
	    start, stop
	}
    /**
     * 2.2.10 리모컨 기기 추가/삭제
     * IR 리모컨 기기 추가 및 삭제시 (command 값으로 구분) 호출되는 API.  (이벤트 상태값은 host와 주기적인 통신 후 저장된 최종값을 어플로 전달)
     * @param requestDto
     * @return
     */
    @PostMapping("/irAddDel")
    public ResponseEntity irAddDel(@RequestBody HashMap hashMap) {
        HashMap<String, Object> rtnMap = new HashMap();
        rtnMap.put("result_code", "200");
        rtnMap.put("result_msg", "Success");
        String command = (String) hashMap.getOrDefault("command","");
        if(null!=hashMap.get("irindex")) {
            int irindex = (int) hashMap.get("irindex");
            if("del".equals(command)) {
                ResponseEntity rss = iRController.deleteIR(irindex);
                rtnMap.replace("result_code",rss.getStatusCodeValue());
                rtnMap.replace("result_msg",rss.getHeaders().get("message"));
            }    
        }
        return new ResponseEntity<>(rtnMap, HttpStatus.OK);
    }
    
    /**
     * IR 리모컨 리스트
     * 앱과 월패드 공통 사용
     * 앱 - 2.2.7
     * 월패드 - 2.2.5
     * IR 리모컨 관련 기기의 목록을 가져오는 공통 API (월패드의 경우 에어컨만 지원)
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
        if(rss.getStatusCodeValue()==HttpStatus.OK.hashCode()) {
            HashMap<String, List<?>> map = (HashMap<String, List<?>>)iRController.getIR().getBody();
            List<IR> lstIR = (List<IR>) map.getOrDefault("list", new ArrayList());
            for (int i = 0; i < lstIR.size(); i++) {
                IR iR = lstIR.get(i);
                HashMap irMap = new HashMap();
                irMap.put("irname",iR.getIrName());
                irMap.put("irindex", Integer.toString(iR.getNo()));
                rtnList.add(irMap);
            }
        }
        rtnIrcategoryMap.put("ircategory", rtnList);
        rtnMap.put("result_data", rtnIrcategoryMap);
        return new ResponseEntity<>(rtnMap, HttpStatus.OK);
    }

    
    /**
     * 2.2.35. 리모컨 팝업 화면
     * 학습이 완료된 리모컨의 경우 학습된 버튼값을 받아와서 UI를 업데이트 해주기위한 API
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
        if(hashMap.get("ir_type")==null) {
            rtnMap.replace("result_msg", "No Contents");
        }else {
            String irType = (String) hashMap.get("ir_type");
            ResponseEntity<List<IR>> rss = iRController.getIRType(irType);
            if(rss.getStatusCodeValue()==200) {
                List<IR> irs = rss.getBody();
                for (int i = 0; i < irs.size(); i++) {
                    IR ir= irs.get(i);
                    rtnIrbuttonList.add(ir.getAction());
                }
            }else {
                rtnMap.put("result_msg", rss.getHeaders().get("message"));
            }
        }
        rtnDataMap.put("ir_button", rtnIrbuttonList);
        rtnMap.put("result_data", rtnDataMap);
        return new ResponseEntity<>(rtnMap, HttpStatus.OK);
    }
    

    /**
     * 2.2.11 IR 리모컨 학습 모드 요청
     * 리모컨 학습전에 학습모드 전환을 유도하기 위한 함수. 학습시작 전과 학습 완료후 2번 호출된다.
     * @param requestDto
     * @return
     * @throws JsonProcessingException 
     * @throws Exception
     */
    @PostMapping(value = {"/irMode"})
    public ResponseEntity startOrEndLearnRemoteControl(@RequestBody HashMap hashMap) throws JsonProcessingException {
        HashMap<String, Object> rtnMap = new HashMap();
        rtnMap.put("result_code", "444");
        rtnMap.put("result_msg", "Not Acceptable");
        String command = (String) hashMap.getOrDefault("command", "");
        String serial = (String) hashMap.getOrDefault("serial", "");
        String model = (String) hashMap.getOrDefault("model", "");
        IR ir = new IR();
        ir.setSerial(serial);
        ir.setModel(model);
        ir.setAction(command);
        ir.setStatus(command);
        iRController.createStudyIR(ir);
        return new ResponseEntity<>(rtnMap, HttpStatus.OK);
    }

    /**
     * 2.2.12. IR 리모컨 학습 처리.
     * 리모컨 학습시 공통적으로 사용되는 함수. 리모컨의 경우 선풍기,
     * 에어컨등 각각의 대표적인 탬플릿 이미지가 있고 해당 이미지 버튼을 자율적으로 사용자가 맵핑후 사용.
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
        ir.setIrType((String) hashMap.getOrDefault("irtype",""));
        ir.setAction((String) hashMap.getOrDefault("actionname",""));
        iRController.createStudyIR(ir);
        return new ResponseEntity<>(rtnMap, HttpStatus.OK);
    }
    
	 /**
     * IR 리모컨 제어 처리
     * 앱과 월패드 공통 사용
     * 학습 후 학습된 버튼의 기능을 처리
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
        IR ir = new IR();
        ir.setIrType((String)hashMap.getOrDefault("irindex", ""));
        ir.setAction((String)hashMap.getOrDefault("actionname", ""));
        ResponseEntity<IR> rss = iRController.controlIR(ir.getNo(), ir);
        if(rss.getStatusCodeValue()==200) {
        }else {
            rtnMap.put("result_msg", rss.getHeaders().get("message"));
        }
        return new ResponseEntity<>(rtnMap, HttpStatus.OK);
    }

    /**
    * IR 리모컨 제어 처리
    * 앱과 월패드 공통 사용
    * 학습 후 학습된 버튼의 기능을 처리
    * @param requestDto
    * @return
    * @throws JsonProcessingException 
    * @throws Exception
    */
   @PostMapping("/uiIrInfo")
   public ResponseEntity uiIrInfo(@RequestBody HashMap hashMap) throws JsonProcessingException {
       HashMap<String, Object> rtnMap = new HashMap();
       HashMap<String, Object> rtnDataMap = new HashMap();
       List rtnIrbuttonList = new ArrayList();
       rtnMap.put("result_code", "200");
       rtnMap.put("result_msg", "Success");
       rtnMap.put("result_data", new HashMap());
       ResponseEntity rss = iRController.getIRTypeInfo();
       if(rss.getStatusCodeValue()==200) {
           rtnMap.replace("result_data", rss.getBody());
       }else {
           rtnMap.put("result_msg", rss.getHeaders().get("message"));
       }
       return new ResponseEntity<>(rtnMap, HttpStatus.OK);
   }


}
