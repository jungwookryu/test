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
 * Multilevel Switch Command Class
 *
 * @author Dan Noguerol
 */
public class MultilevelSwitchCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final byte SWITCH_MULTILEVEL_SET = 0x01;
    private static final byte SWITCH_MULTILEVEL_GET = 0x02;
    private static final byte SWITCH_MULTILEVEL_REPORT = 0x03;

    public static final byte ID = 0x26;

    private Byte level;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_SWITCH_MULTILEVEL";
    }

    public Byte getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "MultilevelSwitchCommandClass{" +
                "version=" + getVersion() +
                ", level=" + level +
                '}';
    }

    @Override
    public String getDeviceType() {
        // TODO Auto-generated method stub
        return "HC9";
    }

    @Override
    public String getNicknameType() {
        // TODO Auto-generated method stub
        return "Multilevel switch";
    }

    @Override
    public String getFunctionType() {
        // TODO Auto-generated method stub
        return "SWITCH_MULTILEVEL";
    }
}
