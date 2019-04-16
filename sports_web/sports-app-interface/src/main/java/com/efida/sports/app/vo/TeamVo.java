/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.app.vo;

import com.efida.sport.admin.facade.model.SpAthletesEnrollModel;
import com.efida.sports.entity.Player;
import com.efida.sports.enums.EventTypeEnums;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zoutao
 * @version $Id: TeamVo.java, v 0.1 2018年7月25日 下午6:51:21 zoutao Exp $
 * 团队成员
 */

public class TeamVo {

    private String         playerPhone;

    private String         playerName;

    private String         playerCode;

    private List<PlayerVo> players;

    public String getPlayerPhone() {
        return playerPhone;
    }

    public void setPlayerPhone(String playerPhone) {
        this.playerPhone = playerPhone;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerCode() {
        return playerCode;
    }

    public void setPlayerCode(String playerCode) {
        this.playerCode = playerCode;
    }

    public List<PlayerVo> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerVo> players) {
        this.players = players;
    }

    public static List<TeamVo> getVos(List<Player> players,
                                      SpAthletesEnrollModel athiete,String applyType) throws Exception {
        ArrayList<TeamVo> teams = new ArrayList<TeamVo>();
        if (players == null || players.size() < 1) {
            if(EventTypeEnums.personal.getCode().equals(applyType)){
                TeamVo team = new TeamVo();
                List<PlayerVo> vos = PlayerVo.getVos(null, athiete);
                team.setPlayers(vos);
                teams.add(team);
            }
            return teams;
        }
        for (Player player : players) {
            TeamVo vo = new TeamVo();
            vo.setPlayerName(player.getPlayerName());
            vo.setPlayerPhone(player.getPlayerPhone());
            List<PlayerVo> vos = PlayerVo.getVos(player, athiete);
            vo.setPlayerCode(player.getPlayerCode());
            vo.setPlayers(vos);
            teams.add(vo);
        }
        return teams;
    }

}
