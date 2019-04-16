/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.biz;

import com.efida.sport.facade.enums.RegTerminalEnum;
import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.entity.Player;

/**
 * 
 * @author lizhiyang
 * @version $Id: GudongEnrollService.java, v 0.1 2018年9月6日 上午8:19:22 lizhiyang Exp $
 */
public interface GudongEnrollService {

    /**
     * 
     * 
     * @param apply
     * @param player
     * @param regterminal
     * @return
     */
    public String createAppyInfo(ApplyInfo apply, Player player, RegTerminalEnum regterminal,String ip);

    /**
     * 只对报名成功单子进行通知，通知咕咚报名成功。
     * 
     * @param orderCode
     */
    void notifyGudong(String orderCode);

    /**
     * 同步报名记录
     * @param applyInfo
     */
    void notifyGudong(ApplyInfo applyInfo);

}
