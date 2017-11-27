package com.distribution.data.collector.server;

import com.distribution.data.collector.data.TcpModbusResponse;

/**
 * Created by baling.fang 于 2017年04月17日.
 */
public class Dlt645Utils {

    public static int parserCode(byte[] bytes) {
        return Integer.parseInt(toReverseHexString(bytes, 5, 1));// 5个字节，从0-4。
    }

    public static void minus33h(byte[] data) {
        int len = data[13];
        for (int i = 14; i < 14 + len; i++) {
            data[i] = (byte)(data[i] - 51);
        }
    }

    public static void parserAddr(byte[] data, TcpModbusResponse res) {
        Long l = Long.parseLong(toReverseHexString(data, 5, 6));// 6个字节长，从0-5。
        res.setLongcode(l);
    }

    public static void parserData(byte[] data, TcpModbusResponse res) {
        String key = toReverseHexString(data, 14, 4);// 4个字节长，从0-3。
        int len = data[13] - 4;
        byte[] d = new byte[len];
        System.arraycopy(data, 18, d, 0, len);
        res.getResponse().put(key, d);
    }

    public static String toReverseHexString(byte[] bytes, int start, int len) {
        StringBuffer sb = new StringBuffer();
        for (int i = len - 1; i >= 0; i--) {
            String s = Integer.toHexString(bytes[start + i] & 0xff);
            if (s.length() < 2)
                sb.append('0');
            sb.append(s);
        }
        return sb.toString();
    }
}
