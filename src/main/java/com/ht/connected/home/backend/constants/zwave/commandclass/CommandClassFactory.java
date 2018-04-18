/*
 *******************************************************************************
 * Copyright (c) 2013 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.ht.connected.home.backend.constants.zwave.commandclass;

/**
 * Convenience factory class that creates CommandClass instances from a command class ID byte.
 *
 * @author Dan Noguerol
 */
public class CommandClassFactory {
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
            case NoOperationCommandClass.ID:
                return new NoOperationCommandClass();
            case VersionCommandClass.ID:
                return new VersionCommandClass();
            case WakeUpCommandClass.ID:
                return new WakeUpCommandClass();
            default:
                return null;
        }
    }
}
