package com.efida.esearch.secret;

import com.efida.esearch.test.BaseTest;
import com.efida.esearch.utils.SecretTools;

public class secretTools extends BaseTest {

    public static void main(String[] args) {
        String appKey = SecretTools.generateAppKey();
        String secret = SecretTools.generateAppSecret("sof", appKey);
        System.err.println(appKey);
        System.err.println(secret);
    }
}