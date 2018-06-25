package com.ht.connected.home.backend.category.ir;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.crypto.hash.Hash;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.controller.rest.CommonController;
import com.ht.connected.home.backend.gateway.Gateway;
import com.ht.connected.home.backend.gateway.GatewayRepository;

@RestController
@RequestMapping("/irs")
public class IRController extends CommonController {

    IRService iRService;

    @Autowired
    IRRepository iRRepository;

    @Autowired
    GatewayRepository gatewayRepository;

    @Autowired
    public IRController(IRService iRService) {
        this.iRService = iRService;
    }

    public enum Devicetype {
        aircon, tv, fan;
    }

    /**
     * 201, 204, 500, 406
     * @param IR
     * @return
     * @throws UnsupportedEncodingException
     * @throws IllegalArgumentException
     * @throws NoSuchAlgorithmException
     */
    // 신규 학습
    @PostMapping
    public ResponseEntity<IR> createIR(@RequestBody IR ir) {
        ir.setUserEmail(getAuthUserEmail());
        if (ir.getGatewayNo() > 0) {
            ir = modifyIRForGateway(ir, ir.getGatewayNo());
        }else {
            ir = modifyIRForSerial(ir, ir.getSerial());
            
        }
        IR rtnIr = iRRepository.save(ir);
        return new ResponseEntity<IR>(rtnIr, HttpStatus.OK);
    }

    /**
     * 201, 204, 500, 406
     * @param IR
     * @return
     * @throws JsonProcessingException
     * @throws UnsupportedEncodingException
     * @throws IllegalArgumentException
     * @throws NoSuchAlgorithmException
     */
    // 신규 학습 모드 신청
    @PostMapping("/ir")
    public ResponseEntity createStudyIR(@RequestBody IR ir) throws JsonProcessingException {
        ir.setUserEmail(getAuthUserEmail());
        if (ir.getGatewayNo()>0) {
            ir = modifyIRForGateway(ir, ir.getGatewayNo());
        }else {
            ir = modifyIRForSerial(ir, ir.getSerial());
        }
        iRService.studyIR(ir);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 기기리스트
    @GetMapping
    public ResponseEntity getIR(@RequestParam Map<String, String> parameters) {
        String serial = (String) parameters.getOrDefault("serial", "");
        String userEmail = getAuthUserEmail();
        List<IR> lstIR = iRService.getIRByUser(userEmail, serial);
        HashMap<String, List<?>> map = new HashMap<String, List<?>>();
        map.put("list", lstIR);
        return new ResponseEntity(map, HttpStatus.OK);
    }

    // 기기정보 가져오기
    @GetMapping("/{irType}")
    public ResponseEntity<List<IR>> getIRType(@PathVariable("irType") int irType,
            @RequestParam HashMap<String, Object> hashMap) {
        String useEmail = getAuthUserEmail();
        String serial = (String) hashMap.getOrDefault("serial", "");
        String model = (String) hashMap.getOrDefault("model", "");
        List<IR> ir = iRRepository.findByIrTypeAndUserEmailContainingAndSerialAndModel(irType, useEmail, serial, model);
        return new ResponseEntity<List<IR>>(ir, HttpStatus.OK);
    }

    // 기기정보 가져오기
    @GetMapping("/irTypeInfo")
    public ResponseEntity getIRTypeInfo() {
        String useEmail = getAuthUserEmail();
        HashMap map = new HashMap();
        map.put(IRTypeInfo.devicetype.name(), Arrays.asList("aircon", "tv", "fan"));
        map.put(IRTypeInfo.devicemodel.name(), Arrays.asList("aircon", "tv", "fan"));
        map.put(IRTypeInfo.deviceiridx.name(), Arrays.asList(1, 2, 3));
        return new ResponseEntity(map, HttpStatus.OK);
    }

    // IR 기기 학습 삭제
    @DeleteMapping("/ir/{nos}")
    public ResponseEntity<HttpStatus> deleteIR(@PathVariable("nos") List<Integer> nos) {
        //ir_type, subnumber가 0인기기는 삭제할 수없음.
        String useEmail = getAuthUserEmail();
        if(nos.contains(0)) {
            return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
        }
        iRService.delete(nos);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    // 기기 제어
    @SuppressWarnings("rawtypes")
    @PutMapping("/ir/{no}")
    public ResponseEntity controlIR(@PathVariable("no") int no, @RequestBody IR ir) throws JsonProcessingException, ParseException {
        ir.setUserEmail(getAuthUserEmail());
        iRService.controlIR(ir);
        return new ResponseEntity(HttpStatus.OK);
    }

    private IR modifyIRForGateway(IR ir, int gatewayNo) {
        Gateway gateway = gatewayRepository.findOne(gatewayNo);
        if(gateway!=null) {
            if (Common.empty(ir.getUserEmail())||"anonymousUser".equals(ir.getUserEmail())) {
                ir.setUserEmail(gateway.getCreatedUserId());
            }
            if (Common.empty(ir.getModel())) {
                ir.setModel(gateway.getModel());
            }
            if (Common.empty(ir.getSerial())) {
                ir.setSerial(gateway.getSerial());
            }
            ir.setGatewayNo(gateway.getNo());
        }
        return ir;
    }
    private IR modifyIRForSerial(IR ir, String serial) {
        Gateway gateway = gatewayRepository.findBySerial(serial);
        if(gateway!=null) {
            if (Common.empty(ir.getUserEmail())||"anonymousUser".equals(ir.getUserEmail())) {
                ir.setUserEmail(gateway.getCreatedUserId());
            }
            if (Common.empty(ir.getModel())) {
                ir.setModel(gateway.getModel());
            }
            ir.setGatewayNo(gateway.getNo());
        }
        return ir;
    }
}
