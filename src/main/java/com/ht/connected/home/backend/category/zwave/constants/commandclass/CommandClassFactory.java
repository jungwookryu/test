/*
 *******************************************************************************
 * Copyright (c) 2013 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.ht.connected.home.backend.category.zwave.constants.commandclass;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

import com.ht.connected.home.backend.category.zwave.endpoint.Endpoint;
import com.ht.connected.home.backend.common.ByteUtil;

/**
 * Convenience factory class that creates CommandClass instances from a command class ID byte.
 *
 * @author Dan Noguerol
 */
public class CommandClassFactory {

    @Autowired
    Properties zWaveProperties;
    
    public static CommandClass createCommandClass(byte commandClassId) {
        switch (commandClassId) {
            case AlarmCommandClass.ID:
                return new AlarmCommandClass();
            case AlarmSensorCommandClass.ID:
                return new AlarmSensorCommandClass();
            case BasicCommandClass.ID:
                return new BasicCommandClass();
            case BatteryCommandClass.ID:
                return new BatteryCommandClass();
            case BinarySensorCommandClass.ID:
                return new BinarySensorCommandClass();
            case BinarySwitchCommandClass.ID:
                return new BinarySwitchCommandClass();
            case ColorControlCommandClass.ID:
                return new ColorControlCommandClass();
            case ManufacturerSpecificCommandClass.ID:
                return new ManufacturerSpecificCommandClass();
            case MeterCommandClass.ID:
                return new MeterCommandClass();
            case MultiInstanceCommandClass.ID:
                return new MultiInstanceCommandClass();
            case MultilevelSensorCommandClass.ID:
                return new MultilevelSensorCommandClass();
            case MultilevelSwitchCommandClass.ID:
                return new MultilevelSwitchCommandClass();
            case NetworkManagementBasicCommandClass.ID:
                return new NetworkManagementBasicCommandClass();
            case NetworkManagementInclusionCommandClass.ID:
                return new NetworkManagementInclusionCommandClass();
            case NetworkManagementProxyCommandClass.ID:
                return new NetworkManagementProxyCommandClass();
            case VersionCommandClass.ID:
                return new VersionCommandClass();
            case WakeUpCommandClass.ID:
                return new WakeUpCommandClass();
            case NoOperationCommandClass.ID:
                return new NoOperationCommandClass();
            default:
                return null;
        }
        
    }
    public static CommandClass createSCmdClass(Endpoint endpoint) {
        
        if(endpoint.getGeneric().equals(AlarmCommandClass.genericKey)){
            if(endpoint.getCmdCls().contains(ByteUtil.getHexString2((int) AlarmCommandClass.ID))) {
                return new AlarmCommandClass();
            }
        }
        else if(endpoint.getGeneric().equals(AlarmSensorCommandClass.genericKey)){
            if(endpoint.getCmdCls().contains(ByteUtil.getHexString2((int) AlarmSensorCommandClass.ID))) {
                return new AlarmSensorCommandClass();
            }
        }
        else if(endpoint.getGeneric().equals(BinarySensorCommandClass.genericKey)){
            if(endpoint.getCmdCls().contains(ByteUtil.getHexString2((int) BinarySensorCommandClass.ID))) {
                return new BinarySensorCommandClass();
            }
        }
        else if(endpoint.getGeneric().equals(BinarySwitchCommandClass.genericKey)){
            if(endpoint.getCmdCls().contains(ByteUtil.getHexString2((int) BinarySwitchCommandClass.ID))) {
                return new BinarySwitchCommandClass();
            }
        }
        else if(endpoint.getGeneric().equals(DoorLockCommandClass.genericKey)){
            if(endpoint.getCmdCls().contains(ByteUtil.getHexString2((int) DoorLockCommandClass.ID))) {
                return new DoorLockCommandClass();
            }
        }
        else if(endpoint.getGeneric().equals(MeterCommandClass.genericKey)){
            if(endpoint.getCmdCls().contains(ByteUtil.getHexString2((int) MeterCommandClass.ID))) {
                return new MeterCommandClass();
            }
        }
        else if(endpoint.getGeneric().equals(MultilevelSensorCommandClass.genericKey)){
            if(endpoint.getCmdCls().contains(ByteUtil.getHexString2((int) MultilevelSensorCommandClass.ID))) {
                return new MultilevelSensorCommandClass();
            }
        }
        else if(endpoint.getGeneric().equals(MultilevelSwitchCommandClass.genericKey)){
            if(endpoint.getCmdCls().contains(ByteUtil.getHexString2((int) MultilevelSwitchCommandClass.ID))) {
                return new MultilevelSwitchCommandClass();
            }
        }
        else if(endpoint.getGeneric().equals(GatewayCommandClass.genericKey)&&endpoint.getSpecific().equals(GatewayCommandClass.specificKey)){
            return new GatewayCommandClass();
        }
        else if(endpoint.getCmdCls().contains(ByteUtil.getHexString((int) NoOperationCommandClass.ID))) {
                return new NoOperationCommandClass();
        }
         else {
             return new NoOperationCommandClass();
        }
        return null;
    }
}

