package com.efida.sports.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sport.dmp.facade.model.UnitAccountDto;
import com.efida.sport.dmp.facade.result.DmpPageResult;
import com.efida.sport.facade.enums.RegTerminalEnum;
import com.efida.sport.facade.model.AccessAnalysisModel;
import com.efida.sport.facade.model.ChannelTrendAnalysisModel;
import com.efida.sport.facade.model.PagingResult;
import com.efida.sport.facade.model.RegisterStatisticsModel;
import com.efida.sport.facade.model.RegisterTrendAnalysisModel;
import com.efida.sport.facade.model.TerminaStatisticsModel;
import com.efida.sports.entity.RegisterDayReport;
import com.efida.sports.mapper.RegisterDayReportMapper;
import com.efida.sports.model.ChannelRegisterNumModel;
import com.efida.sports.model.RegisterDayReportTrendModel;
import com.efida.sports.service.IRegisterAccessLogService;
import com.efida.sports.service.IRegisterDayReportService;
import com.efida.sports.service.dubbo.facade.cover.ReportTrendModelCover;
import com.efida.sports.service.dubbo.intergration.UcenterLoginFacadeClient;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;
import com.efida.sports.service.dubbo.intergration.UnitFacadeClient;
import com.efida.sports.util.DateUtil;
import com.efida.sports.util.SeqWorkerUtil;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zoutao
 * @since 2018-08-29
 */
@Service
public class RegisterDayReportServiceImpl extends
                                          ServiceImpl<RegisterDayReportMapper, RegisterDayReport>
                                          implements IRegisterDayReportService {

    @Autowired
    private UnitFacadeClient            unitFacadeClient;
    @Autowired
    private UcenterLoginFacadeClient    ucenterLoginFacadeClient;
    @Autowired
    private RegisterDayReportMapper     reportMapper;
    @Autowired
    private IRegisterAccessLogService   accessLogService;

    @Autowired
    private UcenterRegisterFacadeClient registerFacadeClient;

    private static Logger               log = LoggerFactory
        .getLogger(RegisterDayReportServiceImpl.class);

    @Override
    public void batchCreateDailyReport() {
        Date now = Calendar.getInstance().getTime();
        //结束时间
        Date startTime = null;
        //1 查询最后一条生成的数据
        RegisterDayReport lastReport = getLastReport();
        //如果还不存在记录 将之前得记录补全
        if (lastReport == null) {
            RegisterModel register = registerFacadeClient.getFirstRegRegister();
            startTime = register.getRegTime();
        } else {
            startTime = lastReport.getReportDay();
        }
        while (true) {
            if (startTime.after(now)) {
                return;
            }
            createResport(startTime);
            startTime = DateUtil.getNextDay(startTime);
        }
    }

    @Override
    public void createResport() {
        createResport(new Date());
    }

    private void createResport(Date reportDate) {
        Date now = Calendar.getInstance().getTime();
        Date startTime = DateUtil.getStartTime(reportDate);
        Date endTime = DateUtil.getEndTime(reportDate);
        List<Map<String, Object>> list = registerFacadeClient
            .getRegisterGroupByRegTerminal(startTime, endTime);
        /**
         * 将之前出现的注册来源数据补全
         */
        HashSet<String> regSet = new HashSet<String>();
        List<String> regs = reportMapper.getRegTerminals(endTime);
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map : list) {
                String regTerminal = map.get("regTerminal").toString();
                if (RegTerminalEnum.DMP.getCode().equals(regTerminal)) {
                    continue;
                }
                regSet.add(regTerminal);
                RegisterDayReport report = getReportByRegTerminalAndDate(regTerminal, reportDate);
                if (report == null) {
                    report = new RegisterDayReport();
                    report.setGmtCreate(now);
                    report.setGmtModified(now);
                }
                long accessCount = 0;
                if (RegTerminalEnum.PC.getCode().equals(regTerminal)) {
                    accessCount = accessLogService.getPCAccessCount(reportDate);
                }
                Long newRegister = (Long) map.get("newRegister");
                Long totalRegister = registerFacadeClient.getTotalRegisterByRegTerminal(regTerminal,
                    endTime);
                long weekActiveRegister = ucenterLoginFacadeClient.getWeekActiveRegister(reportDate,
                    regTerminal);
                long monthActiveRegister = ucenterLoginFacadeClient
                    .getMonthActiveRegister(reportDate, regTerminal);
                long loginRegister = ucenterLoginFacadeClient.getLoginRegister(reportDate,
                    regTerminal);
                report.setNewRegister(newRegister);
                report.setRegTerminal(regTerminal);
                report.setTotalRegister(totalRegister);
                report.setReportCode(SeqWorkerUtil.buildSingleId());
                report.setReportDay(reportDate);
                report.setWeekActiveRegister(weekActiveRegister);
                report.setMonthActiveRegister(monthActiveRegister);
                report.setLoginRegister(loginRegister);
                report.setReportTime(now);
                report.setAccessCount(accessCount);
                insertOrUpdate(report);
            }
        }
        if (regs != null && regs.size() > 0) {
            for (String reg : regs) {
                if (reg == null || regSet.contains(reg)) {
                    continue;
                }
                RegisterDayReport report = getReportByRegTerminalAndDate(reg, reportDate);
                if (report == null) {
                    report = new RegisterDayReport();
                    report.setGmtCreate(now);
                    report.setGmtModified(now);
                }
                Long newRegister = 0L;
                Long totalRegister = registerFacadeClient.getTotalRegisterByRegTerminal(reg,
                    endTime);
                long accessCount = 0;
                if (RegTerminalEnum.PC.getCode().equals(reg)) {
                    accessCount = accessLogService.getPCAccessCount(reportDate);
                }
                long loginRegister = ucenterLoginFacadeClient.getLoginRegister(reportDate, reg);
                long weekActiveRegister = ucenterLoginFacadeClient.getWeekActiveRegister(reportDate,
                    reg);
                long monthActiveRegister = ucenterLoginFacadeClient
                    .getMonthActiveRegister(reportDate, reg);
                report.setNewRegister(newRegister);
                report.setRegTerminal(reg);
                report.setTotalRegister(totalRegister);
                report.setReportCode(SeqWorkerUtil.buildSingleId());
                report.setWeekActiveRegister(weekActiveRegister);
                report.setMonthActiveRegister(monthActiveRegister);
                report.setLoginRegister(loginRegister);
                report.setAccessCount(accessCount);
                report.setReportTime(now);
                report.setReportDay(reportDate);
                report.setReportTime(now);
                insertOrUpdate(report);
            }
        }

        List<Map<String, Object>> channelList = registerFacadeClient
            .getRegisterGroupByChannelCode(startTime, endTime);
        HashSet<String> channelSet = new HashSet<String>();
        List<String> channelCodes = reportMapper.getChannelCods(endTime);
        if (channelList != null && channelList.size() > 0) {
            for (Map<String, Object> map : channelList) {
                String channel = map.get("channelCode").toString();
                RegisterDayReport report = getReportByChannelAndDate(channel, reportDate);
                if (report == null) {
                    report = new RegisterDayReport();
                    report.setGmtCreate(now);
                    report.setGmtModified(now);
                }
                channelSet.add(channel);
                Long newRegister = (Long) map.get("newRegister");
                Long totalRegister = registerFacadeClient.getTotalRegisterByRegChannel(channel,
                    endTime);
                report.setNewRegister(newRegister);
                report.setChannelCode(channel);
                report.setRegTerminal(RegTerminalEnum.DMP.getCode());
                report.setTotalRegister(totalRegister);
                report.setReportCode(SeqWorkerUtil.buildSingleId());
                report.setReportDay(reportDate);
                report.setWeekActiveRegister(0L);
                report.setMonthActiveRegister(0L);
                report.setLoginRegister(0L);
                report.setAccessCount(0L);
                report.setReportTime(now);
                insertOrUpdate(report);
            }
        }

        if (channelCodes != null && channelCodes.size() > 0) {
            for (String channel : channelCodes) {
                if (channel == null || channelSet.contains(channel)) {
                    continue;
                }
                RegisterDayReport report = getReportByChannelAndDate(channel, reportDate);
                if (report == null) {
                    report = new RegisterDayReport();
                    report.setGmtCreate(now);
                    report.setGmtModified(now);
                }
                Long newRegister = 0L;
                Long totalRegister = registerFacadeClient.getTotalRegisterByRegChannel(channel,
                    endTime);
                report.setNewRegister(newRegister);
                report.setChannelCode(channel);
                report.setRegTerminal(RegTerminalEnum.DMP.getCode());
                report.setTotalRegister(totalRegister);
                report.setReportCode(SeqWorkerUtil.buildSingleId());
                report.setReportDay(reportDate);
                report.setWeekActiveRegister(0L);
                report.setMonthActiveRegister(0L);
                report.setLoginRegister(0L);
                report.setAccessCount(0L);
                report.setReportTime(now);
                insertOrUpdate(report);
            }
        }

    }

    private RegisterDayReport getReportByChannelAndDate(String channel, Date reportDate) {
        Wrapper<RegisterDayReport> wrapper = new EntityWrapper<RegisterDayReport>();
        String formatDay = DateUtil.formatDay(reportDate);
        wrapper.eq("report_day", formatDay);
        wrapper.eq("channel_code", channel);
        return selectOne(wrapper);
    }

    private RegisterDayReport getReportByRegTerminalAndDate(String regType, Date reportDate) {
        Wrapper<RegisterDayReport> wrapper = new EntityWrapper<RegisterDayReport>();
        String formatDay = DateUtil.formatDay(reportDate);
        wrapper.eq("report_day", formatDay);
        wrapper.eq("reg_terminal", regType);
        return selectOne(wrapper);
    }

    private RegisterDayReport getLastReport() {
        Page<RegisterDayReport> page = new Page<RegisterDayReport>(1, 1, "id", false);
        Page<RegisterDayReport> selectPage = selectPage(page);
        List<RegisterDayReport> records = selectPage.getRecords();
        if (records == null || records.size() < 1) {
            return null;
        }
        return records.get(0);
    }

    @Override
    public RegisterStatisticsModel getRegisterStatistics() {
        RegisterStatisticsModel model = new RegisterStatisticsModel();
        long sevenActiveRegitsers = ucenterLoginFacadeClient.activeRegitsers(7);
        double sevenAvgActive = sevenActiveRegitsers * 1.0 / 7;
        long thirtyActiveRegitsers = ucenterLoginFacadeClient.activeRegitsers(30);
        long newRegsiyer = getNewRegsiyer(7);
        double avgNewRegister = newRegsiyer * 1.0 / 7;
        long sevenAvgNewRegister = (long) avgNewRegister;
        String sevenRetention = "-";
        long totalRegister = getTotalRegister();
        long retentionRegisters = ucenterLoginFacadeClient.getRetentionRegisters(7);
        if (retentionRegisters != 0) {
            double retention = retentionRegisters * 1.0 / newRegsiyer * 1.0 / 7 * 100;
            long reten = (long) retention;
            if (reten == retention) {
                sevenRetention = reten + "%";
            } else {
                sevenRetention = new BigDecimal(retention).setScale(2, BigDecimal.ROUND_HALF_UP)
                    .doubleValue() + "%";
            }
        }
        //总用户数
        model.setTotalRegister(totalRegister);
        //七日活跃用户
        model.setSevenActiveRegitsers(sevenActiveRegitsers);
        //三十日活跃用户
        model.setThirtyActiveRegitsers(thirtyActiveRegitsers);
        //七日平均活跃用户数
        model.setSevenAvgActive(sevenAvgActive);

        //七日留存率
        model.setSevenRetention(sevenRetention);
        //七日平均新增用户数
        model.setSevenAvgNewRegister(sevenAvgNewRegister);

        return model;
    }

    private long getTotalRegister() {
        return registerFacadeClient.getTotalRegister();
    }

    private long getNewRegsiyer(int days) {
        Date startTime = DateUtil.getDayAgoTime(days);
        return reportMapper.getNewRegsiyer(startTime);
    }

    @Override
    public List<RegisterTrendAnalysisModel> getRegisterGrowthTrend(Date startTime, Date endTime) {
        if (startTime == null) {
            startTime = DateUtil.getDayAgoTime(7);
        }
        List<RegisterDayReportTrendModel> list = reportMapper.selectRegisterTrend(startTime,
            endTime);
        return ReportTrendModelCover.getTrendAnalysisModel(list, startTime, endTime);
    }

    @Override
    public PagingResult<RegisterTrendAnalysisModel> getRegisterGrowthTrend(int currentPage,
                                                                           int pageSize,
                                                                           Date startTime,
                                                                           Date endTime) {
        if (startTime == null) {
            startTime = DateUtil.getDayAgoTime(7);
        }
        //        Page<RegisterDayReportTrendModel> page = new Page<RegisterDayReportTrendModel>(currentPage,
        //            pageSize);
        if (startTime != null) {
            startTime = DateUtil.getStartTime(startTime);
        }
        if (endTime != null) {
            endTime = DateUtil.getEndTime(endTime);
        }
        //        List<RegisterDayReportTrendModel> list = reportMapper.selectPageRegisterTrend(startTime, endTime);
        //        page.setRecords(list);
        List<RegisterDayReportTrendModel> list = reportMapper.selectRegisterTrend(startTime,
            endTime);
        long daysBetween = DateUtil.daysBetween(startTime, endTime);
        int startIndex = (currentPage - 1) * pageSize;
        int toIndex = currentPage * pageSize;
        log.info("分页获取用户增长趋势,当前页数：{},起始坐标：{},结束坐标：{}", currentPage, startIndex, toIndex);
        List<RegisterTrendAnalysisModel> models = ReportTrendModelCover.getTrendAnalysisModel(list,
            startTime, endTime);
        List<RegisterTrendAnalysisModel> subList = new ArrayList<RegisterTrendAnalysisModel>();
        if (models.size() <= toIndex) {
            toIndex = models.size();
        }
        subList = models.subList(startIndex, toIndex);
        PagingResult<RegisterTrendAnalysisModel> pagingResult = new PagingResult<RegisterTrendAnalysisModel>(
            subList, daysBetween, currentPage, pageSize);
        return pagingResult;
    }

    private Map<String, String> getAllUnit() {
        HashMap<String, String> map = new HashMap<String, String>();
        DmpPageResult<UnitAccountDto> page = unitFacadeClient.getList(null, 1, 1000);
        List<UnitAccountDto> list = page.getList();
        if (list != null && list.size() > 0) {
            for (UnitAccountDto unitAccountDto : list) {
                map.put(unitAccountDto.getUnitCode(), unitAccountDto.getUnitName());
            }
        }
        return map;

    }

    @Override
    public List<String> getTopNumChannel(Date startTime, Date endTime, Integer topNum) {
        if (startTime != null) {
            startTime = DateUtil.getStartTime(startTime);
        } else {
            startTime = DateUtil.getDayAgoTime(7);
        }
        if (endTime != null) {
            endTime = DateUtil.getEndTime(endTime);
        }
        List<String> channelCodes = reportMapper.getTopNumChannel(startTime, endTime, topNum);
        return channelCodes;
    }

    @Override
    public List<RegisterTrendAnalysisModel> channelAnalysis(Date startTime, Date endTime,
                                                            List<String> channelCodes) {

        if (startTime != null) {
            startTime = DateUtil.getStartTime(startTime);
        } else {
            startTime = DateUtil.getDayAgoTime(7);
        }
        if (endTime != null) {
            endTime = DateUtil.getEndTime(endTime);
        }
        List<RegisterDayReportTrendModel> list = reportMapper.selectChannelAnalysis(startTime,
            endTime, channelCodes);

        Map<String, Long> channelMap = new HashMap<String, Long>();
        List<ChannelRegisterNumModel> channelRegisterNum = reportMapper.getChannelRegisterNum();
        long totalRegister = 0;
        if (channelRegisterNum != null && channelRegisterNum.size() > 0) {
            for (ChannelRegisterNumModel model : channelRegisterNum) {
                Long channelNum = model.getTotalRegister() == null ? 0L : model.getTotalRegister();
                totalRegister += channelNum;
                channelMap.put(model.getChannelCode(), channelNum);
            }
        }

        return handleChannelAnalysis(list, channelMap, totalRegister, channelCodes);
    }

    private List<RegisterTrendAnalysisModel> handleChannelAnalysis(List<RegisterDayReportTrendModel> list,
                                                                   Map<String, Long> channelMap,
                                                                   long totalRegister,
                                                                   List<String> channelCodes) {
        List<RegisterTrendAnalysisModel> models = new ArrayList<RegisterTrendAnalysisModel>();

        Map<String, String> allUnit = new HashMap<String, String>();
        if (channelCodes == null || channelCodes.size() < 1) {
            DmpPageResult<UnitAccountDto> page = unitFacadeClient.getList(null, 1, 1000);
            List<UnitAccountDto> units = page.getList();
            channelCodes = new ArrayList<String>();
            if (units != null && units.size() > 0) {
                for (UnitAccountDto unitAccountDto : units) {
                    channelCodes.add(unitAccountDto.getUnitCode());
                    allUnit.put(unitAccountDto.getUnitCode(), unitAccountDto.getUnitName());
                }
            }
        } else {
            allUnit = getAllUnit();
        }
        Set<String> channelSet = new HashSet<String>();

        if (list != null && list.size() > 0) {
            for (RegisterDayReportTrendModel item : list) {
                String channelCode = item.getChannelCode();
                if (!allUnit.containsKey(channelCode)) {
                    continue;
                }
                channelSet.add(channelCode);
                RegisterTrendAnalysisModel model = new RegisterTrendAnalysisModel();
                model.setChannelCode(channelCode);
                model.setChannelName(allUnit.get(channelCode));
                model.setNewRegister(item.getNewRegister());
                models.add(model);
            }
        }
        for (String channelCode : channelCodes) {
            if (channelSet.contains(channelCode)) {
                continue;
            }
            RegisterTrendAnalysisModel model = new RegisterTrendAnalysisModel();
            model.setChannelCode(channelCode);
            model.setChannelName(allUnit.get(channelCode));
            model.setNewRegister(0);
            models.add(model);
        }
        if (models.size() > 0) {
            for (RegisterTrendAnalysisModel model : models) {
                String channelCode = model.getChannelCode();
                long channelRegisterNum = 0l;
                if (channelMap.containsKey(channelCode)) {
                    channelRegisterNum = channelMap.get(channelCode);
                }
                if (channelRegisterNum == 0) {
                    model.setProportion("0%");
                } else {
                    double retention = channelRegisterNum * 1.0 / totalRegister * 100;
                    long reten = (long) retention;
                    if (reten == retention) {
                        model.setProportion(reten + "%");
                    } else {
                        model.setProportion(new BigDecimal(retention)
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "%");
                    }
                }

            }
        }

        return models;

    }

    @Override
    public List<ChannelTrendAnalysisModel> channelTrendAnalysis(Date startTime, Date endTime,
                                                                List<String> channelCodes) {

        if (startTime != null) {
            startTime = DateUtil.getStartTime(startTime);
        } else {
            startTime = DateUtil.getDayAgoTime(7);
        }
        if (endTime != null) {
            endTime = DateUtil.getEndTime(endTime);
        }
        List<RegisterDayReportTrendModel> list = reportMapper.channelTrendAnalysis(startTime,
            endTime, channelCodes);

        return handlechannelTrendAnalysis(list, startTime, endTime, channelCodes);
    }

    /**
     * 处理渠道用户增长趋势
     * 补全数据
     * 
     * @param list
     * @param startTime
     * @param endTime
     * @return
     */
    private List<ChannelTrendAnalysisModel> handlechannelTrendAnalysis(List<RegisterDayReportTrendModel> list,
                                                                       Date startTime, Date endTime,
                                                                       List<String> channelCodes) {

        List<ChannelTrendAnalysisModel> models = new ArrayList<ChannelTrendAnalysisModel>();

        Map<String, String> allUnit = new HashMap<String, String>();
        if (channelCodes == null || channelCodes.size() < 1) {
            DmpPageResult<UnitAccountDto> page = unitFacadeClient.getList(null, 1, 1000);
            List<UnitAccountDto> units = page.getList();
            channelCodes = new ArrayList<String>();
            if (units != null && units.size() > 0) {
                for (UnitAccountDto unitAccountDto : units) {
                    channelCodes.add(unitAccountDto.getUnitCode());
                    allUnit.put(unitAccountDto.getUnitCode(), unitAccountDto.getUnitName());
                }
            }
        } else {
            allUnit = getAllUnit();
        }

        Map<String, RegisterTrendAnalysisModel> itemMap = new HashMap<String, RegisterTrendAnalysisModel>();
        if (list != null && list.size() > 0) {
            for (RegisterDayReportTrendModel item : list) {
                Date reportDay = item.getDate();
                String channelCode = item.getChannelCode();
                if (!allUnit.containsKey(channelCode)) {
                    continue;
                }
                String channelName = allUnit.get(channelCode);
                String key = DateUtil.formatDay(reportDay) + "_" + channelCode;
                RegisterTrendAnalysisModel model = new RegisterTrendAnalysisModel();
                model.setNewRegister(item.getNewRegister());
                model.setTotalRregister(item.getTotalRregister());
                model.setChannelCode(channelCode);
                model.setChannelName(channelName);
                model.setDate(reportDay);
                itemMap.put(key, model);
            }
        }

        while (true) {
            if (endTime == null) {
                endTime = Calendar.getInstance().getTime();
            }
            if (startTime.after(endTime)) {
                break;
            }
            List<RegisterTrendAnalysisModel> trendModels = new ArrayList<RegisterTrendAnalysisModel>();
            ChannelTrendAnalysisModel model = new ChannelTrendAnalysisModel();

            model.setDate(startTime);
            model.setDateStr(DateUtil.formatDay(startTime));
            for (String channelCode : channelCodes) {
                String key = DateUtil.formatDay(startTime) + "_" + channelCode;
                String channelName = allUnit.get(channelCode);
                if (itemMap.containsKey(key)) {
                    RegisterTrendAnalysisModel item = itemMap.get(key);
                    trendModels.add(item);
                } else {
                    RegisterTrendAnalysisModel item = new RegisterTrendAnalysisModel();
                    item.setChannelCode(channelCode);
                    item.setChannelName(channelName);
                    item.setDate(startTime);
                    item.setNewRegister(0);
                    trendModels.add(item);
                }
            }
            model.setTrendModels(trendModels);
            models.add(model);
            startTime = DateUtil.getNextDay(startTime);
        }
        return models;
    }

    @Override
    public List<AccessAnalysisModel> getPCAccessAnalysis(Date startTime, Date endTime) {
        if (startTime != null) {
            startTime = DateUtil.getStartTime(startTime);
        } else {
            startTime = DateUtil.getDayAgoTime(6);
        }
        if (endTime != null) {
            endTime = DateUtil.getEndTime(endTime);
        } else {
            endTime = DateUtil.getEndTime(new Date());
        }
        List<RegisterDayReport> registerDayReports = reportMapper.selectRegisterDayReport(startTime,
            endTime);
        return handlePCAccessAnalysis(registerDayReports, startTime, endTime);
    }

    //处理用户访问量数据
    private List<AccessAnalysisModel> handlePCAccessAnalysis(List<RegisterDayReport> list,
                                                             Date startTime, Date endTime) {

        List<AccessAnalysisModel> models = new ArrayList<AccessAnalysisModel>();
        Map<String, AccessAnalysisModel> itemMap = new HashMap<String, AccessAnalysisModel>();
        if (list != null && list.size() > 0) {
            for (RegisterDayReport item : list) {
                if (RegTerminalEnum.PC.getCode().equals(item.getRegTerminal())) {
                    Date reportDay = item.getReportDay();
                    String key = DateUtil.formatDay(reportDay);
                    AccessAnalysisModel model = new AccessAnalysisModel();
                    model.setAccessCount(item.getAccessCount() == null ? 0 : item.getAccessCount());
                    model.setDate(reportDay);
                    model.setDateStr(key);
                    itemMap.put(key, model);
                }
            }
        }
        while (true) {
            if (endTime == null) {
                endTime = Calendar.getInstance().getTime();
            }
            if (startTime.after(endTime)) {
                break;
            }
            String key = DateUtil.formatDay(startTime);
            if (itemMap.containsKey(key)) {
                models.add(itemMap.get(key));
                startTime = DateUtil.getNextDay(startTime);
                continue;
            }
            AccessAnalysisModel model = new AccessAnalysisModel();
            model.setAccessCount(0);
            model.setDate(startTime);
            model.setDateStr(key);
            models.add(model);
            startTime = DateUtil.getNextDay(startTime);
        }
        return models;
    }

    @Override
    public List<TerminaStatisticsModel> getTerMinaStatisticsAnalysis(Date statiscsTime) {
        Date startTime = DateUtil.getYesterdayStartTime();
        Date endTime = DateUtil.getYesterdayEndTime();
        if (statiscsTime != null) {
            startTime = DateUtil.getStartTime(statiscsTime);
            endTime = DateUtil.getEndTime(statiscsTime);
        }
        List<RegisterDayReport> registerDayReports = reportMapper.selectRegisterDayReport(startTime,
            endTime);

        return handleTerMinaStatisticsAnalysis(registerDayReports, statiscsTime);
    }

    private List<TerminaStatisticsModel> handleTerMinaStatisticsAnalysis(List<RegisterDayReport> list,
                                                                         Date statiscsTime) {
        List<TerminaStatisticsModel> models = new ArrayList<TerminaStatisticsModel>();
        Map<String, TerminaStatisticsModel> itemMap = new HashMap<String, TerminaStatisticsModel>();
        if (list != null && list.size() > 0) {
            for (RegisterDayReport item : list) {
                String regTerminal = item.getRegTerminal();
                if (StringUtils.isBlank(regTerminal)
                    || RegTerminalEnum.DMP.getCode().equals(regTerminal)) {
                    continue;
                }
                TerminaStatisticsModel model = new TerminaStatisticsModel();
                model.setActiveCount(
                    item.getWeekActiveRegister() == null ? 0 : item.getWeekActiveRegister());
                model.setDate(item.getReportDay());
                model.setDateStr(DateUtil.formatDay(item.getReportDay()));
                model.setRegCount(item.getNewRegister());
                model.setTerminaName(RegTerminalEnum.getDescByCode(item.getRegTerminal()));
                model.setTerminaType(item.getRegTerminal());
                itemMap.put(regTerminal, model);
            }
        }
        RegTerminalEnum[] values = RegTerminalEnum.values();
        for (RegTerminalEnum em : values) {
            String code = em.getCode();
            if (itemMap.containsKey(code)) {
                TerminaStatisticsModel terminaStatisticsModel = itemMap.get(code);
                models.add(terminaStatisticsModel);
                continue;
            }
            TerminaStatisticsModel model = new TerminaStatisticsModel();
            model.setActiveCount(0);
            model.setDate(statiscsTime);
            model.setDateStr(DateUtil.formatDay(statiscsTime));
            model.setRegCount(0);
            model.setTerminaName(em.getCname());
            model.setTerminaType(code);
            models.add(model);
        }

        return models;
    }

}
