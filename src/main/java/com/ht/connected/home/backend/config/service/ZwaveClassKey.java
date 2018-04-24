package com.ht.connected.home.backend.config.service;

import com.ht.connected.home.backend.common.ByteUtil;

public class ZwaveClassKey {

    //0x52
	public static byte NETWORK_MANAGEMENT_PROXY = 0x52;
	public static byte NETWORK_MANAGEMENT_BASIC = 0x4D;
	public static int NETWORK_MANAGEMENT_INCLUSION = 0x34;
	public static int NETWORK_MANAGEMENT_PRIMARY = 0x54;
	public static int ZWAVE_PLUS_INFO = 0x5E;
	public static int MANUFACTURER_SPECIFIC = 0x72;
	public static int APPLICATION_STATUS = 0x22;
	public static int MULTI_CHANNEL = 0x60;
	public static int MULTI_CHANNEL_ASSOCIATION = 0x8E;
	public static int DOOR_LOCK = 0x62;
	public static int SECURITY = 0x98;
	public static int SECURITY_2 = 0x9F;
	public static byte BASIC = 0x20;
	public static int USER_CODE = 0x63;
	public static int THERMOSTAT_MODE = 0x40;
	public static int THERMOSTAT_SET_POINT = 0x43;
	public static int THERMOSTAT_SETBACK = 0x47;
	public static int NOTIFICATION = 0x71;
	public static int CRC_16_ENCAPSULATION = 0x56;
	public static int METER = 0x32;
	public static int SENSOR_MULTILEVEL = 0x31;
	public static int ASSOCIATION = 0x85;
	public static int ASSOCIATION_GRP_INFO = 0x59;
	public static int BATTERY = 0x80;
	public static int SWITCH_BINARY = 0x25;
	public static int DEVICE_RESET_LOCALLY = 0x5A;
	public static int SWITCH_MULTILEVEL = 0x26;
	public static int POWER_LEVEL = 0x73;
	public static int VERSION = 0x86;
	public static int WAKE_UP = 0x84;
	
	
	public int getParseInt(int byteInt) {
	    return Integer.parseInt(ByteUtil.getHexString(byteInt));
	}


}
