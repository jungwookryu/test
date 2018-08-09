package com.ht.connected.home.backend.device.category.zwave.certi.commandclass;

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
