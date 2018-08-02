package com.ht.connected.home.backend.ipc.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ht.connected.home.backend.controller.rest.CommonController;
import com.ht.connected.home.backend.ipc.model.entity.IPCDevicePreset;
import com.ht.connected.home.backend.ipc.service.IPCAccessService;

/**
 * IPC 기기 및 프리셋 등록/삭제 요청 컨트롤러
 * 
 * @author 구정화
 *
 */
@RestController
@RequestMapping("/ipc")
public class IPCAccessController extends CommonController {

    @Autowired
    private IPCAccessService accessService;

    String authorizedUserEmail = "light@light.com";

    /**
     * Connected Home 엑세스 토큰으로 요청했을경우 또는 테스트용 이메일 반환
     * 
     * @return
     */
    private String getIotAccountName() {
        String authUserEmail = null;
        authUserEmail = getAuthUserEmail();
        if (authUserEmail.equals("")) {
            authUserEmail = authorizedUserEmail;
        }
        return authUserEmail;
    }

    /**
     * 서브계정 엑세스 토큰 생성
     * 
     * @return
     */
    @GetMapping("/token")
    public ResponseEntity<String> getAccessToken() {
        return accessService.getSubAccountAccessToken(getIotAccountName());
    }

    /**
     * 서브계정 생성
     * 
     * @param request
     * @return
     */
    @PostMapping("/account")
    public ResponseEntity<String> createAccount() {
        return accessService.createSubAccount(getIotAccountName());
    }

    /**
     * 기기등록
     * 
     * @param request
     * @return
     */
    @PostMapping("/device")
    public ResponseEntity<String> registerDevice(@RequestBody HashMap<String, String> request) {
        return accessService.registerDevice(request, getIotAccountName());
    }

    /**
     * 기기 공유
     * 
     * @param request
     * @return
     */
    @PutMapping("/device")
    public ResponseEntity<String> shareDeivce(@RequestBody HashMap<String, String> request) {
        return accessService.shareDevice(request, null);
    }

    /**
     * 기기 삭제
     * 
     * @param request
     * @return
     */
    @PostMapping("/device/delete")
    public ResponseEntity<String> deleteDevice(@RequestBody HashMap<String, String> request) {
        return accessService.deleteDevice(request, getIotAccountName());
    }

    /**
     * 프리셋 닉네임 등록
     * 
     * @param request
     * @return
     */
    @PostMapping("/preset/register")
    public ResponseEntity<String> updateDevicePreset(@RequestBody IPCDevicePreset request) {
        request.setIotAccount(getIotAccountName());        
        return accessService.updateDevicePreset(request);
    }

    /**
     * 프리셋 리스트
     * 
     * @param request
     * @return
     */
    @PostMapping("/preset/list")
    public ResponseEntity<String> getDevicePresets(@RequestBody HashMap<String, String> request) {
        String deviceSerial = request.get("deviceSerial");               
        return accessService.getDevicePresets(deviceSerial, getIotAccountName());
    }
    
    /**
     * 프리셋 삭제
     * 
     * @param request
     * @return
     */
    @PostMapping("/preset/remove")
    public ResponseEntity<String> deleteDevicePreset(@RequestBody IPCDevicePreset request) {
        request.setIotAccount(getIotAccountName());        
        return accessService.deleteDevicePreset(request);
    }

}
