package com.efida.sports.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.efida.sports.entity.RegisterDayReport;
import com.efida.sports.model.ChannelRegisterNumModel;
import com.efida.sports.model.RegisterDayReportTrendModel;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zoutao
 * @since 2018-08-29
 */
public interface RegisterDayReportMapper extends BaseMapper<RegisterDayReport> {
    /**
     * 获取总用户数
     * 
     * @return
     */
    long getSumRegister();

    /**
     * 从开始时间查询新增加用户数
     * 
     * @param startTime
     * @return
     */
    long getNewRegsiyer(Date startTime);

    /**
     * 获取用户增长趋势数据
     * @param page 
     * 
     * @param startTime
     * @param endTime
     * @return
     */
    List<RegisterDayReportTrendModel> selectRegisterTrend(@Param("startTime") Date startTime,
                                                          @Param("endTime") Date endTime);

    List<RegisterDayReportTrendModel> selectPageRegisterTrend(Page<RegisterDayReportTrendModel> page,
                                                              @Param("startTime") Date startTime,
                                                              @Param("endTime") Date endTime);

    /**
     * 获取一段时间内用户增长数top+topNum的ChannelCode
     * 
     * @param startTime
     * @param endTime
     * @param topNum
     * @return
     */

    List<String> getTopNumChannel(@Param("startTime") Date startTime,
                                  @Param("endTime") Date endTime, @Param("topNum") Integer topNum);

    /**
     * 查询渠道分析数据
     * 查询一段时间类各个渠道的新增用户数，总用户数
     * 
     * @param startTime
     * @param endTime
     * @param channelCodes
     * @return
     */
    List<RegisterDayReportTrendModel> selectChannelAnalysis(@Param("startTime") Date startTime,
                                                            @Param("endTime") Date endTime,
                                                            @Param("channelCodes") List<String> channelCodes);

    /**
     * 查询渠道用户增长趋势
     * 
     * 查询一段时间类 每天各个渠道的新增人数,总人数
     * @param startTime
     * @param endTime
     * @param channelCodes
     * @return
     */
    List<RegisterDayReportTrendModel> channelTrendAnalysis(@Param("startTime") Date startTime,
                                                           @Param("endTime") Date endTime,
                                                           @Param("channelCodes") List<String> channelCodes);

    /**
     * 查询结束时间的所有注册来源
     * 
     * @param endTime
     * @return
     */
    List<String> getRegTerminals(@Param("endTime") Date endTime);

    /**
     * 
     * 查询结束时间段类所有渠道编号
     * @param endTime
     * @return
     */
    List<String> getChannelCods(@Param("endTime") Date endTime);

    List<ChannelRegisterNumModel> getChannelRegisterNum();

    List<RegisterDayReport> selectRegisterDayReport(@Param("startTime") Date startTime,
                                                    @Param("endTime") Date endTime);

}
