package com.tjrac.wbcost.utils;

import com.tjrac.wbcost.configs.Const;

import java.security.MessageDigest;

/**
 * @author myd
 */
public class MD5Utils {


    /**
     * MD5加密
     *
     * @param origin      字符
     * @param charsetname 编码
     * @return MD5String
     */
    public static String md5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (null == charsetname || "".equals(charsetname)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
        } catch (Exception ignored) {
        }
        return resultString;
    }


    public static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte value : b) {
            resultSb.append(byteToHexString(value));
        }
        return resultSb.toString();
    }

    public static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return Const.HEX_DIGITS[d1] + Const.HEX_DIGITS[d2];
    }

}
