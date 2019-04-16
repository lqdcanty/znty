package com.efida.sports.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * 咕咚赛事对应表
 * Created by wangyan on 2018/9/11.
 */
public class GudongMatch implements Serializable {

    private static final long serialVersionUID = -6626571880436028933L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 赛事编号
     */
    private String code;

    /**
     * 咕咚对应编号
     */
    private String gudongCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGudongCode() {
        return gudongCode;
    }

    public void setGudongCode(String gudongCode) {
        this.gudongCode = gudongCode;
    }
}
