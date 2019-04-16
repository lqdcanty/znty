/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.util;

/**
 * 
 * @author zoutao
 * @version $Id: StringReplaceUtil.java, v 0.1 2018年4月14日 下午5:27:43 zoutao Exp $
 */
public class StringReplaceUtil {

    /**
     * 根据用户名的不同长度，来进行替换 ，达到保密效果
     *
     * @param str 用户名
     * @return 替换后的用户名
     */
    public static String strReplaceWithStar(String str) {
        String strAfterReplaced = "";

        if (str == null) {
            str = "";
        }

        int nameLength = str.length();

        if (nameLength <= 1) {
            strAfterReplaced = "*";
        } else if (nameLength == 2) {
            strAfterReplaced = replaceAction(str, "(?<=\\d{0})\\d(?=\\d{1})");
        } else if (nameLength >= 3 && nameLength <= 6) {
            strAfterReplaced = replaceAction(str, "(?<=\\d{1})\\d(?=\\d{1})");
        } else if (nameLength == 7) {
            strAfterReplaced = replaceAction(str, "(?<=\\d{1})\\d(?=\\d{2})");
        } else if (nameLength == 8) {
            strAfterReplaced = replaceAction(str, "(?<=\\d{2})\\d(?=\\d{2})");
        } else if (nameLength == 9) {
            strAfterReplaced = replaceAction(str, "(?<=\\d{2})\\d(?=\\d{3})");
        } else if (nameLength == 10) {
            strAfterReplaced = replaceAction(str, "(?<=\\d{3})\\d(?=\\d{3})");
        } else if (nameLength >= 11) {
            strAfterReplaced = replaceAction(str, "(?<=\\d{3})\\d(?=\\d{4})");
        }

        return strAfterReplaced;

    }

    /**
     * 实际替换动作
     *
     * @param str str
     * @param regular  正则
     * @return
     */
    private static String replaceAction(String str, String regular) {
        return str.replaceAll(regular, "*");
    }

    /**
     * 身份证号替换，保留前四位和后四位
     *
     * 如果身份证号为空 或者 null ,返回null ；否则，返回替换后的字符串；
     *
     * @param idCard 身份证号
     * @return
     */
    public static String idCardReplaceWithStar(String idCard) {

        if (idCard.isEmpty() || idCard == null) {
            return null;
        } else {
            return replaceAction(idCard, "(?<=\\d{4})\\d(?=\\d{4})");
        }
    }

    /**
     * 银行卡替换，保留后四位
     *
     * 如果银行卡号为空 或者 null ,返回null ；否则，返回替换后的字符串；
     *
     * @param bankCard 银行卡号
     * @return
     */
    public static String bankCardReplaceWithStar(String bankCard) {

        if (bankCard.isEmpty() || bankCard == null) {
            return null;
        } else {
            return replaceAction(bankCard, "(?<=\\d{0})\\d(?=\\d{4})");
        }
    }

}
