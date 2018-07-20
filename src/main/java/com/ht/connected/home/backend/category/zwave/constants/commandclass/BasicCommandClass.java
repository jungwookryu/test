/*
 *******************************************************************************
 * Copyright (c) 2013 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.ht.connected.home.backend.category.zwave.constants.commandclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic Command Class
 *
 * @author Dan Noguerol
 */
public class BasicCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final byte BASIC_SET = 0x01;
    public static final byte BASIC_GET = 0x02;
    public static final byte BASIC_REPORT = 0x03;

    public static final int INT_BASIC_SET = 0x01;
    public static final int INT_BASIC_GET = 0x02;
    public static final int INT_BASIC_REPORT = 0x03;
    
    public static final byte ID = (byte)0x20;
    public static final int INT_ID = (byte)0x20;
    public static final String functionCode ="20";

    private Byte value;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_BASIC";
    }

    public Byte getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "BasicCommandClass{" +
                "version=" + getVersion() +
                ", value=" + value +
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
        return "";
    }
    
    @Override
    public String getFunctionCode() {
        return functionCode;
    }
}
