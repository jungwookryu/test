package com.ht.connected.home.backend.common;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
//import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

//import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.config.service.EmailConfig;
import com.ht.connected.home.backend.model.dto.ZwaveRequest;
import com.ht.connected.home.backend.model.entity.Gateway;
import com.ht.connected.home.backend.model.entity.User;

/**
 * @author ijlee
 *         <p>
 *         create by 2017.06.21
 */
public class ZWaveCommon {

    private static Logger logger = LoggerFactory.getLogger(ZWaveCommon.class);

    /**
     * mqtt publish 토픽 생성
     * @param topicLeadingPath
     * @return
     */
    static String getMqttPublishTopic(ZwaveRequest zwaveRequest, String target, String model, String serial) {
        String topic = "";
        int nodeId = 0;
        int endPointId = 0;
        if (!Objects.isNull(zwaveRequest.getNodeId())) {
            nodeId = zwaveRequest.getNodeId();
        }
        if (!Objects.isNull(zwaveRequest.getEndpointId())) {
            endPointId = zwaveRequest.getEndpointId();
        }
        String[] segments = new String[] { "/server", target, serial, "zwave", "certi",
                ByteUtil.getHexString(zwaveRequest.getClassKey()), ByteUtil.getHexString(zwaveRequest.getCommandKey()), zwaveRequest.getVersion(),
                ByteUtil.getHexString(nodeId), ByteUtil.getHexString(endPointId),
                zwaveRequest.getSecurityOption() };
        topic = String.join("/", segments);
        logger.info("====================== ZWAVE PROTO MQTT PUBLISH TOPIC ======================");
        logger.info(topic);
        return topic;
    }

}
