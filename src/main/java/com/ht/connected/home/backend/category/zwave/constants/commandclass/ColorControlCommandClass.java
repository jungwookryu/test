package com.ht.connected.home.backend.category.zwave.constants.commandclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColorControlCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final byte ID = (byte)0x33;

    public static final byte CAPABILITY_GET = 0x00;
    public static final byte CAPABILITY_REPORT = 0x01;
    public static final byte START_CAPABILITY_LEVEL_CHANGE = 0x02;
    public static final byte STATE_GET = 0x03;
    public static final byte STATE_REPORT = 0x04;
    public static final byte STATE_SET = 0x05;
    public static final byte STOP_STATE_CHANGE = 0x06;

    public static final byte CAPABILITY_ID_WARM_WHITE = 0x00;
    public static final byte CAPABILITY_ID_COLD_WHITE = 0x01;
    public static final byte CAPABILITY_ID_RED = 0x02;
    public static final byte CAPABILITY_ID_GREEN = 0x03;
    public static final byte CAPABILITY_ID_BLUE = 0x04;

    public static final String functionCode ="33";
    
    private Byte capabilityId;
    private Byte value;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_COLOR_CONTROL";
    }

    public Byte getCapabilityId() {
        return capabilityId;
    }

    public Byte getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ColorControlCommandClass{" +
                "version=" + getVersion() +
                ", capabilityId=" + capabilityId +
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
