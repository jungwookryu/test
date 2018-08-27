package com.ht.connected.home.backend.update.service;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.client.user.User;
import com.ht.connected.home.backend.client.user.UserRepository;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.device.category.gateway.Gateway;
import com.ht.connected.home.backend.device.category.gateway.GatewayRepository;
import com.ht.connected.home.backend.update.model.entity.UPDeviceVersion;
import com.ht.connected.home.backend.update.model.entity.UPFileVersion;
import com.ht.connected.home.backend.update.repository.UPDeviceVersionRepository;
import com.ht.connected.home.backend.update.repository.UPFileVersionRepository;


/**
 * 업데이트 기능 지원
 * 
 * @author 구정화
 *
 */
@Service
public class UPService {

    private static final Log LOGGER = LogFactory.getLog(UPService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UPFileVersionRepository upFileVersionRepository;

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private UPMqttPublishService upMqttPublishService;

    @Autowired
    private UPDeviceVersionRepository upDeviceVersionRepository;

    @Autowired
    private UPFileAdviseService upFileAdviseService;

    /**
     * 로그인 처리
     * 
     * @param body
     * @param session
     * @return
     */
    public boolean login(Map<String, String> body, HttpSession session) {
        boolean isUserFound = false;
        List<User> users = userRepository.findByUserEmail(body.get("user_email"));
        if (users.size() > 0) {
            String password = Common.encryptHash(MessageDigestAlgorithms.SHA_256, body.get("user_pwd"));
            for (User user : users) {
                if (user.getPassword().equals(password) && user.getAuthority().equals("ROLE_ADMIN")) {
                    session.setAttribute("username", user.getUserEmail());
                    isUserFound = true;
                    break;
                }
            }
        }
        return isUserFound;
    }

    /**
     * 로그인 여부
     * 
     * @param session
     * @return
     */
    public boolean isLoggedIn(HttpSession session) {
        return !isNull(session.getAttribute("username"));
    }

    /**
     * 업데이트 파일 등록 알림 MQTT 
     * 
     * @param request
     * @return
     */
    @Async
    public Integer addVersion(UPFileVersion request) {
        UPFileVersion fileVersion = upFileVersionRepository.findByModelName(request.getModelName());
        if (isNull(fileVersion)) {
            fileVersion = request;
        }
        fileVersion.setDeviceType(request.getDeviceType());
        fileVersion.setForce(request.getForce());
        fileVersion.setUpdatedAt(LocalDateTime.now().toString());
        fileVersion.setVersionOS(request.getVersionOS());
        fileVersion.setVersionAPI(request.getVersionAPI());
        fileVersion.setVersionAPP(request.getVersionAPP());

        fileVersion = upFileVersionRepository.save(fileVersion);
        if (!isNull(fileVersion.getSeq())) {
            HashMap<String, String> fileMD5 = new HashMap<>();
            List<UPDeviceVersion> deviceVersions = upDeviceVersionRepository.getByModelName(request.getModelName());
            List<String> serialNumbers = upFileAdviseService.getSerialNumbers(request);            
            for (UPDeviceVersion deviceVersion : deviceVersions) {
                if(serialNumbers.size() != 0 && !serialNumbers.contains(deviceVersion.getSerialNo())) {
                    continue;
                }
                LOGGER.info(String.format("Compare device(%s) version", deviceVersion.getSerialNo()));
                HashMap<String, String> updateType = upFileAdviseService.getUpdateType(fileVersion, deviceVersion);
                if (!isNull(updateType.get("update"))) {
                    String md5 = null;
                    String fileURL = upFileAdviseService.getRemoteFileURL(fileVersion.getDeviceType(), updateType);
                    String md5TmpKey = updateType + updateType.get("version");
                    if (fileMD5.containsKey(md5TmpKey)) {
                        md5 = fileMD5.get(md5TmpKey);
                    } else {
                        md5 = UPFileAdviseService.getMD5Checksum(fileURL);
                        fileMD5.put(md5TmpKey, md5);
                    }
                    if (!isNull(md5)) {
                        String payload = upFileAdviseService.getUpdateNotifyPayload(fileVersion, deviceVersion,
                                updateType, fileURL, md5);
                        if (!isNull(payload)) {
                            String topic = String.format("/server/device/update/%s/%s/get_file", request.getModelName(),
                                    deviceVersion.getSerialNo());
                            LOGGER.info("===== UPDATE INFORM TO DEVICE =====");
                            LOGGER.info(topic);
                            LOGGER.info(payload);
                            upMqttPublishService.publish(topic, payload);
                        }
                    } else {
                        LOGGER.info(
                                String.format("Update file md5 is null for device(%s)", deviceVersion.getSerialNo()));
                    }
                } else {
                    LOGGER.info(String.format(
                            "Update type is null since registered version is older then device(%s) version",
                            deviceVersion.getSerialNo()));
                }
            }
        }
        return request.getSeq();
    }

    /**
     * 앱에서 업데이트 명령
     * 
     * @param request
     */
    public void updateOwnDevice(UPDeviceVersion request) {
        Gateway gateway = gatewayRepository.findBySerial(request.getSerialNo());
        if (!isNull(gateway)) {
            if (gateway.getCreated_user_id().equals(request.getIotAccount())) {
                String topic = String.format("/server/device/update/%s/%s/update_now", gateway.getModel(),
                        gateway.getSerial());
                upMqttPublishService.publish(topic, "");
            }
        } else {
            LOGGER.debug(String.format("SERIAL NOT FOUND IN DB : %s", request.getSerialNo()));
        }
    }
}
