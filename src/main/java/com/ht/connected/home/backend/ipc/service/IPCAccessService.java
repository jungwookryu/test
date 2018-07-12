package com.ht.connected.home.backend.ipc.service;

import static java.util.Objects.isNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.ipc.controller.IPCAccessController;
import com.ht.connected.home.backend.ipc.model.entity.IPCAccount;
import com.ht.connected.home.backend.ipc.model.entity.IPCDevice;
import com.ht.connected.home.backend.ipc.model.entity.IPCDevicePreset;
import com.ht.connected.home.backend.ipc.repository.IPCAccountRepository;
import com.ht.connected.home.backend.ipc.repository.IPCDevicePresetRepository;
import com.ht.connected.home.backend.ipc.repository.IPCDeviceRepository;

/**
 * 앱 요청 처리 비지니스 로직
 * 
 * @author 구정화
 *
 */
@Service
public class IPCAccessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPCAccessController.class);

    @Autowired
    private IPCAccountRepository accountRepository;

    @Autowired
    private IPCRemoteRequestService remoteRequestService;

    @Autowired
    private IPCMasterAccessTokenService masterAccessTokenService;

    @Autowired
    private IPCDeviceRepository deviceRepository;

    @Autowired
    private IPCDevicePresetRepository devicePresetRepository;

    private String testAccountTail = "test2";

    private String subAccountPrefix = "hdtel_";

    @Autowired
    private Environment env;

    /**
     * 서브계정 생성
     * 
     * @param request
     * @return
     */
    public ResponseEntity<String> createSubAccount(String iotAccount) {
        String profile = env.getRequiredProperty("spring.profiles.active");
        if (profile.equals("develop") == false) {
            testAccountTail = "";
        }
        String accountName = subAccountPrefix + iotAccount.replace("@", "_").replaceAll("[^A-Za-z0-9_]", "")
                + testAccountTail;
        String password = Common.encryptHash(MessageDigestAlgorithms.SHA_256, iotAccount);
        password = Common.encryptHash(MessageDigestAlgorithms.MD5, IPCRemoteRequestService.APP_KEY + "#" + password)
                .toLowerCase();
        ResponseEntity<String> response = remoteRequestService
                .createSubAccount(masterAccessTokenService.getMasterAccessToken(), accountName, password);
        if (isSuccessResponseStatus(response)) {
            String accountId = getResponseDataValue(response.getBody(), "accountId").toString();
            if (!isNull(accountId)) {
                IPCAccount account = new IPCAccount();
                account.setAccountId(accountId);
                account.setIotAccount(iotAccount);
                account.setAccountName(accountName);
                account.setCreatedAt(IPCMasterAccessTokenService.dateFormat.format(new Date()));
                account.setUpdatedAt(IPCMasterAccessTokenService.dateFormat.format(new Date()));
                account.setAreaDomain(getResponseDataValue(response.getBody(), "areaDomain").toString());
                accountRepository.save(account);
            }
        }
        return response;
    }

    /**
     * 서브계정 엑세스 토큰을 하이크비전 서버에 요청 서브게정이 없을경우 서브계정 생성 후 엑세스 토큰 발급 요청하고 앱에 응답
     * 
     * @param iotAccount
     * @return
     */
    public ResponseEntity<String> getSubAccountAccessToken(String iotAccount) {
        ResponseEntity<String> response = null;
        IPCAccount account = accountRepository.findByIotAccount(iotAccount);
        if (!isNull(account)) {
            if (isNull(account.getAccessToken())) {
                masterAccessTokenService.getMasterAccessToken();
                LOGGER.info(String.format("요청시작, 서브계정명 : %s", account.getAccountName()));
                response = remoteRequestService.getIPCSubAccountToken(
                        IPCMasterAccessTokenService.masterAccount.getAccessToken(), account.getAccountId());
                updateSubAccountAccessToken(response, account);
                response = getEncodedAreaDomainReponseEntity(response);
            } else if (IPCMasterAccessTokenService.isAccessTokenExpired(account.getTokenExpireAt())) {
                response = remoteRequestService.getIPCSubAccountToken(
                        IPCMasterAccessTokenService.masterAccount.getAccessToken(), account.getAccountId());
                updateSubAccountAccessToken(response, account);
                response = getEncodedAreaDomainReponseEntity(response);
            } else {
                response = getSubAccountAccessTokenResponseEntity(account);
            }
        } else {
            LOGGER.info(String.format("서브계정 없음, iot account : %s", iotAccount));
            createSubAccount(iotAccount);
            response = getSubAccountAccessToken(iotAccount);
        }

        return response;
    }

    /**
     * areaDomain 유알엘 인코딩
     * 
     * @param response
     * @return
     */
    private ResponseEntity<String> getEncodedAreaDomainReponseEntity(ResponseEntity<String> response) {
        try {
            JSONObject jsonObject = new JSONObject(response.getBody());
            JSONObject jsonObjectData = (JSONObject) jsonObject.get("data");
            String encodedAreaDomain = URLEncoder.encode(jsonObjectData.get("areaDomain").toString(),
                    StandardCharsets.UTF_8.name());
            jsonObjectData.put("areaDomain", encodedAreaDomain);
            jsonObject.putOpt("data", jsonObjectData);
            response = new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }

    private void updateSubAccountAccessToken(ResponseEntity<String> response, IPCAccount account) {
        if (isSuccessResponseStatus(response)) {
            String accessToken = getResponseDataValue(response.getBody(), "accessToken").toString();
            LOGGER.info(String.format("서브게정 토큰, 서브계정명 : %s, 발급받은 토큰 : %s", account.getAccountName(), accessToken));

            long t = Long.valueOf(getResponseDataValue(response.getBody(), "expireTime").toString());
            Timestamp timestamp = new java.sql.Timestamp(t);
            String tokenExpireAt = IPCMasterAccessTokenService.dateFormat.format(timestamp);

            account.setAccessToken(accessToken);
            account.setTokenExpireAt(tokenExpireAt);
            account.setAreaDomain(getResponseDataValue(response.getBody(), "areaDomain").toString());
            accountRepository.save(account);
        }
    }

    /**
     * 유효한 인증토큰이 디비에 있는경우 앱에 응답할 JSON 오브젝트 생성
     * 
     * @param account
     * @return
     */
    private ResponseEntity<String> getSubAccountAccessTokenResponseEntity(IPCAccount account) {
        HashMap<String, Object> tmp = new HashMap<>();
        JSONObject tmp2 = new JSONObject();
        try {
            tmp2.put("accessToken", account.getAccessToken());
            tmp2.put("expireTime", IPCMasterAccessTokenService.dateFormat.parse(account.getTokenExpireAt()).getTime());
            tmp2.put("areaDomain", URLEncoder.encode(account.getAreaDomain(), StandardCharsets.UTF_8.name()));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tmp.put("data", tmp2);
        tmp.put("code", "200");
        tmp.put("msg", "Operation succeed !");
        return new ResponseEntity<String>(new JSONObject(tmp).toString(), HttpStatus.OK);
    }

    /**
     * HIK서버에서 응답한 JSON 파싱용
     * 
     * @param jsonString
     * @param key
     * @return
     */
    private Object getResponseDataValue(String jsonString, String key) {
        Object value = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (!isNull(key)) {
            JSONObject data = jsonObject.optJSONObject("data");
            value = data.optString(key);
            if (isNull(value)) {
                value = data.optLong(key);
            }
        } else {
            value = jsonObject;
        }
        return value;
    }

    /**
     * 기기 등록
     * 
     * @param request
     * @param iotAccount
     * @return
     */
    public ResponseEntity<String> registerDevice(HashMap<String, String> request, String iotAccount) {
        ResponseEntity<String> response = remoteRequestService.registerDevice(
                IPCMasterAccessTokenService.masterAccount.getAccessToken(), request.get("deviceSerial"),
                request.get("validateCode"));
        if (isSuccessResponseStatus(response)) {
            IPCAccount account = accountRepository.findByIotAccount(iotAccount);
            if (isNull(account)) {
                createSubAccount(iotAccount);
                account = accountRepository.findByIotAccount(iotAccount);
            }
            response = shareDevice(request, account);
            if (isSuccessResponseStatus(response)) {
                IPCDevice device = deviceRepository.findByDeviceSerial(request.get("deviceSerial"));
                if (isNull(device)) {
                    device = new IPCDevice();
                }
                device.setAccountSeq(account.getSeq());
                device.setIsOwner("Y");
                device.setDeviceSerial(request.get("deviceSerial"));
                device.setCreatedAt(IPCMasterAccessTokenService.dateFormat.format(new Date()));
                deviceRepository.save(device);
            }
        }
        return response;
    }

    /**
     * HIK서버 응답 상태 성공 여부
     * 
     * @param response
     * @return
     */
    private boolean isSuccessResponseStatus(ResponseEntity<String> response) {
        return ((JSONObject) getResponseDataValue(response.getBody(), null)).optString("code").equals("200");
    }

    /**
     * 공유신청
     * 
     * @param request
     * @param account
     * @return
     */
    public ResponseEntity<String> shareDevice(HashMap<String, String> request, IPCAccount account) {
        ResponseEntity<String> response = null;
        if (isNull(account)) {
            account = accountRepository.findByIotAccount(request.get("sharedEmail"));
            if (isNull(account)) {
                response = createSubAccount(request.get("sharedEmail"));
                if (isSuccessResponseStatus(response)) {
                    account = accountRepository.findByIotAccount(request.get("sharedEmail"));
                }
            }
        }
        if (!isNull(account)) {
            String statement = String.format(
                    "{\"Permission\": \"Get,Update,Real,Replay,DevCtrl,Video\", \"Resource\": [\"dev:%s\"]}",
                    request.get("deviceSerial"));
            response = remoteRequestService.stateAccountPermission(
                    IPCMasterAccessTokenService.masterAccount.getAccessToken(), account.getAccountId(), statement);
        }
        return response;
    }

    /**
     * 기기 삭제
     * 
     * @param request
     * @return
     */
    public ResponseEntity<String> deleteDevice(HashMap<String, String> request, String iotAccount) {
        deviceRepository.deleteByDeviceSerial(request.get("deviceSerial"));
        devicePresetRepository.deleteByDeviceSerial(request.get("deviceSerial"));
        ResponseEntity<String> response = remoteRequestService
                .deleteDevice(IPCMasterAccessTokenService.masterAccount.getAccessToken(), request.get("deviceSerial"));
        return response;
    }

    /**
     * 프리셋 닉네임 저장
     * 
     * @param request
     * @param iotAccountName
     * @return
     */
    public ResponseEntity<String> updateDevicePreset(IPCDevicePreset request) {
        ResponseEntity<String> result = null;
        List<IPCDevicePreset> devicePresets = devicePresetRepository.getAccountDevicePreset(request.getDeviceSerial(),
                request.getIotAccount());
        boolean presetFound = false;
        if (devicePresets.size() > 0) {             
            for (IPCDevicePreset dp : devicePresets) {
                if (dp.getPresetId().equals(request.getPresetId())
                        && dp.getChannelNo().equals(request.getChannelNo())) {                    
                    result = getDevicePresetUpdateResponse(updateDevicePreset(dp, request));
                    presetFound = true;
                    break;
                }
            }            
        }
        
        if(!presetFound){
            ResponseEntity<String> preset = addDevicePreset(request);
            if(isSuccessResponseStatus(preset)) {
                IPCDevicePreset dp = new IPCDevicePreset();
                request.setPresetId(getResponseDataValue(preset.getBody(), "index").toString());
                result = getDevicePresetUpdateResponse(updateDevicePreset(dp, request));
            }else {
                result = preset;
            }
        }        
        return result;
    }
    
    /**
     * 프리셋 디비 업데이트 
     * 
     * @param devicePreset
     * @param request
     * @return
     */
    private IPCDevicePreset updateDevicePreset(IPCDevicePreset devicePreset, IPCDevicePreset request) {
        devicePreset.setDeviceSerial(request.getDeviceSerial());
        devicePreset.setPresetId(request.getPresetId());
        devicePreset.setNickname(request.getNickname());
        devicePreset.setChannelNo(request.getChannelNo());
        devicePresetRepository.save(devicePreset);
        return devicePreset;        
    }
    
    /**
     * 프리셋 저장 후 응답 데이터넷 생성
     * 
     * @param devicePreset
     * @return
     */
    private ResponseEntity<String> getDevicePresetUpdateResponse(IPCDevicePreset devicePreset) {
        HashMap<String, Object> tmp = new HashMap<>();
        JSONObject tmp2 = new JSONObject();
        try {
            tmp2.put("nickname", devicePreset.getNickname());
            tmp2.put("preset", devicePreset.getPresetId());
            tmp2.put("deviceSerial", devicePreset.getDeviceSerial());
            tmp2.put("channel", devicePreset.getChannelNo());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tmp.put("code", "200");
        tmp.put("msg", "Operation succeed !");
        tmp.put("data", tmp2);
        return new ResponseEntity<String>(new JSONObject(tmp).toString(), HttpStatus.OK);
    }

    /**
     * 프리셋 저장 요청
     * 
     * @param request
     * @return
     */
    private ResponseEntity<String> addDevicePreset(IPCDevicePreset request) {
        IPCAccount account = accountRepository.findByIotAccount(request.getIotAccount());
        return remoteRequestService.addDevicePreset(account, request);
    }

    /**
     * 프리셋 정보 응답값 생성
     * 
     * @param request
     * @param iotAccountName
     * @return
     */
    public ResponseEntity<String> getDevicePresets(String deviceSerial, String iotAccount) {
        List<IPCDevicePreset> devicePresets = devicePresetRepository.getAccountDevicePreset(deviceSerial, iotAccount);
        JSONArray json = new JSONArray();
        devicePresets.stream().forEach(preset -> {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("preset", preset.getPresetId());
                jsonObject.put("nickname", preset.getNickname());
                jsonObject.put("channel", preset.getChannelNo());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            json.put(jsonObject);
        });
        return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
    }

    /**
     * 프리셋 삭제
     * 
     * @param request
     * @return
     */
    public ResponseEntity<String> deleteDevicePreset(IPCDevicePreset request) {
        IPCAccount account = accountRepository.findByIotAccount(request.getIotAccount());
        devicePresetRepository.deleteByDeviceSerialAndChannelNoAndPresetId(request.getDeviceSerial(),
                request.getChannelNo(), request.getPresetId());
        ResponseEntity<String> response = remoteRequestService.deleteDevicePreset(account, request);
        return response;
    }

}
