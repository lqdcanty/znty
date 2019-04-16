package com.efida.sports.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zoutao
 * @since 2018-07-24
 */
public class LeaderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 报名信息编号
     */
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
    /**
     * 创建时间
     */
    private Date gmtCreate;
    /**
     * 修改时间
     */
    private Date gmtModified;


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

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "LeaderInfo{" +
        ", id=" + id +
        ", applyInfoCode=" + applyInfoCode +
        ", leaderCode=" + leaderCode +
        ", registerCode=" + registerCode +
        ", teamName=" + teamName +
        ", leaderName=" + leaderName +
        ", leaderPhone=" + leaderPhone +
        ", gmtCreate=" + gmtCreate +
        ", gmtModified=" + gmtModified +
        "}";
    }
}
