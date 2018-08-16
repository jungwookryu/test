package com.ht.connected.home.backend.service.push;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

/**
 * FCM 푸시 발송 기능
 * 
 * @author 구정화
 *
 */
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
        } else {
            LOGGER.error("======= FCM PUSH NOTIFICATION REQUEST SUCCESS =======");
            LOGGER.info(response.getBody());
        }
        return response;
    }

    /**
     * 유니코드 문자열을 포함하는 메세지 또는 그외 발송시 사용 가능
     * 
     * @param message
     * @param fireBaseServerKey
     */
    public void send(String message, String fireBaseServerKey) {
        try {
            URL url = new URL(FIREBASE_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=" + fireBaseServerKey);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(message.getBytes("UTF-8"));
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            LOGGER.info("FCM :: Sending POST request to URL : " + url);
            LOGGER.info("FCM :: Post parameters : " + message);
            LOGGER.info("FCM :: Response code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            LOGGER.info("FCM :: Response");
            LOGGER.info("FCM :: Response data - " + response.toString());
        } catch (IOException e) {
            LOGGER.error("Sending FCM error : ", e);
        }
    }

}
