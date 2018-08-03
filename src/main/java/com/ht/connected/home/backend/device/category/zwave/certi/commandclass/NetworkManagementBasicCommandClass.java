/*
 *******************************************************************************
 * Copyright (c) 2013 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.ht.connected.home.backend.device.category.zwave.certi.commandclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NetworkManagementBasic Command Class
 *
 * @author ijlee
 */
public class NetworkManagementBasicCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final byte LEARN_MODE_SET = 0x02;
    public static final byte NETWORK_UPDATE_REQUEST = 0x03;
    public static final byte NETWORK_UPDATE_REQUEST_STATUS = 0x04;
    public static final byte DEFAULT_SET = 0x06;
    public static final byte DEFAULT_SET_COMPLETE = 0x07;
    
    public static final int INT_LEARN_MODE_SET = 0x02;
    public static final int INT_LEARN_MODE_SET_STATUS = 0x05;
    public static final int INT_NETWORK_UPDATE_REQUEST = 0x03;
    public static final int INT_NETWORK_UPDATE_REQUEST_STATUS = 0x04;
    public static final int INT_DEFAULT_SET = 0x06;
    public static final int INT_DEFAULT_SET_COMPLETE = 0x07;
    
    
    public static final byte ID = (byte)0x4D;
    public static final int INT_ID = (byte)0x4D;
    public static final String functionCode ="4D";
    
    private Byte value;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_NETWORK_MANAGEMENT_BASIC";
    }

    public Byte getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "COMMAND_CLASS_NETWORK_MANAGEMENT_BASIC{" +
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
