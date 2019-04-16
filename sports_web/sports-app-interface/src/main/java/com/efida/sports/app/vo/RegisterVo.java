package com.efida.sports.app.vo;

import org.apache.commons.lang3.StringUtils;

import com.efida.easy.ucenter.facade.enums.GenderEnum;
import com.efida.easy.ucenter.facade.model.RegisterModel;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "用户对象", description = "用户对象")
public class RegisterVo {

    /**
     * 用户唯一编号
     */
    private String            registerCode;

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
    }

    public static RegisterVo getVo(RegisterModel register) {
        if (register == null) {
            return null;
        }
        RegisterVo vo = new RegisterVo();
        vo.setRegisterCode(register.getRegisterCode());
        vo.setAccount(register.getAccount());
        GenderEnum gender = register.getGender();
        vo.setGender(gender != null ? gender.getCode() : "");
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
