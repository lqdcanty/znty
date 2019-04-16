package com.efida.sports.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.efida.sport.facade.model.AccessAnalysisModel;
import com.efida.sport.facade.model.ChannelTrendAnalysisModel;
import com.efida.sport.facade.model.PagingResult;
import com.efida.sport.facade.model.RegisterStatisticsModel;
import com.efida.sport.facade.model.RegisterTrendAnalysisModel;
import com.efida.sport.facade.model.TerminaStatisticsModel;
import com.efida.sports.entity.RegisterDayReport;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zoutao
 * @since 2018-08-29
 */
public interface IRegisterDayReportService extends IService<RegisterDayReport> {

    /**
     * 实时生成今天用户日报表数据
     */
    void createResport();

    /**
     * 统计昨天得用户数据
     * 若未统计过   
     * 生成今天之前得所有日报表数据
     * 
     */
    void batchCreateDailyReport();

    /**
     * 获取用户统计数据
     * 
     * @return
     */
    RegisterStatisticsModel getRegisterStatistics();

    /**
     * 获取某个时间段用户增长数据
     * 
     * @param startTime
     * @param endTime
     * @return
     */
    List<RegisterTrendAnalysisModel> getRegisterGrowthTrend(Date startTime, Date endTime);

    /**
     * 获取某个时间段用户增长数据
     * 分页获取
     * 
     * @param currentPage
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    PagingResult<RegisterTrendAnalysisModel> getRegisterGrowthTrend(int currentPage, int pageSize,
                                                                    Date startTime, Date endTime);

    /**
     * 渠道用户分析
     * 一段时间段类 各个渠道的新增用户数，总用户数，新增占比数据
     * 
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param channelCodes  为空 去top5数据
     * @return
     */
    List<RegisterTrendAnalysisModel> channelAnalysis(Date startTime, Date endTime,
                                                     List<String> channelCodes);

    /**
     * 
     * 渠道用户趋势分析
     * 一段时间段了各个渠道 用户新增数据
     * @param startTime
     * @param endTime
     * @param channelCodes 为空 渠道用户数top5数据
     * @return
     */
    List<ChannelTrendAnalysisModel> channelTrendAnalysis(Date startTime, Date endTime,
                                                         List<String> channelCodes);

    /**
     * 按渠道分组 ，统计总用户数，按总用户数倒序查询渠道编号
     * 
     * @param startTime
     * @param endTime
     * @param topNum
     * @return
     */

    List<String> getTopNumChannel(Date startTime, Date endTime, Integer topNum);

    /**
     * 获取终端统计数据
     * 
     * @param statiscsTime
     * @return
     */
    List<TerminaStatisticsModel> getTerMinaStatisticsAnalysis(Date statiscsTime);

    /**
     * 获取官网访问数据
     * 
     * @param startTime
     * @param endTime
     * @return
     */
    List<AccessAnalysisModel> getPCAccessAnalysis(Date startTime, Date endTime);

}
