/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service;

import java.util.Date;

/**
 * 
 * @author zoutao
 * @version $Id: SMSService.java, v 0.1 2018年5月24日 下午3:40:22 zoutao Exp $
 */
public interface MsgService {

    /**
     * 清理 手机号发验证码后等待50s限制。
     * 比如：刚注册完毕，立马进行报名验证。只在注册后清理该状态，其他地方不要用该方法。
     *
     * @param mobile
     * @return
     */
    boolean clearVerifyStatus(String mobile);

    /**
     * 发送验证码
     * 
     * @param mobile
     * @return
     */
    boolean sendVerifyCode(String mobile);

    /**
    * 校验验证码
    * 
    * @param mobile
    * @param code
    * @return
    */
    boolean checkVerifyCode(String mobile, String code);

    /**
     * 发送成功报名消息
     * 
     * @param mobile 手机号
     * @param itemName 项目名称
     * @param startTime 报名开始时间
     * @param address 报名地址
     * @param endTime 报名结束时间
     * @param partCode 蓝天绿野验证code
     * @return
     */
    boolean sendSuccessMessage(String matchName, String groupName, String mobile, String itemName,
                               Date startTime, String address, Date endTime, String partCode);

    /**
     * 发送通知消息
     * 
     * @param mobile
     * @param itemName
     * @param startTime
     * @param address
     * @param partCode 蓝天绿野验证code
     * @return
     */
    boolean sendNoticeMessage(String matchName, String groupName, String mobile, String itemName,
                              Date startTime, String address, String partCode);

    /**
     * 报名中发送通知消息
     * 
     * @param mobile
     * @param itemName
     * @param startTime
     * @param address
     * @param partCode 蓝天绿野验证code
     * @return
     */
    boolean flourlyMessage(String matchName, String groupName, String mobile, String itemName,
                           Date startTime, String address, Date endTime, String partCode);

}
