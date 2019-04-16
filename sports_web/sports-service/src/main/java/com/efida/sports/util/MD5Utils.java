package com.efida.sports.util;

import java.security.MessageDigest;
import java.util.Formatter;

/**
 * 
 * @author zoutao
 * @version $Id: OperationType.java, v 0.1 2017年5月5日 下午2:01:22 zoutao Exp $
 */
public class MD5Utils {
    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                                                "A", "B", "C", "D", "E", "F" };

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin) {
        String result = null;
        try {
            result = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = byteArrayToHexString(md.digest(result.getBytes("UTF-8")));
        } catch (Exception exception) {
        }
        return result;
    }

    /**
     * 获取签名  微信获取用户地理位置时候调用
     * 
     * @param origin
     * @return
     */
    public static String Sha1Encode(String origin) {
        String result = null;
        try {
            result = new String(origin);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            result = byteToHex(md.digest(result.getBytes("UTF-8")));
        } catch (Exception exception) {
        }
        return result;
    }
}
