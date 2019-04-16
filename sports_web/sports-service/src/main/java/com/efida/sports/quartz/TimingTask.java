package com.efida.sports.quartz;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.efida.sports.service.CacheService;
import com.efida.sports.service.IRegisterDayReportService;
import com.efida.sports.util.SeqWorkerUtil;

@Component
public class TimingTask {

    private final String              CACHE_REGISTER_STATUS        = "cache_regitser_status";
    private final String              CACHE_USER_STATUS            = "cache_user_status";

    private final String              CACHE_REGITSER_REPORT_SECOND = "znyd_cache_regitser_report_second";

    private final String              CACHE_REGITSER_REPORT_MIN    = "znyd_cache_regitser_report_min";
    @Autowired
    private CacheService              cacheSevcice;
    @Autowired
    private IRegisterDayReportService dayReportService;

    private static Logger             logger                       = LoggerFactory
        .getLogger(TimingTask.class);

    @Value("${runSycTask}")
    private Boolean                   runSycTask;

    //    @Scheduled(cron = "0 0/10 * * * ?")
    //    public void timingPutRegister2Cache() {
    //        if (!runSycTask) {
    //            return;
    //        }
    //        String status = cacheSevcice.get(CACHE_REGISTER_STATUS);
    //        if (StringUtils.isNotBlank(status)) {
    //            return;
    //        }
    //        cacheSevcice.put(CACHE_REGISTER_STATUS, SeqWorkerUtil.buildSingleId(), 720000);
    //        int pageSize = 1000;
    //        int current = 1;
    //        while (true) {
    //            Page<Register> page = new Page<Register>(current, pageSize, "id", false);
    //            //TODO: 修改为按ID倒序排列，或者按修改时间倒序排列chauxn
    //            Page<Register> selectPage = registerService.selectPage(page);
    //            List<Register> records = selectPage.getRecords();
    //            if (records == null) {
    //                break;
    //            }
    //            for (Register register : records) {
    //                cacheSevcice.putObj(Constants.REGISTER_CODE_KEY + register.getRegisterCode(),
    //                    register, 1000 * 60 * 30);
    //                if (StringUtils.isNotBlank(register.getAccount())) {
    //                    cacheSevcice.putObj(Constants.REGISTER_PHONE_KEY + register.getAccount(),
    //                        register, 1000 * 60 * 30);
    //                }
    //            }
    //            if (records.size() < pageSize) {
    //                break;
    //            }
    //            //最多加载10页 10000用户热点进入缓存
    //            if (current >= 10) {
    //                break;
    //            }
    //            current++;
    //
    //        }
    //        cacheSevcice.remove(CACHE_REGISTER_STATUS);
    //
    //    }
    //
    //    /**
    //     * 定时将user缓存到redis中
    //     */
    //    @Scheduled(cron = "0 0/10 * * * ?")
    //    public void timingPutUser2Cache() {
    //        if (!runSycTask) {
    //            return;
    //        }
    //        String status = cacheSevcice.get(CACHE_USER_STATUS);
    //        if (StringUtils.isNotBlank(status)) {
    //            return;
    //        }
    //        cacheSevcice.put(CACHE_USER_STATUS, SeqWorkerUtil.buildSingleId(), 720000);
    //        int pageSize = 1000;
    //        int current = 1;
    //        while (true) {
    //            Page<Users> page = new Page<Users>(current, pageSize, "id", false);
    //            //TODO：需要更改为id倒序加载或者修改时间倒序加载，加载热点数据
    //            Page<Users> selectPage = userService.selectPage(page);
    //            List<Users> records = selectPage.getRecords();
    //            if (records == null) {
    //                break;
    //            }
    //            for (Users user : records) {
    //                cacheSevcice.putObj(
    //                    Constants.USER_OPEN_ID_KEY + user.getAppId() + "_" + user.getOpenId(), user,
    //                    1000 * 60 * 30);
    //            }
    //            if (records.size() < pageSize) {
    //                break;
    //            }
    //            //只加载10页
    //            if (current >= 10) {
    //                break;
    //            }
    //            current++;
    //        }
    //        cacheSevcice.remove(CACHE_USER_STATUS);
    //    }

    // 每晚一点 隔5分钟执行
    @Scheduled(cron = "0 0/5 1 * * ?")
    public void createRegisterDayReportMin() {
        if (!runSycTask) {
            return;
        }
        logger.info("开始执行生成用户日报表任务");
        String status = cacheSevcice.get(CACHE_REGITSER_REPORT_MIN);
        if (StringUtils.isNotBlank(status)) {
            return;
        }
        cacheSevcice.put(CACHE_REGITSER_REPORT_MIN, SeqWorkerUtil.buildSingleId(), 720000);
        dayReportService.batchCreateDailyReport();
        cacheSevcice.remove(CACHE_REGITSER_REPORT_MIN);
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void createRegisterDayReport() {
        if (!runSycTask) {
            return;
        }
        String status = cacheSevcice.get(CACHE_REGITSER_REPORT_SECOND);
        if (StringUtils.isNotBlank(status)) {
            return;
        }
        cacheSevcice.put(CACHE_REGITSER_REPORT_SECOND, SeqWorkerUtil.buildSingleId(), 720000);
        dayReportService.createResport();
        cacheSevcice.remove(CACHE_REGITSER_REPORT_SECOND);
    }

}
