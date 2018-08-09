package com.ht.connected.home.backend.device.category.zwave.certi.commandclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserCodeCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final byte USER_CODE_SET = 0x01;
    public static final byte USER_CODE_GET = 0x02;
    public static final byte USER_CODE_REPORT = 0x03;
    public static final byte USER_NUMBER_GET = 0x04;
    public static final byte USER_NUMBER_REPORT = 0x05;

    public static final byte ID = (byte)0x63;
    public static final int INT_ID = (byte)0x63;

    public static final int INT_USER_CODE_SET = (byte)0x01;
    public static final int INT_USER_CODE_GET = (byte)0x02;
    public static final int INT_USER_CODE_REPORT = (byte)0x03;
    public static final int INT_USER_NUMBER_GET = (byte)0x04;
    public static final int INT_USER_NUMBER_REPORT = (byte)0x05;
    
    public static final String genericKey = "00";
    public static final String specificKey = "00";
    public static final String functionCode ="63";
    
    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_USER_CODE";
    }

    @Override
    public String toString() {
        return "usercodeCommandClass{" +
                "version=" + getVersion() +
                '}';
    }

    @Override
    public String getDeviceType() {
        return "";
    }

    @Override
    public String getNicknameType() {
        return "usercode";
    }

    @Override
    public String getFunctionType() {
        return "usercode";
    }

    @Override
    public String getGenericKey() {
        return genericKey;
    }

    @Override
    public String getFunctionCode() {
        return functionCode;
    }
}
