package com.efida.sports.quartz;

import com.efida.sports.biz.GudongEnrollService;
import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.IApplyInfoService;
import com.efida.sports.util.SeqWorkerUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wangyan on 2018/9/14.
 */
@Component
public class GudongTask {

    private Logger logger  = LoggerFactory.getLogger(GudongTask.class);

    private final String             Cache_Gudong_Sync_status        = "sports_cache_gudongsync_task";

    @Autowired
    private IApplyInfoService iApplyInfoService;

    @Autowired
    private GudongEnrollService gudongEnrollService;
    
    @Autowired
    private CacheService              cacheSevcice;
    @Value("${runSycTask}")
    private Boolean                   runSycTask;


    /**
     * 定时同步数据到咕咚 30同步一次
     */
     @Scheduled(cron = "*/30 * * * * ?")
    public void synchronousData() {
         String status = cacheSevcice.get(Cache_Gudong_Sync_status);
         if (StringUtils.isNotBlank(status)) {
             return;
         }
         
      cacheSevcice.put(Cache_Gudong_Sync_status, SeqWorkerUtil.buildSingleId(), 300000);  
     int pageSize = 100;
     try{
         while(true){
             List<ApplyInfo> list = iApplyInfoService.synchronousData(pageSize);
             if(list==null||list.size()<1){
                 break;
             }       
              for (ApplyInfo applyInfo:list){
                 gudongEnrollService.notifyGudong(applyInfo);
             }     
          
              if(list.size()<pageSize){
                  break;
              }
         }
               
     }catch(Exception ex){
         logger.error("", ex);
         
     }finally{
         cacheSevcice.remove(Cache_Gudong_Sync_status);         
     }

    }

}
