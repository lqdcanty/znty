package com.efida.sports.entity;

import com.baomidou.mybatisplus.enums.IdType;
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
public class ApplyPlayer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 报名信息编号
     */
    private String applyCode;
    /**
     * 运动员编号
     */
    private String playerCode;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    public String getPlayerCode() {
        return playerCode;
    }

    public void setPlayerCode(String playerCode) {
        this.playerCode = playerCode;
    }

    @Override
    public String toString() {
        return "ApplyPlayer{" +
        ", id=" + id +
        ", applyCode=" + applyCode +
        ", playerCode=" + playerCode +
        "}";
    }
}
