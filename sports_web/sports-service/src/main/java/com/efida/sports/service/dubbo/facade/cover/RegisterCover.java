/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.facade.cover;

import java.util.ArrayList;
import java.util.List;

import com.efida.easy.ucenter.facade.enums.TerminalEnum;
import com.efida.sport.facade.model.RegisterModel;

/**
 * 
 * @author zoutao
 * @version $Id: RegisterCover.java, v 0.1 2018年8月5日 下午2:06:16 zoutao Exp $
 */
public class RegisterCover {

    //    public static RegisterModel entity2model(Register register) {
    //        if (register == null) {
    //            return null;
    //        }
    //        RegisterModel model = new RegisterModel();
    //        model.setAccount(register.getAccount());
    //        model.setCity(register.getCity());
    //        model.setGender(register.getGender());
    //        model.setGenderStr(GenderEnum.getDescByCode(register.getGender()));
    //        model.setHeadimgUrl(register.getHeadimgUrl());
    //        model.setLastLoginTime(register.getLastLoginTime());
    //        model.setNickName(register.getNickName());
    //        model.setPlatfrom(register.getPlatfrom());
    //        model.setPlatfromStr(PlatformEnum.getDescByCode(register.getPlatfrom()));
    //        model.setProvince(register.getProvince());
    //        model.setRealName(register.getRealName());
    //        model.setRegisterCode(register.getRegisterCode());
    //        model.setRegTerminal(register.getRegTerminal());
    //        model.setRegTime(register.getRegTime());
    //        model.setRegTerminalStr(RegTerminalEnum.getDescByCode(register.getRegTerminal()));
    //        return model;
    //    }
    //
    //    public static List<RegisterModel> entities2models(List<Register> registers) {
    //        List<RegisterModel> list = new ArrayList<RegisterModel>();
    //        if (registers == null || registers.size() < 1) {
    //            return list;
    //        }
    //        for (Register register : registers) {
    //            list.add(entity2model(register));
    //        }
    //        return list;
    //
    //    }

    public static RegisterModel entity2model(com.efida.easy.ucenter.facade.model.RegisterModel register) {
        if (register == null) {
            return null;
        }
        RegisterModel model = new RegisterModel();
        model.setAccount(register.getAccount());
        model.setCity(register.getCity());
        com.efida.easy.ucenter.facade.enums.GenderEnum gender = register.getGender();
        model.setGender(gender != null ? gender.getCode() : "");
        model.setGenderStr(gender != null ? gender.getDesc() : "");
        model.setHeadimgUrl(register.getHeadimgUrl());
        model.setLastLoginTime(register.getLastLoginTime());
        model.setNickName(register.getNickName());
        com.efida.easy.ucenter.facade.enums.PlatformEnum platfrom = register.getPlatfrom();
        if (platfrom != null) {
            model.setPlatfrom(platfrom.getCode());
            model.setPlatfromStr(platfrom.getDesc());
        }
        model.setProvince(register.getProvince());
        model.setRealName(register.getRealName());
        model.setRegisterCode(register.getRegisterCode());
        TerminalEnum regTerminal = register.getRegTerminal();
        if (regTerminal != null) {
            model.setRegTerminal(regTerminal.getCode());
            model.setRegTerminalStr(regTerminal.getCname());
        }
        model.setRegTime(register.getRegTime());
        return model;
    }

    public static List<RegisterModel> ucenterModel2models(List<com.efida.easy.ucenter.facade.model.RegisterModel> records) {
        List<RegisterModel> list = new ArrayList<RegisterModel>();
        if (records == null || records.size() < 1) {
            return list;
        }
        for (com.efida.easy.ucenter.facade.model.RegisterModel register : records) {
            list.add(entity2model(register));
        }
        return list;
    }

}
