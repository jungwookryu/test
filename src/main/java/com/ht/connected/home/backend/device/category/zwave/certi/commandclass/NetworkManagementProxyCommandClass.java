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
 * NetworkManagementProxyCommandClass
 *
 * @author ijlee
 */
public class NetworkManagementProxyCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final byte NODE_LIST_GET = 0x01;
    public static final byte NODE_LIST_REPORT = 0x02;

    public static final int INT_NODE_LIST_GET = 0x01;
    public static final int INT_NODE_LIST_REPORT = 0x02;
    
    public static final byte ID = (byte)0x52;
    public static final int INT_ID = (byte)0x52;
    public static final String functionCode ="52";
    
    private Byte value;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_NETWORK_MANAGEMENT_PROXY";
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
