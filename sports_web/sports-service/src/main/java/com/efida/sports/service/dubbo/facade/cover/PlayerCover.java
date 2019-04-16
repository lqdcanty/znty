/**
 * 
 */
package com.efida.sports.service.dubbo.facade.cover;

import java.util.ArrayList;
import java.util.List;

import com.efida.sport.facade.enums.SexEnum;
import com.efida.sport.facade.model.PlayerModel;
import com.efida.sports.entity.Player;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.util.SeqWorkerUtil;

/**
 * @author Administrator
 *
 */
public class PlayerCover {

    public static Player model2entity(PlayerModel model) {
        if (model == null) {
            return null;
        }
        Player player = new Player();
        //        player.setPlayerCode(model.getPlayerCode());
        player.setPlayerPhone(model.getPlayerPhone());
        player.setPlayerName(model.getPlayerName());
        if (SexEnum.male.getCode().equalsIgnoreCase(model.getSex())) {
            player.setSex(SexEnum.male.getCode());
        } else if (SexEnum.female.getCode().equalsIgnoreCase(model.getSex())) {
            player.setSex(SexEnum.female.getCode());
        } else {
            player.setSex(SexEnum.unknown.getCode());
        }
        player.setEmail(model.getEmail());
        player.setPlayerHeight(model.getPlayerHeight());
        player.setPlayerWeight(model.getPlayerWeight());
        player.setPlayerBirth(model.getPlayerBirth());
        player.setPlayerNationality(model.getPlayerNationality());
        player.setPlayerAddress(model.getPlayerAddress());
        player.setPlayerCerType(model.getPlayerCerType());
        player.setPlayerCerNo(model.getPlayerCerNo());
        player.setPlayerBloodType(model.getPlayerBloodType());
        player.setPlayerNation(model.getPlayerNation());
        player.setPlayerClothSize(model.getPlayerClothSize());
        player.setPlayerWorkUnit(model.getPlayerWorkUnit());
        player.setPlayerEmergencyName(model.getPlayerEmergencyName());
        player.setPlayerEmergencyPhone(model.getPlayerEmergencyPhone());
        player.setAttUrl(model.getAttUrl());
        player.setAttOne(model.getAttOne());
        player.setAttTwo(model.getAttTwo());
        player.setImgUrl(model.getImgUrl());
        player.setAssettoId(model.getAssettoId());
        player.setExtPro(model.getExtProp());
        player.setPlayerCode(SeqWorkerUtil.generateTimeSequence());
        return player;
    }

    public static List<Player> models2entitys(List<PlayerModel> players) {
        if (players == null || players.size() < 1) {
            throw new BusinessException("运动员不能为空");
        }
        ArrayList<Player> list = new ArrayList<Player>();
        for (PlayerModel playerModel : players) {
            list.add(model2entity(playerModel));
        }
        return list;
    }

}
