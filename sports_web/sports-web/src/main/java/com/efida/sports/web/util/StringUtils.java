/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.util;

/**
 * 
 * @author zhiyang
 * @version $Id: StringUtils.java, v 0.1 2018年5月29日 下午3:28:54 zhiyang Exp $
 */
public class StringUtils {

    /**
     * 截断字符串
     * 
     * @param str
     * @param maxLen
     * @return
     */
    public static String trimMaxStr(String str, int maxLen) {

        if (str == null) {
            return null;
        }

        if (str.length() <= maxLen) {
            return str;
        }

        return str.substring(0, maxLen);

    }
}
