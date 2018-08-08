package com.ht.connected.home.backend.update.service;

import static java.util.Objects.isNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.update.model.entity.UPDeviceVersion;
import com.ht.connected.home.backend.update.model.entity.UPFileVersion;

@Service
public class UPFileAdviseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UPFileAdviseService.class);

    private static final String fileServerHttp = "http://iot.dev.htiotservice.com:8090";

    @Autowired
    private ObjectMapper objectMapper;

    private boolean isOldDeviceVersion(String fileVersion, String deviceVersion) {
        boolean oldVersion = true;
        if (Integer.valueOf(fileVersion) <= Integer.valueOf(deviceVersion)) {
            oldVersion = false;
        }
        return oldVersion;
    }

    /**
     * 파일 버전관리 정책에 의한 분기처리
     * 
     * @param fileVersion
     * @param deviceVersion
     * @return
     */
    public String getUpdateType(UPFileVersion fileVersion, UPDeviceVersion deviceVersion) {
        LOGGER.info(String.format("FILE VERSION (%s), DEVICE OS VERSION (%s)", fileVersion.getVersionOS(),
                deviceVersion.getVersionOS()));
        LOGGER.info(String.format("FILE VERSION (%s), DEVICE API VERSION (%s)", fileVersion.getVersionAPI(),
                deviceVersion.getVersionAPI()));
        LOGGER.info(String.format("FILE VERSION (%s), DEVICE APP VERSION (%s)", fileVersion.getVersionAPP(),
                deviceVersion.getVersionAPP()));
        String updateType = null;
        if (isOldDeviceVersion(fileVersion.getVersionOS(), deviceVersion.getVersionOS())) {
            updateType = UPFileVersion.UPDATE_TYPE_OS;
        } else if (isOldDeviceVersion(fileVersion.getVersionAPI(), deviceVersion.getVersionAPI())) {
            updateType = UPFileVersion.UPDATE_TYPE_API;
        } else if (isOldDeviceVersion(fileVersion.getVersionAPP(), deviceVersion.getVersionAPP())) {
            updateType = UPFileVersion.UPDATE_TYPE_APP;
        }
        return updateType;
    }

    public String getUpdateNotifyPayload(UPFileVersion fileVersion, UPDeviceVersion deviceVersion, String updateType,
            String fileURL, String md5) {
        String payload = null;
        if (md5.length() > 10) {
            fileVersion.setMd5(md5);
            fileVersion.setUrl(fileURL);
            if (fileVersion.getForce().equals("Y")) {
                fileVersion.setForce("true");
            } else if(!fileVersion.getForce().equals("true")){
                fileVersion.setForce("false");
            }
            fileVersion.setUpdateType(updateType);
            try {
                JSONObject jo = new JSONObject();
                jo.put("update_type", fileVersion.getUpdateType());
                jo.put("url", fileVersion.getUrl());
                jo.put("md5", fileVersion.getMd5());
                jo.put("force", fileVersion.getForce());                
                payload = jo.toString();
            } catch (JSONException e) {
                LOGGER.warn("error message", e);
            }  
        } else {
            LOGGER.error(String.format("Remote file is not valid or not exists : %s", fileURL));
        }

        return payload;
    }

    public String getRemoteFileURL(UPFileVersion request, UPDeviceVersion device) {
        // String url = "http://class.brandstock.me/README.md";
        String url = String.format("%s/%s/%s/update_%s.tar.gz", fileServerHttp, request.getDeviceType(),
                request.getVersion(), getUpdateType(request, device));
        return url;
    }

    private static MessageDigest createChecksum(String filename) {
        MessageDigest complete = null;
        try {
            URL url = new URL(filename);
            InputStream fis = url.openStream();
            byte[] buffer = new byte[1024];
            complete = MessageDigest.getInstance("MD5");
            int numRead;
            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            fis.close();
        } catch (FileNotFoundException e) {
            LOGGER.info(String.format("File %s is not exists", filename));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return complete;
    }

    public static String getMD5Checksum(String filename) {
        String result = "";
        MessageDigest messageDigest = createChecksum(filename);
        if (!isNull(messageDigest)) {
            byte[] b = messageDigest.digest();
            if (!isNull(b)) {
                for (int i = 0; i < b.length; i++) {
                    result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
                }
            }
        }
        return result;
    }
}
