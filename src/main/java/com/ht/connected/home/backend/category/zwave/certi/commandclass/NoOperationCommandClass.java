/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.ht.connected.home.backend.category.zwave.certi.commandclass;

public class NoOperationCommandClass extends CommandClass {
    public static final byte ID = 0x00;
    public static final String functionCode ="00";
    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_NO_OPERATION";
    }

    @Override
    public String toString() {
        return "NoOperationCommandClass{" +
                "version=" + getVersion() +
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
