package com.ht.connected.home.backend.category.zwave.constants.commandclass;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Multilevel Sensor Command Class
 *
 * @author ijlee
 */
public class MultilevelSensorCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final byte SENSOR_MULTILEVEL_GET = 0x04;
    private static final byte SENSOR_MULTILEVEL_REPORT = 0x05;

    public static final byte ID = 0x31;
    public static final String genericKey = "21";
    private Type type;
    private Scale scale;
    private List<Double> values = new ArrayList<>();

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_SENSOR_MULTILEVEL";
    }

    public Type getType() {
        return type;
    }

    public Scale getScale() {
        return scale;
    }

    public List<Double> getValues() {
        return values;
    }

    private void setTypeAndScale(byte t, int s) {
        switch (t) {
            case 0x00:
                type = Type.Reserved;
                scale = Scale.Reserved;
                break;
            case 0x01:
                type = Type.AirTemperature;
                switch (s) {
                    case 0x00:
                        scale = Scale.Celsius;
                        break;
                    case 0x01:
                        scale = Scale.Fahrenheit;
                        break;
                    default:
                        scale = Scale.Reserved;
                        break;
                }
                break;
            case 0x02:
                type = Type.GeneralPurpose;
                switch (s) {
                    case 0x00:
                        scale = Scale.PercentageValue;
                        break;
                    case 0x01:
                        scale = Scale.DimensionlessValue;
                        break;
                    default:
                        scale = Scale.Reserved;
                        break;
                }
                break;
            case 0x03:
                type = Type.Luminance;
                switch (s) {
                    case 0x00:
                        scale = Scale.PercentageValue;
                        break;
                    case 0x01:
                        scale = Scale.Lux;
                        break;
                    default:
                        scale = Scale.Reserved;
                        break;
                }
                break;
            case 0x04:
                type = Type.Power;
                switch (s) {
                    case 0x00:
                        scale = Scale.Watt;
                        break;
                    case 0x01:
                        scale = Scale.BtuPerHour;
                        break;
                    default:
                        scale = Scale.Reserved;
                        break;
                }
                break;
            case 0x05:
                type = Type.Humidity;
                switch (s) {
                    case 0x00:
                        scale = Scale.PercentageValue;
                        break;
                    case 0x01:
                        scale = Scale.AbsoluteHumidity;
                        break;
                    default:
                        scale = Scale.Reserved;
                        break;
                }
                break;
        }
    }

    public enum Type {
        AirTemperature,
        GeneralPurpose,
        Humidity,
        Luminance,
        Power,
        Reserved
    }

    public enum Scale {
        AbsoluteHumidity,
        BtuPerHour,
        Celsius,
        DimensionlessValue,
        Fahrenheit,
        Lux,
        PercentageValue,
        Reserved,
        Watt
    }

    @Override
    public String toString() {
        return "MultilevelSensorCommandClass{" +
                "version=" + getVersion() +
                ", type=" + type +
                ", scale=" + scale +
                ", values=" + values +
                '}';
    }

    @Override
    public String getDeviceType() {
        // TODO Auto-generated method stub
        return "HC6";
    }

    @Override
    public String getNicknameType() {
        // TODO Auto-generated method stub
        return "Multilevel Sensor";
    }

    @Override
    public String getFunctionType() {
        // TODO Auto-generated method stub
        return "SENSOR_MULTILEVEL";
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
