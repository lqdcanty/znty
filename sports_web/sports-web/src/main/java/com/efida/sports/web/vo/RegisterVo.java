package com.efida.sports.web.vo;

import org.apache.commons.lang3.StringUtils;

import com.efida.easy.ucenter.facade.enums.GenderEnum;
import com.efida.easy.ucenter.facade.model.RegisterModel;

public class RegisterVo {
    /**
     * 登录账号
     */
    private String  account;
    /**
     * 昵称
     */
    private String  nickName;
    /**
     * 头像
     */
    private String  headimgUrl;
    /**
     * 性别
     */
    private String  gender;

    /**
     * 真实姓名
     */
    private String  realName;

    /**
     * 是否绑定手机号
     */
    private Boolean isBindPhone;

    /**
     * 是否填写报名卡
     */
    private Boolean isCard;

    /**
     * 报名卡-手机号码
     */
    private String  phone;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public Boolean getIsBindPhone() {
        return isBindPhone;
    }

    public void setIsBindPhone(Boolean isBindPhone) {
        this.isBindPhone = isBindPhone;
    }

    public Boolean getIsCard() {
        return isCard;
    }

    public void setIsCard(Boolean isCard) {
        this.isCard = isCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public static RegisterVo getVo(RegisterModel register) {
        if (register == null) {
            return null;
        }
        RegisterVo vo = new RegisterVo();
        vo.setAccount(register.getAccount());
        GenderEnum gender = register.getGender();
        if (gender != null) {
            vo.setGender(gender.getCode());
        }
        vo.setHeadimgUrl(register.getHeadimgUrl());
        vo.setNickName(register.getNickName());
        vo.setRealName(register.getRealName());
        vo.setIsBindPhone(StringUtils.isNotBlank(register.getAccount()));
        vo.setIsCard((StringUtils.isNotBlank(register.getPhone())
                      || StringUtils.isNotBlank(register.getRealName())));
        vo.setPhone(register.getPhone());
        return vo;
    }
}
