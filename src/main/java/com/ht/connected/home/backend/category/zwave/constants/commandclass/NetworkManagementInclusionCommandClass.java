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
 * NetworkManagementInclusion Command Class
 *
 * @author ijlee
 */
public class NetworkManagementInclusionCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final byte NODE_ADD = 0x01;
    public static final byte NODE_ADD_STATUS = 0x02;
    public static final byte NODE_REMOVE = 0x03;
    public static final byte NODE_REMOVE_STATUS = 0x04;
    public static final byte NODE_STOP = 0x05;
    public static final byte FAILED_NODE_STATUS = 0x08;
    public static final byte FAILED_NODE_REPLACE = 0x09;
    
    public static final int INT_NODE_ADD = 0x01;
    public static final int INT_NODE_ADD_STATUS = 0x02; 
    public static final int INT_NODE_REMOVE = 0x03;
    public static final int INT_NODE_REMOVE_STATUS = 0x04;
    public static final int INT_NODE_STOP = 0x05;
    public static final int INT_FAILED_NODE_STATUS = 0x08;
    public static final int INT_FAILED_NODE_REPLACE = 0x09;
    
    
    public static final byte ID = (byte)0x34;
    public static final int INT_ID = (byte)0x34;
    public static final String functionCode ="34";
    
    private Byte value;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_NETWORK_MANAGEMENT_INCLUSION";
    }

    public Byte getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "NetworkManagementCommandClass{" +
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
        return functionCode;
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
