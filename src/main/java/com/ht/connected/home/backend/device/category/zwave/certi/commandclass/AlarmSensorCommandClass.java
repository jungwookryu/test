/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.ht.connected.home.backend.device.category.zwave.certi.commandclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Alarm Sensor Command Class.
 *
 * @author Dan Noguerol
 */
public class AlarmSensorCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final byte SENSOR_ALARM_GET = 0x01;
    public static final byte SENSOR_ALARM_REPORT = 0x02;

    public static final byte ID = (byte)0x9C;
    public static final String genericKey = "A1";
    public static final String functionCode ="9C";
    
    private Type type;
    private byte level;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_SENSOR_ALARM";
    }

    public Type getType() {
        return type;
    }

    public byte getLevel() {
        return level;
    }

    public enum Type {
        GENERAL,
        SMOKE,
        CARBON_MONOXIDE,
        CARBON_DIOXIDE,
        HEAT,
        FLOOD;

        public static Type convert(byte b) {
            return Type.values()[b];
        }
    }

    @Override
    public String toString() {
        return "AlarmSensorCommandClass{" +
                "version=" + getVersion() +
                ", type=" + type +
                ", level=" + level +
                '}';
    }

    @Override
    public String getDeviceType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getNicknameType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFunctionType() {
        // TODO Auto-generated method stub
        return null;
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
