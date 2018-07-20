/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.ht.connected.home.backend.category.zwave.constants.commandclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * For door and window sensors, it is RECOMMENDED to use the Notification Type: “Access Control (0x06)“ with the events: “Door/Window Open” and ”Door/Window Closed” (respectively 0x16 and 0x17).
 * @author COM
 *
 */
public class AlarmCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final byte ALARM_GET = 0x04;
    public static final byte ALARM_REPORT = 0x05;
    public static final byte ALARM_SET = 0x06;

    public static final byte ID = (byte)0x71;
    public static final int INT_ID = (byte)0x71;
    public static final int INT_ALARM_REPORT = (byte)0x05;
    public static final int INT_ALARM_SET = (byte)0x06;
    
    public static final String genericKey = "07";
    public static final String specificKey ="01";
    public static final String functionCode ="71";
    
    private byte type;
    private byte level;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_SENSOR_ALARM";
    }

    public byte getType() {
        return type;
    }

    public byte getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "AlarmCommandClass{" +
                "version=" + getVersion() +
                ", type=" + type +
                ", level=" + level +
                '}';
    }

    @Override
    public String getDeviceType() {
        return "HC4";
    }

    @Override
    public String getNicknameType() {
        return "Notification Sensors";
    }

    @Override
    public String getFunctionType() {
        return "SENSOR_ALARM";
    }

    @Override
    public String getGenericKey() {
        return genericKey;
    }

    @Override
    public String getFunctionCode() {
        return functionCode;
    }
}
