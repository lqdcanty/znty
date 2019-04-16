/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.model;

import java.util.Date;

/**
 * 
 * @author zhiyang
 * @version $Id: MatchShortInfo.java, v 0.1 2018年8月2日 下午9:34:45 zhiyang Exp $
 */
public class MatchShortInfo {

    private String poster;
    private Date   matchDate;
    private String statusCode;
    private String statusDesc;

    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

}
