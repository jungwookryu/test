
package com.ht.connected.home.backend.device.category.zwave.certi.commandclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoorLockCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

   public static final byte DOOR_LOCK_CONFIGURATION_GET = 0x05;
   public static final byte DOOR_LOCK_CONFIGURATION_REPORT = 0x06;
   public static final byte DOOR_LOCK_CONFIGURATION_SET = 0x04;
   public static final byte DOOR_LOCK_OPERATION_GET = 0x02;
   public static final byte DOOR_LOCK_OPERATION_REPORT = 0x03;
   public static final byte DOOR_LOCK_OPERATION_SET = 0x01;

    public static final byte ID = (byte)0x62;
    public static final String genericKey = "40";
    
    public static final String functionCode ="62";
    
    private Configration configration;
    private Operation operation;
    private Byte value;

    
    enum Configration{
        configration
    }
    
    enum Operation{
        operation
    }
    
    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_DOOR_LOCK";
    }

    public byte getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "AlarmCommandClass{" +
                "version=" + getVersion() +
                "configration=" + configration +
                "operation=" + operation +
                ", value=" + value +
                '}';
    }

    @Override
    public String getDeviceType() {
        return "HC4";
    }

    @Override
    public String getNicknameType() {
        return "Door Lock";
    }

    @Override
    public String getFunctionType() {
        return "DOOR_LOCK";
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
