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
 * Version Command Class
 *
 * @author Dan Noguerol
 */
public class VersionCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final byte ID = (byte)0x86;

    private static final byte VERSION_GET = 0x11;
    private static final byte VERSION_REPORT = 0x12;
    private static final byte VERSION_COMMAND_CLASS_GET = 0x13;
    private static final byte VERSION_COMMAND_CLASS_REPORT = 0x14;
    public static final String functionCode ="86";
    
    private String library;
    private String protocol;
    private String application;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_VERSION";
    }

    public String getLibrary() {
        return library;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getApplication() {
        return application;
    }

    @Override
    public String toString() {
        return "VersionCommandClass{" +
                "version=" + getVersion() +
                ", library='" + library + '\'' +
                ", protocol='" + protocol + '\'' +
                ", application='" + application + '\'' +
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
