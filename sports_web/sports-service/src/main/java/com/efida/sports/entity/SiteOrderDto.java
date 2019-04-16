package com.efida.sports.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 赛场订单传输对象
 * 
 * @author zengbo
 * @version $Id: SiteOrderDto.java, v 0.1 2018年6月20日 上午10:35:23 zengbo Exp $
 */
public class SiteOrderDto implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    /**
     * 赛场CODE
     */
    private String            siteCode;

    /**
     * 赛事code
     */
    private String            matchCode;

    /**
     * 比赛项CODE
     */
    private List<String>      eventdCodes;

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getMatchCode() {
        return matchCode;
    }

    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode;
    }

    public List<String> getEventdCodes() {
        return eventdCodes;
    }

    public void setEventdCodes(List<String> eventdCodes) {
        this.eventdCodes = eventdCodes;
    }

}
