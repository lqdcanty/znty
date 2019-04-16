/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.vo;

import java.io.Serializable;

/**
 * 
 * @author zoutao
 * @version $Id: ParameterVo.java, v 0.1 2018年6月20日 下午1:37:07 zoutao Exp $
 */
public class ParameterVo implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;
    private String            val;
    private String            desc;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
