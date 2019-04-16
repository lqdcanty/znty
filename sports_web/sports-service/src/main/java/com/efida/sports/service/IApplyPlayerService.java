package com.efida.sports.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.efida.sports.entity.ApplyPlayer;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zoutao
 * @since 2018-07-24
 */
public interface IApplyPlayerService extends IService<ApplyPlayer> {
    /**
     * 根据报名信息编号获取运动员编号
     * 
     * @param applyCode
     * @return
     */
    List<String> selectPlayerCodeByApplyCode(String applyCode);

    /**
     * 创建中间表记录
     * 
     * @param applyPlayer
     */
    void createApplyPlayer(ApplyPlayer applyPlayer);

    /**
     * 根据报名编号查询团队人数
     * 
     * @param applyCode
     * @return
     */
    Integer getTotalTermNum(String applyCode);

    /**
     * 根据远动员编号删除中间表
     * 
     * @param playerCode
     */
    void deleteByPlayerCode(String playerCode);

    /**
     * 
     * 根据报名编号删除中间表
     * @param applyCode
     */
    void deleteByApplyCode(String applyCode);

}
