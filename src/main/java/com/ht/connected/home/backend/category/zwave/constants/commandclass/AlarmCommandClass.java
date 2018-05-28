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

public class AlarmCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final byte ALARM_GET = 0x04;
    public static final byte ALARM_REPORT = 0x05;

    public static final byte ID = (byte)0x71;

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
}
