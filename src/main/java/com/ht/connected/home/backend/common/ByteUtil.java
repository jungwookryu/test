/*
 *******************************************************************************
 * Copyright (c) 2013 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.ht.connected.home.backend.common;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Formatter;
import java.util.Objects;

import org.hibernate.hql.internal.ast.util.LiteralProcessor.DecimalLiteralFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class for various byte related functions.
 *
 * @author Dan Noguerol
 */
public class ByteUtil {
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static Logger logger = LoggerFactory.getLogger(ByteUtil.class);
    static public String createString(byte[] bytes, int length) {
        return createString(bytes, 0, length);
    }

    static public String createString(byte[] bytes, int startIndex, int length) {
        char[] hexChars = new char[length * 5];
        int v;
        for (int j=startIndex; j < length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 5] = '0';
            hexChars[j * 5 + 1] = 'x';
            hexChars[j * 5 + 2] = hexArray[v >>> 4];
            hexChars[j * 5 + 3] = hexArray[v & 0x0F];
            hexChars[j * 5 + 4] = ' ';
        }
        return new String(hexChars);
    }
    static public String createString(byte b) {
        char[] hexChars = new char[4];
        int v = b & 0xFF;
        hexChars[0] = '0';
        hexChars[1] = 'x';
        hexChars[2] = hexArray[v >>> 4];
        hexChars[3] = hexArray[v & 0x0F];
        return new String(hexChars);
    }

    static public int convertTwoBytesToInt(byte msb, byte lsb) {
        return (msb << 8) | (lsb);
    }

    static public double parseValue(byte[] b, int start, int length, int precision) {
        long value = 0;
        for (int i=start; i < start+length; i++) {
            int shift = 8 * ((length - (i - start)) - 1);
            value += (b[i] & 0xFF) << shift;
        }
        return new BigDecimal(value).movePointLeft(precision).doubleValue();
    }
    static public int convertByteToInt(byte msb) {
        return msb ;
    }
    
    /**
     * 호스트에 전달하는 토픽에 Node ID와 Endpoint ID는 헥사값
     * 
     * @param number
     * @return
     */
    static public String getHexString(Integer number) {
        return "0x" + String.format("%2s", Integer.toHexString(number)).replace(' ', '0');
    }
    
    /**
     * 호스트에 전달하는 토픽에 Node ID와 Endpoint ID는 헥사값
     * 
     * @param number
     * @return
     */
    static public int getStringtoInt(String snumber) {
        int iRtn = 9999;
        try {
            iRtn = Short.decode(snumber);
        }catch (Exception e) {
            logger.error("not accept topic : snumber::: "+snumber+"::::"+e.getMessage());
        }
        return iRtn;
    }
}
