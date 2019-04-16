/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.vo;

import com.efida.sports.entity.LeaderInfo;

/**
 * 
 * @author zoutao
 * @version $Id: LeaderInfoVo.java, v 0.1 2018年7月25日 下午3:03:15 zoutao Exp $
 */
public class LeaderInfoVo {

    private String applyInfoCode;
    /**
     * 领队唯一编号
     */
    private String leaderCode;
    /**
     * 创建人唯一编号
     */
    private String registerCode;
    /**
     * 团队名称
     */
    private String teamName;
    /**
     * 领队名称
     */
    private String leaderName;
    /**
     * 领队手机号
     */
    private String leaderPhone;

    public String getApplyInfoCode() {
        return applyInfoCode;
    }

    public void setApplyInfoCode(String applyInfoCode) {
        this.applyInfoCode = applyInfoCode;
    }

    public String getLeaderCode() {
        return leaderCode;
    }

    public void setLeaderCode(String leaderCode) {
        this.leaderCode = leaderCode;
    }

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getLeaderPhone() {
        return leaderPhone;
    }

    public void setLeaderPhone(String leaderPhone) {
        this.leaderPhone = leaderPhone;
    }

    public static LeaderInfoVo getVO(LeaderInfo info) {
        if (info == null) {
            return null;
        }
        LeaderInfoVo vo = new LeaderInfoVo();
        vo.setApplyInfoCode(info.getApplyInfoCode());
        vo.setLeaderCode(info.getLeaderCode());
        vo.setLeaderName(info.getLeaderName());
        vo.setLeaderPhone(info.getLeaderPhone());
        vo.setRegisterCode(info.getRegisterCode());
        vo.setTeamName(info.getTeamName());
        return vo;

    }

}
