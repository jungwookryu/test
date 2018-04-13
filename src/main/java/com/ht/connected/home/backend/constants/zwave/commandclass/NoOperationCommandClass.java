/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.ht.connected.home.backend.constants.zwave.commandclass;

public class NoOperationCommandClass extends CommandClass {
    public static final byte ID = 0x00;

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
}