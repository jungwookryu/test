package com.ht.connected.home.backend.device.category.zwave.certi;

import java.io.IOException;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ht.connected.home.backend.device.category.zwave.ZWaveRequest;
import com.ht.connected.home.backend.device.category.zwave.certi.commandclass.BinarySwitchCommandClass;
import com.ht.connected.home.backend.device.category.zwave.notification.ZwaveCertiNotificationService;

@Service
public class ZWaveCertiBinarySwitchServiceImpl implements ZWaveCertiBinarySwitchService {

    private static final Log logging = LogFactory.getLog(ZWaveCertiBinarySwitchServiceImpl.class);

    @Autowired
    ZwaveCertiNotificationService zWaveCertiNotificationService;

    @Override
	@Transactional
    public void subscribe(ZWaveRequest zwaveRequest, String payload) throws JsonParseException, JsonMappingException, IOException, Exception {

        // binary switch report
        if (zwaveRequest.getCommandKey() == BinarySwitchCommandClass.INT_SWITCH_BINARY_REPORT) {
            zWaveCertiNotificationService.subscribe(zwaveRequest, payload);
        }

    }

}
