/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.efida.sports.constants;

/**
 * 常量
 * 
 * @author zoutao
 * @version $Id: Constants.java, v 0.1 2017年5月5日 下午1:27:12 zoutao Exp $
 */
public class Constants {

    /**
     * 返回json相关
     */
    public static final String RESULTCODE              = "resultCode";

    public static final String RESULTMSG               = "resultMsg";

    public static final String RESULT                  = "result";
    /**
     * 操作成功
     */
    public static final String SUCCESS                 = "200";

    /**
     * 未找到
     */
    public static final String NON_EXISTENT            = "404";

    /**
     * 操作失败
     */
    public static final String FAIL                    = "500";

    /**
     * 超过有效时间
     */
    public static final String OVERTIME                = "408";

    /**
     * 微信token
     */
    public static final String USER_TOKEN_KEY          = "sport_front_user_token";
    /**
     * 验证码Key
     */
    public static final String VERIFY_CODE_KEY         = "verify_code_key_";
    /**
    * 验证码状态
    */
    public static final String VERIFY_CODE_STATUS_KEY  = "verify_code_status_key_";

    /**
    * 验证码状态
    */
    public static final String MESSAGE_VERIFY_CODE     = "message_verify_code_";

    /**
     * 咕咚赛事信息对应集合
     */
    public static final String GUDONG_MATCH_CODE       = "gudong_match_code";

    /**
     * 消息未读
     */
    public static final String UNREAD                  = "0";

    /**
     * 消息已读
     */
    public static final String READ                    = "1";

    /**
     * 通过
     */
    public static final String ADOPT                   = "0";

    /**
     * 不通过
     */
    public static final String NOT_PASS                = "1";

    /**
     * code码
     */
    public static final String CODE                    = "code";

    /**
     * 消息内容
     */
    public static final String MSG                     = "msg";

    /**
     * 图片验证码在session中对应的键
     */
    public static final String IMG_VERIFY_CODE         = "img_verify_code";

    /**
     * 用户验证码在session中对应的键
     */
    public static final String AUTH_TOKEN1             = "auth_token";

    /**
     * 用户认证重定向地址在session中对应的键
     */
    public static final String AUTH_REDIRECT_URL       = "auth_redirect_url";

    /**
     * 用户绑定手机后重定向地址在session中对应的键
     */
    public static final String BIND_PHONE_REDIRECT_URL = "bind_redirect_url";

    /**
     * 微信端获取token
     */
    public static final String ACCESS_TOKEN_KEY        = "access_token_key";

    /**
     * 微信端获取JSAPI_TICKET_KEY
     */
    public static final String JSAPI_TICKET_KEY        = "jsapi_ticket_key";
    /**
     * 第三方用户openIdkey
     */
    public static final String USER_OPEN_ID_KEY        = "user_open_id_";
    /**
     * 根据用户唯一编号缓存key
     */
    public static final String REGISTER_CODE_KEY       = "register_code_";

    /**
     * 根据用户电话号码缓存key
     */
    public static final String REGISTER_PHONE_KEY      = "register_phone_";

    /**
     * 报名过期时间   单位分钟
     */
    public static final int    APPLY_TIME_OUT          = 10;
    /**
    * app 登录redis中的key
    */
    public static final String APP_TOKEN_KEY           = "app_auto_token_";

    /**
    * PC 登录redis中的key
    */
    public static final String COOKIE_TOKEN_KEY        = "cookie_auto_token_";

    /**
     * pc端cookie中的值
     */
    public static final String COOKIE_AUTH_TOKEN       = "cookie_auth_token";

    /**
     * 用户认证重定向地址在session中对应的键
     */
    public static final String REGISTER_REDIRECT_URL   = "register_redirect_url";

    public static final int    COOKIE_TIME_OUT         = 60 * 60 * 24 * 365;

    public static final String MESSAGE_TREU            = "true";

    public static final String ZTYD_PC_ACCESS_KEY      = "ztyd_pc_access_key_";

    public static final String ZTYD_PC_ACCESS_IP_KEY   = "ztyd_pc_access_ip_key_";

}
