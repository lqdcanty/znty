/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.efida.sports.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author lijiaqi
 * @desc 
 * @version $Id: RedisUtil.java, v 0.1 2016年11月9日 下午5:52:25 lijiaqi Exp $
 */
public class RedisUtil {

    private static final String charset = "UTF-8";

    public static byte[] convert(String val) {
        try {
            return val.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 序列号对象
     * 
     * @param object
     * @return
     * @throws IOException 
     */
    public static byte[] serialize(Object object) throws IOException {
        ObjectOutputStream objectOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        byteArrayOutputStream = new ByteArrayOutputStream();
        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;

    }

    /**
     * 反序列化对象 
     * 
     * @param bytes
     * @return
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    public static Object deSeialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayOutputStream = null;
        byteArrayOutputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayOutputStream);
        return objectInputStream.readObject();
    }

    /**
     * 
     * @title
     * @methodName
     * @author lijiaqi
     * @description
     * @param app
     * @param bizName
     * @param id
     * @return
     */
    public static String getUniqueKey(String app, String bizName, String id) {

        return String.format("%s_%s_%s", app, bizName, id);
    }
}
