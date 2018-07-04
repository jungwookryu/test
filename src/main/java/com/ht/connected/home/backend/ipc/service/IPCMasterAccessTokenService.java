package com.ht.connected.home.backend.ipc.service;

import static java.util.Objects.isNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.ipc.model.entity.IPCAdministrate;
import com.ht.connected.home.backend.ipc.repository.IPCAdministrateRepository;

@Service
public class IPCMasterAccessTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPCMasterAccessTokenService.class);

    @Autowired
    private IPCAdministrateRepository administrateRepository;

    @Autowired
    private IPCRemoteRequestService remoteRequestService;
    
    public static IPCAdministrate masterAccount;

    public static SimpleDateFormat dateFormat;
    
    
    public IPCMasterAccessTokenService() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }
    
    @PostConstruct
    public void init() {
        getMasterAccessToken();
    }
    
    /**
     * 토큰 만료기간 유효성 검사
     * 
     * @param dateTime
     * @return
     */
    public static boolean isAccessTokenExpired(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.n");        
        long duration = ChronoUnit.SECONDS.between(LocalDateTime.now(), LocalDateTime.parse(dateTime, formatter));
        return 172800 >= duration;
    }

    /**
     * 마스터 게정 토큰 발급
     * 
     * @return
     */
    public String getMasterAccessToken() {
        String accessToken = null;        
        List<IPCAdministrate> administrates = administrateRepository.findAll();        
        if (administrates.size() > 0) {
            masterAccount = administrates.get(0);
            if (!isNull(masterAccount.getAccessToken())) {
                if (!isAccessTokenExpired(masterAccount.getTokenExpireAt())) {
                    accessToken = masterAccount.getAccessToken();
                }
            }
        } else {
            masterAccount = new IPCAdministrate();
        }
        accessToken = updateMasterAccessToken(accessToken);
        IPCRemoteRequestService.areaDomain = masterAccount.getAreaDomain();

        return accessToken;
    }

    private String updateMasterAccessToken(String accessToken) {
        /**
         * 엑세스 토큰이 없거나 만료 된경우 HIK 서버로 토큰 발급 요청
         */
        if (isNull(accessToken)) {
            ResponseEntity<String> responseMasterToken = remoteRequestService.getIPCMasterToken();
            try {
                JSONObject jsonObject = new JSONObject(responseMasterToken.getBody());
                if(jsonObject.getString("code").equals("200")) {
                    JSONObject dataObject = (JSONObject) jsonObject.get("data");
                    accessToken = dataObject.getString("accessToken");
    
                    long t = (long) dataObject.get("expireTime");
                    Timestamp timestamp = new java.sql.Timestamp(t);
                    String tokenExpireAt = dateFormat.format(timestamp);
                    LOGGER.info(String.format("마스터 액세스 토큰 발급 완료 : %s (만료일시 %s)", accessToken, tokenExpireAt));
    
                    masterAccount.setAccessToken(accessToken);
                    masterAccount.setTokenExpireAt(tokenExpireAt);
                    masterAccount.setAppKey(IPCRemoteRequestService.APP_KEY);
                    masterAccount.setAppSecret(IPCRemoteRequestService.APP_SECRET);
                    masterAccount.setUpdatedAt(dateFormat.format(new Date()));
                    masterAccount.setAreaDomain(dataObject.getString("areaDomain"));
                    masterAccount = administrateRepository.save(masterAccount);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return accessToken;
    }

}
