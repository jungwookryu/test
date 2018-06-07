package com.ht.connected.home.backend.service.push;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FirebasePushClient {

    private static final Log LOGGER = LogFactory.getLog(FirebasePushClient.class);

    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

    /**
     * FCM 에 푸시 메세지 발송 요청
     * 
     * @param entity
     * @param fireBaseServerKey
     * @return
     */
    public ResponseEntity<String> send(HttpEntity<Object> entity, String fireBaseServerKey) {
        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + fireBaseServerKey));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        ResponseEntity<String> response = restTemplate.exchange(FIREBASE_API_URL, HttpMethod.POST, entity,
                String.class);

        HttpStatus.Series series = response.getStatusCode().series();
        if (HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series)) {
            String responseBody = response.getBody();
            LOGGER.error("======= FCM PUSH NOTIFICATION REQUEST FAILED =======");
            LOGGER.error(responseBody);
        }else {
            LOGGER.error("======= FCM PUSH NOTIFICATION REQUEST SUCCESS =======");
            LOGGER.info(response.getBody());
        }
        return response;
    }


}
