package com.efida.sports.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.efida.easy.ucenter.facade.enums.TerminalEnum;
import com.efida.sport.facade.enums.RegTerminalEnum;
import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.entity.LeaderInfo;
import com.efida.sports.entity.PayOrder;
import com.efida.sports.entity.Player;
import com.efida.sports.entity.SiteOrderDto;
import com.efida.sports.enums.OrderStatusEnum;
import com.efida.sports.enums.PayGateWayEnum;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zoutao
 * @since 2018-05-18
 */
public interface IPayOrderService extends IService<PayOrder> {
    /**
     * 通过订单编号获取订单
     * 
     * @param orderCode
     * @return
     */
    PayOrder getOrderByCode(String orderCode);

    /**
     * 
     * 完成支付   支付完成回调是否调用
     * @param orderCode  订单编号
     * @param trxNo      支付流水号
     * @param weichatPay 支付网关
     */
    void completePaySuccess(String orderCode, String trxNo, PayGateWayEnum payGetWay);

    /**
     * 获取订单详情
     * 
     * @param orderCode
     * @return
     */
    PayOrder getOrderDetail(String orderCode);

    /**
     * 
     * 
     * @param itemCodes 比赛项编号
     * @param fileidCode 赛场编号
     * @param player     运动员
     * @param registerCode  用户
     * @param ip
     * @param referer
     * @return
     */
    @Deprecated
    PayOrder addApplyOrder(List<String> itemCodes, String fileidCode, Player player,
                           String registerCode, String ip, String referer,
                           RegTerminalEnum regTerminalEnum);

    /**
     * 分页查询订单数据（优化版本）
     * 
     * @param currentPage
     * @param pageSize
     * @param registerCode
     * @param orderStatus
     * @return
     */
    public Page<PayOrder> selectEnrollInfoByStatus(int currentPage, int pageSize,
                                                   String registerCode,
                                                   OrderStatusEnum orderStatus);

    /**
     *  分页查询订单数据
     * 
     * @param currentPage
     * @param pageSize      每页大小
     * @param registerCode  用户编号
     * @see OrderStatusEnum
     * @param orderStatus   订单状态
     * @return
     */
    Page<PayOrder> selectPagePageOrderByStatus(int currentPage, int pageSize, String registerCode,
                                               OrderStatusEnum orderStatus);

    /**
     * 创建待完善的订单
     * 
     * @param ids
     * @param siteCode
     * @param registerCode
     * @return
     */
    PayOrder createWaitComplete(List<String> eventdCodes, String siteCode, String ip,
                                String registerCode, RegTerminalEnum regTerminal);

    /**
     * 创建待完善的订单
     * 
     * @param sites
     * @param ip
     * @param registerCode
     * @return
     */
    public PayOrder createWaitComplete(List<SiteOrderDto> sites, String ip, String registerCode);

    /**
     * 完善订单
     * 个人项目调用
     * 
     * @param orderCode
     * @param registerCode
     * @param player
     * @return
     */
    PayOrder completeOrder(String orderCode, String registerCode, Player player);

    /**
     * 保存运动员信息
     * 
     * @param orderCode  订单编号
     * @param registerCode   用户编号
     * @param player      运动员
     * @return
     */
    PayOrder savePlayer(String orderCode, String registerCode, Player player);

    /**
     * 
     * 取消订单
     * @param orderCode
     * @param registerCode
     * @return
     */
    PayOrder cancelOrder(String orderCode, String registerCode);

    /**
     * 检查订单是否可以支付
     * 
     * @param orderCode
     * @return
     */
    PayOrder checkOrderCanPay(String orderCode);

    /**
     * 批量修改订单创建者
     * @param oldRegiterCode;
     * @param newRegisterCode;
     */
    void batchUpdateOrderCreater(String registerCode, String newRegisterCode);

    /**
     * dmp系统同步数据创建报名信息
     * @param applyInfo
     * @param player
     */
    String createApply(ApplyInfo apply, Player player, TerminalEnum terminal, String ip);

    /**
     * 
     * dmp系统同步数据创建报名信息
     * @param applyInfo
     * @param leader
     * @param players
     * @return
     */

    String createDmpApply(ApplyInfo applyInfo, LeaderInfo leader, List<Player> players);

    /**
     * 
     * 添加团队成员
     * @param player  运动员信息
     * @param orderCode   订单编号
     * @param registerCode   用户编号
     * @return
     */
    Player createTeamMembers(Player player, String orderCode, String registerCode);

    /**
     * 完善团队报名信息
     * 团队报名调用
     * @param learn  领队信息
     * @param orderCode  订单编号
     * @param registerCode 用户编号
     * @return
     */
    PayOrder completeTeamApply(LeaderInfo learn, String orderCode, String registerCode);

    /**
     * 保存领队信息
     * 
     * @param learn   领队信息
     * @param registerCode  用户编号
     * @param orderCode     订单编号
     */
    void saveLearnerInfo(LeaderInfo learn, String registerCode, String orderCode);

    /**
     * 报表查询-支付统计
     * @author antony
     * @param params
     */
    List<PayOrder> selectSettlementPayOrderList(Page<PayOrder> pagePayOrder,
                                                Map<String, Object> params);

    /**
     * 报表查询-支付统计(不分页)
     * @author antony
     * @param params
     */
    List<PayOrder> selectSettlementPayOrderList(Map<String, Object> params);

    /**
     * 报表查询-支付统计总数
     * @author antony
     * @param params
     */
    PayOrder selectSettlementPayOrderCount(Map<String, Object> params);

    /**
     * 报表查询-支付明细
     * @author antony
     * @param params
     */
    List<PayOrder> selectSettlementPayOrderDetail(Page<PayOrder> pagePayOrder,
                                                  Map<String, Object> params);

    /**
     * 
     *  
     * @param unitCode
     * @param info
     */
    public void checkUniteStrategy(String unitCode, ApplyInfo info);

    /**
     * 处理脏数据
     * @throws InterruptedException 
     */
    void processingRepetitionIdempotentId() throws InterruptedException;

    /**
     * 删除重复数据
     * 
     * @param info
     */
    void delRepetitionData(ApplyInfo info);

    /**
     * 修复用户编号
     * 
     * @param info
     */
    void repairRegisterCode(ApplyInfo info);

}
