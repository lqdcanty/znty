/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sport.facade.model;

import java.io.Serializable;
import java.util.Date;

import com.efida.sport.facade.enums.PlatformEnum;

/**
 * 
 * @author zoutao
 * @version $Id: registerModel.java, v 0.1 2018年8月5日 下午1:41:12 zoutao Exp $
 */
public class RegisterModel implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;
    /**
     * 用户唯一编号
     */
    private String            registerCode;
    /**
     * 登录账号(手机号)
     */
    private String            account;
    /**
     * 密码
     */
    private String            password;
    /**
     * 昵称
     */
    private String            nickName;
    /**
     * 头像
     */
    private String            headimgUrl;
    /**
     * 性别
     */
    private String            gender;
    /**
     * 转义后的性别
     */
    private String            genderStr;

    /**
     * 省份
     */
    private String            province;
    /**
     * 城市
     */
    private String            city;
    /**
     * 国家
     */
    private String            country;
    /**
     * 注册来源
     */
    private String            regTerminal;
    /**
     * 转义后注册来源
     */
    private String            regTerminalStr;

    private Date              regTime;

    /**
     * 平台来源
     * @see PlatformEnum
     */
    private String            platfrom;
    /**
     * 转以后的平台来源
     */
    private String            platfromStr;

    /**
     * 最近登录时间
     */
    private Date              lastLoginTime;
    /**
     * 真实姓名
     */
    private String            realName;

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadimgUrl() {
        return headimgUrl;
    }

    public void setHeadimgUrl(String headimgUrl) {
        this.headimgUrl = headimgUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGenderStr() {
        return genderStr;
    }

    public void setGenderStr(String genderStr) {
        this.genderStr = genderStr;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegTerminal() {
        return regTerminal;
    }

    public void setRegTerminal(String regTerminal) {
        this.regTerminal = regTerminal;
    }

    public String getRegTerminalStr() {
        return regTerminalStr;
    }

    public void setRegTerminalStr(String regTerminalStr) {
        this.regTerminalStr = regTerminalStr;
    }

    public String getPlatfrom() {
        return platfrom;
    }

    public void setPlatfrom(String platfrom) {
        this.platfrom = platfrom;
    }

    public String getPlatfromStr() {
        return platfromStr;
    }

    public void setPlatfromStr(String platfromStr) {
        this.platfromStr = platfromStr;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

}
