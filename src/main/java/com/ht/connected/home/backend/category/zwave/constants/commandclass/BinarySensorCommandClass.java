/*******************************************************************************
 * Copyright (c) 2013 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.ht.connected.home.backend.category.zwave.constants.commandclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binary Switch Command Class
 *
 * @author Dan Noguerol
 */
public class BinarySensorCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final byte SENSOR_BINARY_SET = 0x01;
    private static final byte SENSOR_BINARY_GET = 0x02;
    private static final byte SENSOR_BINARY_REPORT = 0x03;

    public static final byte ID = 0x30;

    public Boolean isIdle;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_SENSOR_BINARY";
    }

    public Boolean isIdle() {
        return isIdle;
    }

    @Override
    public String toString() {
        return "BinarySensorCommandClass{" +
                "version=" + getVersion() +
                ", isIdle=" + isIdle +
                '}';
    }
}
