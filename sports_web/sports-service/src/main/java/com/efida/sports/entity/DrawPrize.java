package com.efida.sports.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 
 * </p>
 *
 * @author zoutao
 * @since 2018-10-17
 */
public class DrawPrize implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer           id;
    /**
     * 活动编号
     */
    private String            activityCode;
    /**
     * 奖品编号
     */
    private String            prizeCode;
    /**
     * 奖品名称
     */
    private String            prizeName;
    /**
     * 奖品类型:  1:一等奖 2.二等奖 3.三等奖4.四等奖5.五等奖6.谢谢参与 
     */
    private String            prizeType;

    /**
     * 奖品数量 -1无限制
     */
    private Integer           prizeNumber;
    /**
     * 奖品剩余可抽取数量
     */
    private Integer           remainNumber;
    /**
     * 中奖概率
     */
    private BigDecimal        ratio;
    /**
     * 周期
     */
    private Integer           cycleNumber;
    /**
     * 状态 1可使用 0不可使用
     */
    private Integer           status;
    /**
     * 是否删除 1.已经删除 0 未删除
     */
    private Integer           isDel;
    /**
     * 创建时间
     */
    private Date              gmtCreate;
    /**
     * 修改时间
     */
    private Date              gmtModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getPrizeCode() {
        return prizeCode;
    }

    public void setPrizeCode(String prizeCode) {
        this.prizeCode = prizeCode;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }

    public Integer getPrizeNumber() {
        return prizeNumber;
    }

    public void setPrizeNumber(Integer prizeNumber) {
        this.prizeNumber = prizeNumber;
    }

    public Integer getRemainNumber() {
        return remainNumber;
    }

    public void setRemainNumber(Integer remainNumber) {
        this.remainNumber = remainNumber;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public Integer getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(Integer cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
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
        return "DrawPrize{" + ", id=" + id + ", activityCode=" + activityCode + ", prizeCode="
               + prizeCode + ", prizeName=" + prizeName + ", prizeType=" + prizeType
               + ", prizeNumber=" + prizeNumber + ", remainNumber=" + remainNumber + ", ratio="
               + ratio + ", cycleNumber=" + cycleNumber + ", status=" + status + ", isDel=" + isDel
               + ", gmtCreate=" + gmtCreate + ", gmtModified=" + gmtModified + "}";
    }
}
