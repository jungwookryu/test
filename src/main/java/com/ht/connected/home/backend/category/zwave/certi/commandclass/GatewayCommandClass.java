package com.ht.connected.home.backend.category.zwave.certi.commandclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GatewayCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final byte GATEWAY_MODE_GET = 0x01;
    public static final byte GATEWAY_MODE_REPORT = 0x02;
    public static final byte GATEWAY_MODE_SET = 0x03;
    public static final byte GATEWAY_LOCK_SET = 0x07;
    public static final byte GATEWAY_UNSOL_DEST_GET = 0x05;
    public static final byte GATEWAY_UNSOL_DEST_REPORT = 0x06;
    public static final byte GATEWAY_UNSOL_DEST_SET = 0x04;

    public static final byte ID = (byte)0x70;
    public static final int INT_ID = 0x70;
    
    public static final String genericKey = "02";
    public static final String specificKey = "07";
    public static final String functionCode ="70";
    
    private byte type;
    private byte level;

    // 255 open, 0 close
    private Boolean isOpen;
    
    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_GATEWAY";
    }

    public boolean getIsOpen() {
        return isOpen;
    }
    
    public byte getType() {
        return type;
    }

    public byte getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "GatewayCommandClass{" +
                "version=" + getVersion() +
                ", type=" + type +
                ", level=" + level +
                '}';
    }

    @Override
    public String getDeviceType() {
        return "HOST";
    }

    @Override
    public String getNicknameType() {
        return "gateway";
    }

    @Override
    public String getFunctionType() {
        return "GATEWAY";
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
