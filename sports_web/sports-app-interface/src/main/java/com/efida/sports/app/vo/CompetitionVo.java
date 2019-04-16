package com.efida.sports.app.vo;

import java.util.HashMap;
import java.util.Map;

public class CompetitionVo {

    private String area;
    private String event;
    private String group;
    private String competitionCode;

    public static CompetitionVo getCompetitionVo(Map<String,String> map){
        CompetitionVo competitionVo = new CompetitionVo();
        competitionVo.setArea(map.get("area"));
        competitionVo.setCompetitionCode(map.get("competitionCode"));
        competitionVo.setEvent(map.get("event"));
        competitionVo.setGroup(map.get("group"));
      return competitionVo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCompetitionCode() {
        return competitionCode;
    }

    public void setCompetitionCode(String competitionCode) {
        this.competitionCode = competitionCode;
    }
}
