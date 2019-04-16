package com.efida.sports.pc.web.vo;

import java.io.Serializable;
import java.util.Date;

import com.efida.sports.entity.LeaderInfo;

/**
 * 
 * 
 * @author zengbo
 * @version $Id: LeaderInfoVo.java, v 0.1 2018年7月25日 下午3:41:05 zengbo Exp $
 */
public class LeaderInfoVo implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    private Long id;

    private String applyInfoCode;

    private String leaderCode;

    private String registerCode;

    private String teamName;

    private String leaderName;

    private String leaderPhone;

    private String            verifyCode;

    public static LeaderInfoVo getLeaderInfoVo(LeaderInfo leader) {
        LeaderInfoVo vo = new LeaderInfoVo();
        if (leader == null) {
            return vo;
        }
        vo.setId(leader.getId());
        vo.setApplyInfoCode(leader.getApplyInfoCode());
        vo.setLeaderCode(leader.getLeaderCode());
        vo.setRegisterCode(leader.getRegisterCode());
        vo.setTeamName(leader.getTeamName());
        vo.setLeaderName(leader.getLeaderName());
        vo.setLeaderPhone(leader.getLeaderPhone());
        return vo;
    }

    public static LeaderInfo getLeaderInfo(LeaderInfoVo vo) {
        LeaderInfo leader = new LeaderInfo();
        if (vo == null) {
            return leader;
        }
        leader.setId(vo.getId());
        leader.setApplyInfoCode(vo.getApplyInfoCode());
        leader.setLeaderCode(vo.getLeaderCode());
        leader.setRegisterCode(vo.getRegisterCode());
        leader.setTeamName(vo.getTeamName());
        leader.setLeaderName(vo.getLeaderName());
        leader.setLeaderPhone(vo.getLeaderPhone());
        leader.setGmtCreate(new Date());
        return leader;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }


}
