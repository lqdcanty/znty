package com.efida.sports.pc.web.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.efida.sport.admin.facade.model.SpMatchInfoModel;
import com.efida.sport.admin.facade.model.SpPlayingAreaModel;
import com.efida.sports.util.DateUtil;

/**
 * 赛事转换信息
 * 
 * @author zengbo
 * @version $Id: MatchRelevantVo.java, v 0.1 2018年7月28日 上午11:44:20 zengbo Exp $
 */
public class MatchRelevantVo {

    private String regTime;

    private String startTime;

    private String endTime;

    private String address;

    private String matchCode;

    private String poster;

    private String appBanner;

    private String pcPanner;

    private String regStatus;

    public static MatchRelevantVo getMatchRelevantVo(SpMatchInfoModel model,
                                                     List<SpPlayingAreaModel> areas) {
        MatchRelevantVo vo = new MatchRelevantVo();
        if (model == null) {
            return vo;
        }
        vo.setRegTime(DateUtil.formatWeb(model.getRegTime()));
        vo.setStartTime(DateUtil.formatWeb(model.getStartTime()));
        vo.setEndTime(DateUtil.formatWeb(model.getEndTime()));
        vo.setAddress(getAddress(areas));
        vo.setMatchCode(model.getMatchCode());
        vo.setPoster(model.getPoster());
        vo.setPcPanner(model.getPcPanner());
        vo.setAppBanner(model.getAppBanner());
        vo.setRegStatus(model.getRegStatus());
        Date eTime = model.getEndTime();
        if (eTime.before(new Date())) {
            //比赛结束
            vo.setRegStatus("end");
        }
        Date regTime = model.getRegTime();
        if (regTime.before(new Date())) {
            //报名截止
            vo.setRegStatus("enroll_end");
        }
        return vo;
    }

    public static String getAddress(List<SpPlayingAreaModel> areas) {
        String address = "";
        if (areas == null) {
            return address;
        }
        List<String> addrList = addressRemoval(areas);
        for (String str : addrList) {
            if (StringUtils.isNotBlank(str)) {
                address = address + str + "、";
            }
        }
        if (StringUtils.isNotBlank(address)) {
            address = address.substring(0, address.length() - 1);
        }
        return address;
    }

    public static List<String> addressRemoval(List<SpPlayingAreaModel> areas) {
        List<String> list = new ArrayList<String>();
        for (SpPlayingAreaModel model : areas) {
            if (StringUtils.isNotBlank(model.getFieldAddress())
                && !list.contains(model.getFieldAddress())) {
                list.add(model.getFieldAddress());
            }
        }
        return list;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMatchCode() {
        return matchCode;
    }

    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getAppBanner() {
        return appBanner;
    }

    public void setAppBanner(String appBanner) {
        this.appBanner = appBanner;
    }

    public String getPcPanner() {
        return pcPanner;
    }

    public void setPcPanner(String pcPanner) {
        this.pcPanner = pcPanner;
    }

    public String getRegStatus() {
        return regStatus;
    }

    public void setRegStatus(String regStatus) {
        this.regStatus = regStatus;
    }

}
