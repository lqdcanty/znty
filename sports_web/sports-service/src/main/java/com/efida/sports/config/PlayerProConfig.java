/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zoutao
 * @version $Id: PlayerProConfig.java, v 0.1 2018年7月19日 下午9:46:58 zoutao Exp $
 */
public class PlayerProConfig {
    private static PlayerProConfig instance;

    private PlayerProConfig() {

    }

    public static PlayerProConfig getInstance() {
        if (instance == null) {
            instance = new PlayerProConfig();
        }
        return instance;
    }

    private static List<String> playerPro = null;

    public List<String> getPlayerPro() {
        if (playerPro == null) {
            playerPro = new ArrayList<String>();
            playerPro.add("playerCode");
            playerPro.add("verifyCode");
            playerPro.add("playerPhone");
            playerPro.add("playerName");
            playerPro.add("sex");
            playerPro.add("registerCode");
            playerPro.add("email");
            playerPro.add("playerHeight");
            playerPro.add("playerWeight");
            playerPro.add("playerBirth");
            playerPro.add("playerNationality");
            playerPro.add("playerAddress");
            playerPro.add("playerCerType");
            playerPro.add("playerCerNo");
            playerPro.add("playerBloodType");
            playerPro.add("playerNation");
            playerPro.add("playerClothSize");
            playerPro.add("playerWorkUnit");
            playerPro.add("playerEmergencyName");
            playerPro.add("playerEmergencyPhone");
            playerPro.add("attUrl");
            playerPro.add("attOne");
            playerPro.add("attTwo");
            playerPro.add("imgUrl");
            playerPro.add("assettoId");
        }
        return playerPro;

    }

}
