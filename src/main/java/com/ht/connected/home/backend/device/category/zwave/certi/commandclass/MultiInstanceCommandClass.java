package com.ht.connected.home.backend.device.category.zwave.certi.commandclass;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.connected.home.backend.device.category.zwave.constants.node.ZWaveMultiChannelEndpoint;

/**
 * Multi Instance Command Class
 *
 * @author ijlee
 */
public class MultiInstanceCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final byte ID = 0x60;

    // version 1
    public static final byte MULTI_INSTANCE_GET = 0x04;
    public static final byte MULTI_INSTANCE_REPORT = 0x05;
    public static final byte MULTI_INSTANCE_CMD_ENCAP = 0x06;

    // version 2
    public static final byte MULTI_CHANNEL_END_POINT_GET = 0x07;
    public static final byte MULTI_CHANNEL_END_POINT_REPORT = 0x08;
    public static final byte MULTI_CHANNEL_CAPABILITY_GET = 0x09;
    public static final byte MULTI_CHANNEL_CAPABILITY_REPORT = 0x0A;
    public static final byte MULTI_CHANNEL_END_POINT_FIND = 0x0B;
    public static final byte MULTI_CHANNEL_END_POINT_FIND_REPORT = 0x0C;
    public static final byte MULTI_CHANNEL_CMD_ENCAP = 0x0D;

    public static final byte IDENTICAL_ENDPOINTS = 0x40;
    public static final String functionCode ="60";
    
    private Map<Byte, ZWaveMultiChannelEndpoint> endpointMap = new HashMap<Byte, ZWaveMultiChannelEndpoint>();
    private int instanceCount;
    private int endpointCount;
    private boolean endpointsIdentical;

    @Override
    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_MULTI_INSTANCE";
    }

    @Override
    public int getMaxSupportedVersion() {
        return 2;
    }

    /**
     * Returns the number of command class instances associated with this node (v1 only)
     *
     * @return the instance count
     */
    public int getInstanceCount() {
        return instanceCount;
    }

    /**
     * Returns all the endpoints associated with this node (v2 only).
     *
     * @return a Collection of Endpoint instances
     */
    public Collection<ZWaveMultiChannelEndpoint> getEndpoints() {
        return endpointMap.values();
    }

    /**
     * Returns a specific endpoint (v2 only).
     *
     * @param number the endpoint number
     *
     * @return an Endpoint instance (or null if not found)
     */
    public ZWaveMultiChannelEndpoint getEndpoint(byte number) {
        return endpointMap.get(number);
    }

    @Override
    public String toString() {
        return "MultiInstanceCommandClass{" +
                "version=" + getVersion() +
                ", endpointMap=" + endpointMap +
                ", instanceCount=" + instanceCount +
                ", endpointCount=" + endpointCount +
                ", endpointsIdentical=" + endpointsIdentical +
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
