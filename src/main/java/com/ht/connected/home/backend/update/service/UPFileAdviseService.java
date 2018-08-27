package com.ht.connected.home.backend.update.service;

import static java.util.Objects.isNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.update.model.dto.UPSerialNumbersDto;
import com.ht.connected.home.backend.update.model.entity.UPDeviceVersion;
import com.ht.connected.home.backend.update.model.entity.UPFileVersion;

/**
 * 기기 현재 버전에 따른 업데이트 파일 결정 로직
 * 
 * @author 구정화
 *
 */
@Service
public class UPFileAdviseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UPFileAdviseService.class);

    private static final String fileServerHttp = "http://iot.dev.htiotservice.com:8090";

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
    public HashMap<String, String> getUpdateType(UPFileVersion fileVersion, UPDeviceVersion deviceVersion) {
        HashMap<String, String> updateType = new HashMap<>();
        LOGGER.info(String.format("FILE VERSION (%s), DEVICE OS VERSION (%s)", fileVersion.getVersionOS(),
                deviceVersion.getVersionOS()));
        LOGGER.info(String.format("FILE VERSION (%s), DEVICE API VERSION (%s)", fileVersion.getVersionAPI(),
                deviceVersion.getVersionAPI()));
        LOGGER.info(String.format("FILE VERSION (%s), DEVICE APP VERSION (%s)", fileVersion.getVersionAPP(),
                deviceVersion.getVersionAPP()));
        if (isOldDeviceVersion(fileVersion.getVersionOS(), deviceVersion.getVersionOS())) {
            updateType.put("update", UPFileVersion.UPDATE_TYPE_OS);
            updateType.put("version", fileVersion.getVersionOS());
        } else if (isOldDeviceVersion(fileVersion.getVersionAPI(), deviceVersion.getVersionAPI())) {
            updateType.put("update", UPFileVersion.UPDATE_TYPE_API);
            updateType.put("version", fileVersion.getVersionOS());
        } else if (isOldDeviceVersion(fileVersion.getVersionAPP(), deviceVersion.getVersionAPP())) {
            updateType.put("update", UPFileVersion.UPDATE_TYPE_APP);
            updateType.put("version", fileVersion.getVersionOS());
        }
        return updateType;
    }

    /**
     * MQTT 메세지 생성
     * 
     * @param fileVersion
     * @param deviceVersion
     * @param updateType
     * @param fileURL
     * @param md5
     * @return
     */
    public String getUpdateNotifyPayload(UPFileVersion fileVersion, UPDeviceVersion deviceVersion,
            HashMap<String, String> updateType, String fileURL, String md5) {
        String payload = null;
        if (md5.length() > 10) {
            fileVersion.setMd5(md5);
            fileVersion.setUrl(fileURL);
            if (fileVersion.getForce().equals("Y")) {
                fileVersion.setForce("true");
            } else if (!fileVersion.getForce().equals("true")) {
                fileVersion.setForce("false");
            }
            fileVersion.setUpdateType(updateType.get("update"));
            try {
                JSONObject jo = new JSONObject();
                jo.put("update_type", fileVersion.getUpdateType());
                jo.put("url", fileVersion.getUrl());
                jo.put("md5", fileVersion.getMd5());
                jo.put("force", fileVersion.getForce());
                payload = jo.toString();
            } catch (JSONException e) {
                LOGGER.warn("JSONObject put method arguments are not matched", e);
            }
        } else {
            LOGGER.error(String.format("Remote file is not valid or not exists : %s", fileURL));
        }

        return payload;
    }

    /**
     * 업데이트 파일 웹 경로
     * 
     * @param deviceType
     * @param updateType
     * @return
     */
    public String getRemoteFileURL(String deviceType, HashMap<String, String> updateType) {
        String url = String.format("%s/%s/%s/update_%s.tar.gz", fileServerHttp, deviceType, updateType.get("version"),
                updateType.get("update"));
        return url;
    }

    /**
     * 업데이트 파일의 MD5 해쉬
     * 
     * @param filename
     * @return
     */
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

    /**
     * 업데이트 파일의 MD5 해쉬
     * 
     * @param filename
     * @return
     */
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

    /**
     * 구간 지정 또는 단일 시리얼번호를 배열로 반환
     * 
     * @param request
     * @return
     */
    public List<String> getSerialNumbers(UPFileVersion request) {
        List<String> serialNumbers = new ArrayList<>();
        if (request.getSerialType().equals(UPFileVersion.SPECIFIC_SERIAL_SINGLE)) {
            if (!isNull(request.getSerialSingle()) && request.getSerialSingle().length() > 0) {
                serialNumbers.add(request.getSerialSingle());
            }
        } else if (request.getSerialType().equals(UPFileVersion.SPECIFIC_SERIAL_MULTIPLE)) {
            UPSerialNumbersDto from = new UPSerialNumbersDto(request.getSerialFrom());
            UPSerialNumbersDto to = new UPSerialNumbersDto(request.getSerialTo());
            UPSerialNumbersDto step = new UPSerialNumbersDto(request.getSerialFrom());

            if (from.getModelNumber().equals(to.getModelNumber())) {
                List<String> days = getDaysBetweenDates(from, to);
                for (String day : days) {
                    Integer y = Integer.valueOf(day.substring(2, 4));
                    Integer m = Integer.valueOf(day.substring(5, 7));                    
                    step.setProductionYear(y);
                    step.setProductionMonth(m);
                    Integer labelStart = 0;
                    Integer labelEnd = UPSerialNumbersDto.MAX_LABEL_NUMBER_MONTH;
                    if (from.getProductionMonth().equals(m) && from.getProductionYear().equals(y)) {
                        labelStart = step.getLabelNumber();
                    }
                    if (to.getProductionMonth().equals(m) && to.getProductionYear().equals(y)) {
                        labelEnd = to.getLabelNumber();
                    }
                    for (Integer i = labelStart; i <= labelEnd; i++) {
                        step.setLabelNumber(i);
                        serialNumbers.add(getSerialNumberFromDto(step));
                    }
                }
                return serialNumbers;
            } else {
                LOGGER.warn("Multiple serial number model numbers are not matched");
            }
        }
        return serialNumbers;
    }

    /**
     * 날짜 구간을 반환
     * 
     * @param from
     * @param to
     * @return
     */
    private List<String> getDaysBetweenDates(UPSerialNumbersDto from, UPSerialNumbersDto to) {
        String startDate = String.format("20%02d-%02d-%s", from.getProductionYear(), from.getProductionMonth(), "01");
        String endDate = String.format("20%02d-%02d-%s", to.getProductionYear(), to.getProductionMonth(), "01");
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<String> totalDates = new ArrayList<>();
        while (!start.isAfter(end)) {
            totalDates.add(start.toString());
            start = start.plusMonths(1);
        }
        return totalDates;
    }

    /**
     * DTO 객체로 시리얼 번호 생성
     * 
     * @param serialDto
     * @return
     */
    private String getSerialNumberFromDto(UPSerialNumbersDto serialDto) {
        String labelNumber = Integer.toString(serialDto.getLabelNumber(), 16);
        String modelNumber = serialDto.getModelNumber().toString();
        while (modelNumber.length() < 2)
            modelNumber = "0" + modelNumber;
        while (labelNumber.length() < 4)
            labelNumber = "0" + labelNumber;
        return String.format("%s%d%s%s", modelNumber, serialDto.getProductionYear(),
                Integer.toString(serialDto.getProductionMonth(), 16), labelNumber).toUpperCase();
    }

}
