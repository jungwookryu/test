package com.ht.connected.home.backend.update.service;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Integer addVersion(UPFileVersion request) {
        UPFileVersion fileVersion = upFileVersionRepository.findByModelName(request.getModelName());
        fileVersion.setVersion(request.getVersion());
        if (isNull(fileVersion)) {
            fileVersion = request;
        }
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
            List<UPDeviceVersion> deviceVersions = upDeviceVersionRepository.getByModelName(request.getModelName());
            for (UPDeviceVersion deviceVersion : deviceVersions) {
                String payload = upFileAdviseService.getUpdateNotifyPayload(fileVersion, deviceVersion);
                if (!isNull(payload)) {
                    upMqttPublishService.publish(String.format("/server/device/update/%s/%s/get_file",
                            request.getModelName(), deviceVersion.getSerialNo()), payload);
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
