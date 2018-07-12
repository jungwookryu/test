package com.ht.connected.home.backend.ipc.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ht.connected.home.backend.ipc.controller.IPCAccessController;
import com.ht.connected.home.backend.ipc.model.entity.IPCAccount;

/**
 * HIK server 요청
 * 
 * @author 구정화
 *
 */
@Service
public class IPCRemoteRequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPCAccessController.class);

    private String HOST = "https://open.ezvizlife.com";
    private String CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String APP_KEY = "a3da8af54fc743d9a9d3d5016de006d3";
    public static final String APP_SECRET = "218fe6b25aed46fdbeea8d0c63ac845d";
    private HttpHeaders httpHeaders;

    public static String areaDomain;

    /**
     * 헤더 설정
     */
    public IPCRemoteRequestService() {
        httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", CONTENT_TYPE);
        httpHeaders.set("Host", HOST);
    }

    /**
     * 마스터 계정 토큰 요청
     * 
     * @return
     */
    public ResponseEntity<String> getIPCMasterToken() {
        HttpEntity<String> entity = new HttpEntity<String>(String.format("appKey=%s&appSecret=%s", APP_KEY, APP_SECRET),
                httpHeaders);
        ResponseEntity<String> response = request(HOST + "/api/lapp/token/get", entity);
        return response;
    }

    /**
     * 서브계정 토큰 요청
     * 
     * @param accessToken
     * @param accountId
     * @return
     */
    public ResponseEntity<String> getIPCSubAccountToken(String accessToken, String accountId) {
        HttpEntity<String> entity = new HttpEntity<String>(
                String.format("accessToken=%s&accountId=%s", accessToken, accountId), httpHeaders);
        ResponseEntity<String> response = request(getRequestURL("/api/lapp/ram/token/get"), entity);
        return response;
    }

    /**
     * 기기 등록
     * 
     * @param accessToken
     * @param accountId
     * @return
     */
    public ResponseEntity<String> registerDevice(String accessToken, String deviceSerial, String validateCode) {
        HttpEntity<String> entity = new HttpEntity<String>(String
                .format("accessToken=%s&deviceSerial=%s&validateCode=%s", accessToken, deviceSerial, validateCode),
                httpHeaders);
        ResponseEntity<String> response = request(getRequestURL("/api/lapp/device/add"), entity);
        return response;
    }

    /**
     * 서브계정 권한 변경
     * 
     * @param accessToken
     * @param accountId
     * @return
     */
    public ResponseEntity<String> stateAccountPermission(String accessToken, String accountId, String statement) {
        HttpEntity<String> entity = new HttpEntity<String>(
                String.format("accessToken=%s&accountId=%s&statement=%s", accessToken, accountId, statement),
                httpHeaders);
        ResponseEntity<String> response = request(getRequestURL("/api/lapp/ram/statement/add"), entity);
        return response;
    }

    /**
     * 기기 삭제
     * 
     * @param accessToken
     * @param accountId
     * @return
     */
    public ResponseEntity<String> deleteDevice(String accessToken, String deviceSerial) {
        HttpEntity<String> entity = new HttpEntity<String>(
                String.format("accessToken=%s&deviceSerial=%s", accessToken, deviceSerial), httpHeaders);
        ResponseEntity<String> response = request(getRequestURL("/api/lapp/device/delete"), entity);
        return response;
    }

    /**
     * 서브게정 생성
     * 
     * @param accessToken
     * @param accountId
     * @return
     */
    public ResponseEntity<String> createSubAccount(String accessToken, String accountName, String password) {
        HttpEntity<String> entity = new HttpEntity<String>(
                String.format("accessToken=%s&accountName=%s&password=%s", accessToken, accountName, password),
                httpHeaders);
        ResponseEntity<String> response = request(getRequestURL("/api/lapp/ram/account/create"), entity);
        return response;
    }

    /**
     * HIK 서버 요청
     * 
     * @param entity
     * @return
     */
    public ResponseEntity<String> request(String url, HttpEntity<String> entity) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        LOGGER.info(String.format("IPC HIK request entity : %s", entity.toString()));
        HttpStatus.Series series = response.getStatusCode().series();
        if (HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series)) {
            String responseBody = response.getBody();
            LOGGER.error("======= IPC HIK SERVER REQUEST FAILED =======");
            LOGGER.error(responseBody);
        } else {
            LOGGER.error("======= IPC HIK SERVER REQUEST SUCCESS =======");
            LOGGER.info(response.getBody());
        }
        return response;
    }

    /**
     * 마스터 게정 요청 유알엘 생성
     * 
     * @param url
     * @return
     */
    private String getRequestURL(String url) {
        return IPCMasterAccessTokenService.masterAccount.getAreaDomain() + url;
    }

    /**
     * 서브계정 요청 유알엘 생성
     * 
     * @param areaDomain
     * @param url
     * @return
     */
    private String getRequestURL(String areaDomain, String url) {
        return areaDomain + url;
    }

    /**
     * 프리셋 삭제 요청
     * 
     * @param account
     * @param request
     * @return
     */
    public ResponseEntity<String> deleteDevicePreset(IPCAccount account, HashMap<String, String> request) {
        HttpEntity<String> entity = new HttpEntity<String>(
                String.format("accessToken=%s&deviceSerial=%s&channelNo=%s&index=%s", account.getAccessToken(),
                        request.get("deviceSerial"), request.get("channel"), request.get("preset")),
                httpHeaders);
        ResponseEntity<String> response = request(
                getRequestURL(account.getAreaDomain(), "/api/lapp/device/preset/clear"), entity);
        return response;
    }

    /**
     * 프리셋 등록
     * 
     * @param account
     * @param request
     * @return
     */
    public ResponseEntity<String> addDevicePreset(IPCAccount account, HashMap<String, String> request) {
        HttpEntity<String> entity = new HttpEntity<String>(
                String.format("accessToken=%s&deviceSerial=%s&channelNo=%s", account.getAccessToken(),
                        request.get("deviceSerial"), request.get("channel")),
                httpHeaders);
        ResponseEntity<String> response = request(
                getRequestURL(account.getAreaDomain(), "/api/lapp/device/preset/add"), entity);
        return response;
    }
    
    

}
