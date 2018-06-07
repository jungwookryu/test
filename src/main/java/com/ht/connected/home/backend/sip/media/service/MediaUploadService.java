package com.ht.connected.home.backend.sip.media.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.ht.connected.home.backend.sip.media.model.dto.SipMediaMqttRequestMessageDto;

@Service
public class MediaUploadService {
    
    private static final Log LOGGER = LogFactory.getLog(MediaUploadService.class);
    
//    private static final String fileThumbnailPath = "//mnt/media/record/thumbnail/";
//    private static final String fileVideoPath = "//mnt/media/record/video/";
    
    private static final String fileThumbnailPath = "/host/web/media/record/thumbnail/";
    private static final String fileVideoPath = "/host/web/media/record/video/";
    
    public boolean saveFile(SipMediaMqttRequestMessageDto request) throws IOException {
        String strDate = new SimpleDateFormat("YYYYMM").format(Calendar.getInstance().getTime());
        byte[] decoded = Base64.decodeBase64(request.getData());
        SipMediaMqttRequestMessageDto.FileInfoDto fileInfo = request.getFileInfo();
        if (fileInfo.getFileExt().equalsIgnoreCase("jpg")) {
            File file = new File(fileThumbnailPath + request.getSerialNumber() + "/" + strDate);
            String filename = file.toString() + "/" + request.getEventId() + ".jpg";
            if (!file.exists()) {
                file.mkdirs();
                LOGGER.info(String.format("Directory path [%s] created", file.toString()));
            }
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(decoded);
            fos.close();
        } else {
            File file = new File(fileVideoPath + request.getSerialNumber() + "/" + strDate);
            String filename = file.toString() + "/" + request.getEventId() + ".avi";
            if (!file.exists()) {
                file.mkdirs();
                LOGGER.info(String.format("Directory path [%s] created", file.toString()));
            }
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(decoded);
            fos.close();
        }
        return true;
    }
}
