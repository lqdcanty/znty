package com.efida.sports.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.efida.sports.entity.ApplyInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zoutao
 * @since 2018-05-18
 */
public interface ApplyInfoMapper extends BaseMapper<ApplyInfo> {
    /**
     * 报名成功
     * 
     * @param orderCode
     */
    void applySuccess(String orderCode);

    /**
     * 报名成功
     * 
     * @param apply
     */
    void updateApplySuccess(ApplyInfo apply);

    /**
     * 查询报名数
     * @param eventCode
     * @return
     */
    Long getApplyCountByEventCode(String eventCode);

    /**
     * 修改发送状态
     * 
     * @param apply
     */
    void updateSendStatus(ApplyInfo apply);

    /**
     * 
    
        map.put("start", start);
        map.put("limit", qs.getPageSize());
        
     * @param map
     * @return
     */
    List<ApplyInfo> selectUnSycInfos(Map<String, Object> map);

    /**
     * 查询前一天报名比赛数据
     * 
     * @return
     */
    List<ApplyInfo> queryBeforeDayData();

    /**
     * 查询前一天报名比赛失败数据
     * 
     * @return
     */
    List<ApplyInfo> queryBeforeDayFailData();

    /**
     *  根据神力科萨账户id和赛事编号查询报名信息
     * 
     * @param map
     * @return
     */
    List<ApplyInfo> getApplyInfosByAassetoidAndMatchCode(Map<String, Object> map);

    /**
     *
     map.put("registerCode", registerCode);
     map.put("applyStatus", applyStatus);
     map.put("start", start);
     map.put("limit", qs.getPageSize());
     * @param map
     * @return
     */
    List<ApplyInfo> selectApplyInfo(Map<String, Object> map);

    /**
     *
     map.put("registerCode", registerCode);
     map.put("applyStatus", applyStatus);
     * @param map
     * @return
     */
    Integer selectIsReadNum(Map<String, Object> map);

    /**
     map.put("registerCode", registerCode);
     map.put("applyStatus", applyStatus);
     * 改变消息状态
     *
     * @param map
     */
    Integer updateIsRead(Map<String, Object> map);

    /**
     * 查询所有比赛中报名的比赛数据
     * 
     * @return
     */
    List<ApplyInfo> queryGameingData();

    /**
     * map参数：
     *  matchCode
     *  siteCode
     *  phones
     * 
     * @param map
     * @return
     */
    List<ApplyInfo> querySuccessApplyInfoByPhoneAndMachInfo(Map<String, Object> map);

    /**
     * 
     * 
     * @param orderCodes
     * @return
     */
    List<ApplyInfo> getApplyInfosByOrderCodes(List<String> orderCodes);

    /**
     * 查询重复的IdempotentId
     * 
     * @param limit
     * @return
     */
    List<String> selectRepetitionIdempotentId(int limit);

    /**
     * 查询用户编号为空的报名信息
     * 
     * @param limit
     * @return
     */
    List<ApplyInfo> selectInfosByRegisterCodeIsNull(@Param("startIndex") int startIndex,
                                                    @Param("limit") int limit);

    /**
     * 查询咕咚报名成功同步失败的报名信息
     * @return
     */
    List<ApplyInfo> selectSynchronousData(Map<String, Object> map);

    /**
     * 修改咕咚报名成功同步状态
     *
     * @param apply
     */
    void updateSynchronousData(ApplyInfo apply);

    /**
     * 咕咚根据分组id和手机号查询报名成功数
     * @param map
     * @return
     */
    Long getApplyCountByEventCodeAndPhone(Map<String, Object> map);
}
