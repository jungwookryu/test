/*******************************************************************************
 * Copyright (c) 2013 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.ht.connected.home.backend.category.zwave.constants.commandclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.connected.home.backend.category.zwave.notification.NotificationEvent;

/**
 * Binary Switch Command Class
 * @author Dan Noguerol
 */
public class BinarySwitchCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final byte SWITCH_BINARY_SET = 0x01;
    public static final byte SWITCH_BINARY_GET = 0x02;
    public static final byte SWITCH_BINARY_REPORT = 0x03;

    public static final byte ID = (byte) 0x25;
    public static final int INT_ID = (byte) 0x25;
    
    public static final int INT_SWITCH_BINARY_REPORT = 0x03;
    
    
    public static final String genericKey = "10";
    public static final String specificKey = "01";
    public static final String functionCode = "25";
    
    public static final int on = 255;
    public static final int off = 0;
    public static final int notSigned = -1;
    public static final int MEVENT =8;
    
    public static final int DEFAULT_SEQUENCE =0;
    
    private Boolean isOn;
    
    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_SWITCH_BINARY";
    }

    public static Boolean getIsOn(int iIsOn) {
        switch(iIsOn) {
            case on:
                return true;
            case off:
                return false;
            default:
                return false;
        }
    }
    
    public static int getNotificationCode(int iIsOn) {
        switch(iIsOn) {
        case on:
            return NotificationEvent.intOn;
        case off:
            return NotificationEvent.intOff;
        default:
            return -1;
        }
    }

    
    
    @Override
    public String toString() {
        return "BinarySwitchCommandClass{" +
                "version=" + getVersion() +
                ", isOn=" + isOn +
                '}';
    }

    @Override
    public String getDeviceType() {
        return "HC8";
    }

    @Override
    public String getNicknameType() {
        // TODO Auto-generated method stub
        return "On/Off Power Switch";
    }

    @Override
    public String getFunctionType() {
        return "SWITCH_BINARY";
    }

    @Override
    public String getGenericKey() {
        return genericKey;
    }

    @Override
    public String getFunctionCode() {
        // TODO Auto-generated method stub
        return null;
    }

}
