package com.efida.sports.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.service.IService;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.model.MessageInfoModel;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zoutao
 * @since 2018-05-18
 */
public interface IApplyInfoService extends IService<ApplyInfo> {
    /**
     * 根据订单code获取报名信息
     * @param orderCode
     * @return
     */
    List<ApplyInfo> getApplyInfoByOrderCode(String orderCode);

    /**
     * 报名成功， 采用数据库时间修改报名时间
     * 
     * @param orderCode
     */
    @Deprecated
    void applySuccess(String orderCode);

    /**
     * 
     * 报名成功， 采用服务器时间（与支付时间一致）修改报名时间。
     * @param orderCode
     * @param applyTime
     */
    public void applySuccess(String orderCode, Date applyTime);

    /**
     * 根据比赛项编号查询报名人数
     * @param eventCode
     * @return
     */
    Long getApplyCountByEventCode(String eventCode);

    /**
     * 获取头一天报名的数据
     * 
     * @return
     */
    List<ApplyInfo> selectApplyInfoByDate(String sendStatus);

    /**
     * 修改发送状态
     * 
     * @param applyCode
     * @param sendStatus
     */
    void updateSendStatus(String applyCode, String sendStatus);

    /**
     * 
     * 
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public List<ApplyInfo> selectUnSycInfos(int pageNumber, int pageSize);

    /**
     * 根据 aassettoId 和matchCode获取报名信息
     * @param aassettoId
     * @param matchCode
     * @return
     */
    List<ApplyInfo> getApplyInfosByAassetoidAndMatchCode(String aassettoId, String matchCode);

    /**
     * 根据用户编码获取用户报名成功信息
     * @param registerCode
     * @param applyStatus
     * @param pageNumber
     * @param pageSize
     * @return
     */
    List<MessageInfoModel> selectApplyInfo(String registerCode, String applyStatus, int pageNumber,
                                           int pageSize);

    /**
     * 根据用户编码获取用户报名成功未读信息
     * @param registerCode
     * @param applyStatus
     * @return
     */
    Integer selectIsReadNum(String registerCode, String applyStatus, String isRead);

    /**
     * 根据用户编码获取用户报名成功未读信息
     * @param registerCode
     * @param applyStatus
     * @return
     */
    Integer updateIsRead(String registerCode, String applyStatus, String oldIsRead,
                         String newIsRead);

    /**
     * 查询所有比赛中报名的比赛数据
     * 
     * @return
     */
    List<ApplyInfo> queryGameingData();

    /**
     * 根据外部幂等id查询报名信息
     * 
     * @param idempotentId
     * @return
     */
    ApplyInfo getApplyInfoIdempotentId(String idempotentId);

    /**
     * 通过运动员手机号及赛事信息查询成功报名单. 
     *  注意：不支持参数为空查询。 为了数据库性能考虑，并且最多返回200条记录
     * 
     * @param matchCode
     * @param siteCode
     * @param phones
     * @return
     */
    List<ApplyInfo> queryApplyInfoByPhoneAndMachInfo(String matchCode, String siteCode,
                                                     Set<String> phones);

    /**
     * 查询报名单 通过订单编号列表
     * 
     * @param orderCodes
     * @return
     */
    List<ApplyInfo> getApplyInfosByOrderCodes(List<String> orderCodes);

    /**
     * 根据idempotent 查询报名信息
     * 
     * @param idempotent
     * @return
     */
    List<ApplyInfo> applyInfosByidempotent(String idempotent);

    /**
     * 查询重复的幂等id
     * 
     * @param limit
     * @return
     */
    List<String> selectRepetitionIdempotentId(int limit);

    /**
     * 修复channelCode
     * 
     * @param data
     */
    void repairRegisterChannelCode4Apply(RegisterModel register);

    /**
     * 查询咕咚报名成功同步失败的报名信息
     * @return
     */
    List<ApplyInfo> synchronousData(int pageSize);

    /**
     * 修改咕咚报名成功同步状态
     *
     * @param apply
     */
    void updateSynchronousData(ApplyInfo apply);

    /**
     * 咕咚根据比赛项编号和手机号查询报名成功数
     * @param eventCode
     * @param phone
     * @return
     */
    Long getApplyCountByEventCodeAndPhone(String eventCode,String phone);
}
