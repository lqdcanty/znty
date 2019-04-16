package com.efida.sports.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.efida.sports.entity.Player;
import com.efida.sports.entity.PlayerEx;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zoutao
 * @since 2018-05-18
 */
public interface IPlayerService extends IService<Player> {
    /**
     * 根据运动员编号获取单个运动员信息
     * 比赛项为个人时调用
     * 
     * @param playerCode
     * @return
     */
    Player getPlayerByPlayerCode(String playerCode);

    /**
     * 
     * 根据报名信息编号获取运动员
     * @param applyCode
     * @return
     */
    Player getPersonalPlayerByApplyCode(String applyCode);

    /**
     * 批量修改运动员创建者
     * @param oldRegiterCode;
     * @param newRegisterCode;
     */
    void batchUpdatePlayerCreater(String oldRegiterCode, String newRegisterCode);

    /**
     * 根据报名信息编号获取运动员列表 
     * 报名项为团队时候调用
     * 
     * @param applyCode
     * @return
     */
    List<Player> selectPlayerByApplyInfoCode(String applyCode);

    /**
     * 删除团队运动员
     * 
     * @param playerCode  运动员编号
     */
    void deletTeamMembers(String playerCode);

    /**
     * 验证运动员属性
     * 
     * @param player
     */
    void checkPlayer(Player player);

    /**
     * 
     * 
     * @param applyCodes
     * @return
     */
    List<PlayerEx> selectPlayersByApplyInfoCodes(List<String> applyCodes);

    void deletPlayerByApplyInfoCode(String applyCode);
}
