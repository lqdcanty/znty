package com.efida.esearch.utils;

import org.apache.commons.codec.digest.DigestUtils;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * appkey appsecret 生成验证工具
 */
public class SecretTools {

    /**
     * 生成APPkey
     * 默认32位的UUID
     * @return
     */
    public static String  generateAppKey(){

        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid;
    }
    /**
     * 根据应用标识和应用key生成 secret 用于应用校验
     * @param appId
     * @param appKey
     * @return
     */
    public static String generateAppSecret(@NotNull String appId,@NotNull String appKey){
        //不要常规化，并且加盐
        return DigestUtils.md5Hex( "appKey="+appKey+";sys=esearch;appId="+appId);
    }


    /**
     * 验证app应用密文是否正确
     * @param appId
     * @param appKey
     * @param appSecrect
     * @return
     */
    public static Boolean validAppInfo(@NotNull String appId,@NotNull String appKey,@NotNull String appSecrect){
        String secret=generateAppSecret(appId,appKey);
        if(secret.equals(appSecrect)){
            return true;
        }
        return false;
    }

}
