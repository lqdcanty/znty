package com.efida.sports.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.efida.sport.facade.enums.RegTerminalEnum;

/**
 * <p>
 * 
 * </p>
 *
 * @author zoutao
 * @since 2018-09-11
 */
public class RegisterAccessLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long              id;
    /**
     * 访问日志唯一编号
     */
    private String            accessLogCode;
    /**
     * 用户唯一编号
     */
    private String            registerCode;
    /**
     * ip
     */
    private String            ip;
    /**
     * 访问来源
     * @see RegTerminalEnum
     */
    private String            accessTerminal;
    /**
     * 访问时间(秒)
     */
    private Date              accessTime;
    /**
     * 访问时间(天)
     */
    private Date              accessDay;
    /**
     * 访问入口
     */
    private String            accessEntry;
    /**
     * 创建时间
     */
    private Date              gmtCreate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessLogCode() {
        return accessLogCode;
    }

    public void setAccessLogCode(String accessLogCode) {
        this.accessLogCode = accessLogCode;
    }

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    public Date getAccessDay() {
        return accessDay;
    }

    public void setAccessDay(Date accessDay) {
        this.accessDay = accessDay;
    }

    public String getAccessEntry() {
        return accessEntry;
    }

    public void setAccessEntry(String accessEntry) {
        this.accessEntry = accessEntry;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getAccessTerminal() {
        return accessTerminal;
    }

    public void setAccessTerminal(String accessTerminal) {
        this.accessTerminal = accessTerminal;
    }

    @Override
    public String toString() {
        return "RegisterAccessLog [id=" + id + ", accessLogCode=" + accessLogCode
               + ", registerCode=" + registerCode + ", ip=" + ip + ", accessTerminal="
               + accessTerminal + ", accessTime=" + accessTime + ", accessDay=" + accessDay
               + ", accessEntry=" + accessEntry + ", gmtCreate=" + gmtCreate + "]";
    }

}
