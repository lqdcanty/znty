/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.web.vo;

import java.io.Serializable;

/**
 * 
 * @author zoutao
 * @version $Id: AppVersionVo.java, v 0.1 2018年6月20日 下午6:19:38 zoutao Exp $
 */
public class AppVersionVo implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;
    /**
     * 版本号
     */
    private String            version;
    /**
     * 下载地址
     */
    private String            downUrl;
    /**
     * 描述
     */
    private String            desc;
    /**
    * 是否强制升级
    */
    private Boolean           isForcedUpgrade;
    /**
     * 审核状态
     */
    private Boolean           status;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getIsForcedUpgrade() {
        return isForcedUpgrade;
    }

    public void setIsForcedUpgrade(Boolean isForcedUpgrade) {
        this.isForcedUpgrade = isForcedUpgrade;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
