package com.ht.connected.home.backend.update.service;

import static java.util.Objects.isNull;

import java.beans.Introspector;
import java.time.LocalDateTime;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.update.model.entity.UPDeviceVersion;
import com.ht.connected.home.backend.update.model.entity.UPDownloadLog;
import com.ht.connected.home.backend.update.repository.UPDeviceVersionRepository;
import com.ht.connected.home.backend.update.repository.UPDownloadLogRepository;

/**
 * SIP MQTT 메시지 Subscirber 메세지의 Method 필드를 참고하여 수신된 메세지를 처리할 메소드를 동적으로 호출한다
 * 
 * @author 구정화
 *
 */
@Service
public class UPMqttSubscribeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UPMqttSubscribeService.class);

    @Autowired
    private UPDeviceVersionRepository upDeviceVersionRepository;

    @Autowired
    private UPDownloadLogRepository upDownloadLogRepository;

    public void version(JSONObject request) {
        UPDeviceVersion deviceVersion = upDeviceVersionRepository.findBySerialNo(request.optString("serial_no"));
        if (isNull(deviceVersion)) {
            deviceVersion = new UPDeviceVersion();
        }
        deviceVersion.setModelName(request.optString("model"));
        deviceVersion.setSerialNo(request.optString("serial_no"));
        deviceVersion.setVersionOS(request.optString("os_ver"));
        deviceVersion.setVersionAPI(request.optString("api_ver"));
        deviceVersion.setVersionAPP(request.optString("app_ver"));
        deviceVersion.setVersionFW(request.optString("fw_ver"));
        deviceVersion.setUpdatedAt(LocalDateTime.now().toString());
        upDeviceVersionRepository.save(deviceVersion);
    }

    public void fail_md5(JSONObject request) {
        UPDownloadLog downloadLog = new UPDownloadLog();
        downloadLog.setCreatedAt(LocalDateTime.now().toString());
        downloadLog.setModelName(request.optString("model_name"));
        downloadLog.setSerialNo(request.optString("serial_no"));
        downloadLog.setSuccess("N");
        upDownloadLogRepository.save(downloadLog);
    }

    public void update_ready(JSONObject request) {
        UPDownloadLog downloadLog = new UPDownloadLog();
        downloadLog.setCreatedAt(LocalDateTime.now().toString());
        downloadLog.setModelName(request.optString("model_name"));
        downloadLog.setSerialNo(request.optString("serial_no"));
        downloadLog.setSuccess("Y");
        upDownloadLogRepository.save(downloadLog);
    }

    public void subscribe(Message<?> message) {
        String topic = String.valueOf(message.getHeaders().get("mqtt_topic"));
        String payload = String.valueOf(message.getPayload());
        LOGGER.info("messageArrived-Update-Message: Topic=" + topic + ", Payload=" + payload);

        try {
            JSONObject request = new JSONObject();
            if (payload.length() > 0 && !payload.toLowerCase().equals("null")) {
                request = new JSONObject(payload);
            }
            String[] topicSegments = topic.split("/");
            String method = Introspector.decapitalize(topicSegments[6]);
            request.put("model_name", topicSegments[4]);
            request.put("serial_no", topicSegments[5]);
            UPMqttSubscribeService.class.getMethod(method, JSONObject.class).invoke(this, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
