package com.ht.connected.home.backend.category.zwave.certi.commandclass;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.connected.home.backend.common.ByteUtil;

/**
 * Meter command class
 *
 * @author ijlee
 */
public class MeterCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final byte ID = 0x32;

    private static final byte METER_GET = 0x01;
    private static final byte METER_REPORT = 0x02;
    public static final String genericKey = "31";
    public static final String functionCode ="32";

    Map<Scale, MeterReadingValue> value = new HashMap<>();

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_METER";
    }

    @Override
    public int getMaxSupportedVersion() {
        return 2;
    }

    public MeterReadingValue getLastValue(Scale s) {
        return value.get(s);
    }

    private void parseMeterReport(byte[] ccb, int startIndex, int version) {
        Type type;
        Scale scale = Scale.Reserved;
        Double currentValue;
        Double previousValue = null;
        Integer delta = null;

        // read meter type & scale
        int t;
        if (version > 1) {
            t = ccb[startIndex + 2] & 0x1F;
        } else {
            t = ccb[startIndex + 2];
        }
        int s = (ccb[startIndex + 3] >> 3) & 0x03;

        switch (t) {
            case 1:
                type = Type.Electric;
                switch (s) {
                    case 0:
                        scale = Scale.KilowattHours;
                        break;
                    case 1:
                        scale = Scale.KilovoltAmpereHours;
                        break;
                    case 2:
                        scale = Scale.Watts;
                        break;
                    case 3:
                        scale = Scale.PulseCount;
                        break;
                    default:
                        scale = Scale.Reserved;
                        break;
                }
                break;
            case 2:
                type = Type.Gas;
                switch (s) {
                    case 0:
                        scale = Scale.CubicMeters;
                        break;
                    case 1:
                        scale = Scale.CubicFeet;
                        break;
                    case 3:
                        scale = Scale.PulseCount;
                        break;
                    default:
                        scale = Scale.Reserved;
                        break;
                }
                break;
            case 3:
                type = Type.Water;
                switch (s) {
                    case 0:
                        scale = Scale.CubicMeters;
                        break;
                    case 1:
                        scale = Scale.CubicFeet;
                        break;
                    case 2:
                        scale = Scale.USGallons;
                        break;
                    case 3:
                        scale = Scale.PulseCount;
                        break;
                    default:
                        scale = Scale.Reserved;
                }
                break;
            default:
                logger.warn("Found unknown meter type: {}", t);
                type = Type.Unknown;
                break;
        }

        // read precision, scale and size
        int precision = (ccb[startIndex + 3] >> 5) & 0x07;
        int size = ccb[startIndex + 3] & 0x07;
        logger.trace("{} meter precision: {}, size: {}, scale: {}", type, precision, size, scale);

        // determine current value
        currentValue = ByteUtil.parseValue(ccb, startIndex + 4, size, precision);
        logger.trace("Current value is {}", currentValue);

        if (version == 2 && ccb.length >= (startIndex + 6 + size + size)) {
            // read previous value
            previousValue = ByteUtil.parseValue(ccb, startIndex + 6 + size, size, precision);
            // read delta
            delta = ((ccb[startIndex + size + 4] << 8) & 0xFF00) | (ccb[startIndex + size + 5] & 0xFF);
            logger.trace("Previous value was {} received {} seconds ago", previousValue, delta);
        }

        MeterReadingValue val = new MeterReadingValue(type, currentValue, previousValue, delta);
        value.put(scale, val);
    }

    private byte scaleToByte(Scale s) {
        if (s != null) {
            switch (s) {
                case KilowattHours:
                case CubicMeters:
                    return 0;
                case KilovoltAmpereHours:
                case CubicFeet:
                    return 1;
                case Watts:
                case USGallons:
                case Reserved:
                    return 2;
                case PulseCount:
                    return 3;
                default:
                    return 0;
            }
        } else {
            return 0;
        }
    }

    public enum Type {
        Unknown,
        Electric,
        Gas,
        Water
    }

    public enum Scale {
        KilowattHours,
        KilovoltAmpereHours,
        Watts,
        PulseCount,
        CubicMeters,
        CubicFeet,
        USGallons,
        Reserved
    }

    public class MeterReadingValue {

        private Type type;
        private Double currentValue;
        private Double previousValue;
        private Integer delta;

        public MeterReadingValue(Type type, Double currentValue, Double previousValue, Integer delta) {
            this.type = type;
            this.currentValue = currentValue;
            this.previousValue = previousValue;
            this.delta = delta;
        }

        public Type getType() {
            return type;
        }

        public Double getCurrentValue() {
            return currentValue;
        }

        public Double getPreviousValue() {
            return previousValue;
        }

        public Integer getDelta() {
            return delta;
        }

        @Override
        public String toString() {
            return "MeterReadingValue{" +
                    "type=" + type +
                    ", currentValue=" + currentValue +
                    ", previousValue=" + previousValue +
                    ", delta=" + delta +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MeterCommandClass{" +
                "values=" + value +
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
        return genericKey;
    }

    @Override
    public String getFunctionCode() {
        return functionCode;
    }

}
