package com.ht.connected.home.backend.device.category.zwave.certi.commandclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Multilevel Switch Command Class
 *
 * @author ijlee
 */
public class MultilevelSwitchCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final byte SWITCH_MULTILEVEL_SET = 0x01;
    private static final byte SWITCH_MULTILEVEL_GET = 0x02;
    private static final byte SWITCH_MULTILEVEL_REPORT = 0x03;

    public static final byte ID = 0x26;
    
    public static final String genericKey = "11";
    public static final String functionCode ="26";
    
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
    
    @Override
    public String getGenericKey() {
        return genericKey;
    }

    @Override
    public String getFunctionCode() {
        // TODO Auto-generated method stub
        return null;
    }
}
