/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.util.weichat;

import com.efida.sports.util.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author zoutao
 * @version $Id: WeichatUtil.java, v 0.1 2018年2月8日 上午11:33:21 zoutao Exp $
 */
public class WeichatUtil {
    private static Logger log = LoggerFactory.getLogger(WeichatUtil.class);

    /** 
     * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。 
     * @return boolean 
     */
    public static boolean isTenpaySign(String characterEncoding,
                                       SortedMap<Object, Object> packageParams, String API_KEY) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);

        //算出摘要  
        String mysign = MD5Utils.MD5Encode(sb.toString()).toLowerCase();
        //        String mysign = MD5Util.MD5Encode(sb.toString(), characterEncoding).;
        String tenpaySign = ((String) packageParams.get("sign")).toLowerCase();

        //System.out.println(tenpaySign + "    " + mysign);  
        return tenpaySign.equals(mysign);
    }

    /**
     * 获取微信签名
     * @param map 请求参数集合
     * @return 微信请求签名串
     */
    public static String getSign(SortedMap<String, Object> map, String apiKey) {
        StringBuffer sb = new StringBuffer();
        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            //参数中sign、key不参与签名加密
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + apiKey);
        log.info("sbString--------------------"+sb.toString());
        String sign = MD5Utils.MD5Encode(sb.toString()).toUpperCase();
        log.info("sign----"+sign);
        return sign;
    }

    public static String createSha1Sign(SortedMap<Object, Object> map) {
        StringBuffer sb = new StringBuffer();
        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        Boolean isFirst = true;
        while (iterator.hasNext()) {
            if (!isFirst) {
                sb.append("&");
            }
            isFirst = false;
            Map.Entry entry = (Map.Entry) iterator.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            //参数中sign、key不参与签名加密
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v);
            }
        }
        String sign = MD5Utils.Sha1Encode(sb.toString());
        log.info("生成签名前的字符串：" + sb.toString() + " ,签名后结果：" + sign);
        return sign;
    }

    /**
     * 将请求参数转换为xml格式的string 
     * @param parameters
     * @return
     */
    public static String getRequestXml(SortedMap<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)
                || "sign".equalsIgnoreCase(k) || "scene_info".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    public static String createNoncestr(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < length; i++) {
            Random rd = new Random();
            res += chars.indexOf(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    public static String createNoncestr() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < 16; i++) {
            Random rd = new Random();
            res += chars.charAt(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    /** 
     * 取出一个指定长度大小的随机正整数. 
     *  
     * @param length 
     *            int 设定所取出随机数的长度。length小于11 
     * @return int 返回生成的随机数。 
     */
    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    /** 
     * 获取当前时间 yyyyMMddHHmmss 
     *  
     * @return String 
     */
    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }
}
