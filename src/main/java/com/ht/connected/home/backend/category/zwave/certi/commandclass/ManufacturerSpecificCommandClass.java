package com.ht.connected.home.backend.category.zwave.certi.commandclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManufacturerSpecificCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final byte MANUFACTURER_SPECIFIC_GET = 0x04;
    private static final byte MANUFACTURER_SPECIFIC_REPORT = 0x05;

    public static final byte ID = 0x72;
    public static final String functionCode ="72";
    
    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_MANUFACTURER_SPECIFIC";
    }

    @Override
    public String toString() {
        return "ManufacturerSpecificCommandClass{" +
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
