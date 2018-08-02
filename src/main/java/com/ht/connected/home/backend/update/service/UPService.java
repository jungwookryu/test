package com.ht.connected.home.backend.update.service;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;
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

    public boolean isLoggedIn(HttpSession session) {
        return !isNull(session.getAttribute("username"));
    }

    @Async
    public Integer addVersion(UPFileVersion request) {
        UPFileVersion fileVersion = upFileVersionRepository.findByModelName(request.getModelName());
        if (isNull(fileVersion)) {
            fileVersion = request;
            fileVersion.setVersionOS(request.getVersion());
            fileVersion.setVersionAPI(request.getVersion());
            fileVersion.setVersionAPP(request.getVersion());
        }
        fileVersion.setVersion(request.getVersion());
        fileVersion.setDeviceType(request.getDeviceType());
        fileVersion.setForce(request.getForce());
        fileVersion.setUpdatedAt(LocalDateTime.now().toString());
        if (request.getUpdateType().equals(UPFileVersion.UPDATE_TYPE_OS)) {
            fileVersion.setVersionOS(request.getVersion());
        } else if (request.getUpdateType().equals(UPFileVersion.UPDATE_TYPE_API)) {
            fileVersion.setVersionAPI(request.getVersion());
        } else if (request.getUpdateType().equals(UPFileVersion.UPDATE_TYPE_APP)) {
            fileVersion.setVersionAPP(request.getVersion());
        }
        fileVersion = upFileVersionRepository.save(fileVersion);
        if (!isNull(fileVersion.getSeq())) {
            HashMap<String, String> fileMD5 = new HashMap<>();
            List<UPDeviceVersion> deviceVersions = upDeviceVersionRepository.getByModelName(request.getModelName());
            for (UPDeviceVersion deviceVersion : deviceVersions) {
                LOGGER.info(String.format("Compare version device (%s)", deviceVersion.getSerialNo()));
                String updateType = upFileAdviseService.getUpdateType(fileVersion, deviceVersion);
                if (!isNull(updateType)) {
                    String md5 = null;
                    String fileURL = upFileAdviseService.getRemoteFileURL(fileVersion, deviceVersion);
                    String md5TmpKey = updateType + fileVersion.getVersion();
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
                                String.format("UPDATE FILE MD5 IS NULL FOR DEVICE(%s)", deviceVersion.getSerialNo()));
                    }
                } else {
                    LOGGER.info(String.format(
                            "UPDATE TYPE IS NULL SINCE REGISTERED VERSION IS OLDER THEN DEVICE(%s) VERSION",
                            deviceVersion.getSerialNo()));
                }
            }
        }
        return request.getSeq();
    }

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
