/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.model;

/**
 * 
 * @author zoutao
 * @version $Id: ChannelRegisterNUmModel.java, v 0.1 2018年9月4日 下午5:00:40 zoutao Exp $
 */
public class ChannelRegisterNumModel {

    private String channelCode;

    private Long   totalRegister;

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public Long getTotalRegister() {
        return totalRegister;
    }

    public void setTotalRegister(Long totalRegister) {
        this.totalRegister = totalRegister;
    }

}
