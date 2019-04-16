/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.pc.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.efida.sport.admin.facade.model.SpProjectTypeModel;

/**
 * 
 * @author zoutao
 * @version $Id: GameTypeVo.java, v 0.1 2018年5月22日 下午1:52:35 zoutao Exp $
 */
public class GameVo {

    private Long   id;
    private String gameCode;
    private String gameName;
    private String gameImg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameImg() {
        return gameImg;
    }

    public void setGameImg(String gameImg) {
        this.gameImg = gameImg;
    }

    public static List<GameVo> getVos(List<SpProjectTypeModel> types) {

        List<GameVo> list = new ArrayList<GameVo>();
        if (types == null || types.size() < 1) {
            return list;
        }

        for (SpProjectTypeModel type : types) {
            GameVo vo = getVo(type);
            if (vo != null) {
                list.add(vo);
            }
        }
        return list;
    }

    public static GameVo getVo(SpProjectTypeModel type) {
        if (type == null) {
            return null;
        }
        GameVo vo = new GameVo();
        vo.setGameCode(type.getGameCode());
        vo.setGameName(type.getGameName());
        vo.setGameImg(type.getImage());
        return vo;

    }

}
