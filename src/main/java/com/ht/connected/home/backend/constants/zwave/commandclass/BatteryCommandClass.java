/*******************************************************************************
 * Copyright (c) 2013 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.ht.connected.home.backend.constants.zwave.commandclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Battery Command Class
 *
 * @author Dan Noguerol
 */
public class BatteryCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final byte BATTERY_GET = 0x02;
    private static final byte BATTERY_REPORT = 0x03;

    public static final byte ID = (byte)0x80;

    private Byte level;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_BATTERY";
    }

    public Byte getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "BatteryCommandClass{" +
                "version=" + getVersion() +
                ", level=" + level +
                '}';
    }
}
