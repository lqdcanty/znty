/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.service.dubbo.facade.cover;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.efida.sport.facade.model.RegisterTrendAnalysisModel;
import com.efida.sports.model.RegisterDayReportTrendModel;
import com.efida.sports.util.DateUtil;

/**
 * 
 * @author zoutao
 * @version $Id: ReportTrendModelCover.java, v 0.1 2018年8月30日 上午12:18:20 zoutao Exp $
 */
public class ReportTrendModelCover {
    /**
     * 
     * 将数据库中查询出得model装换成趋势分析对象
     * @param models
     * @return
     */
    public static List<RegisterTrendAnalysisModel> getTrendAnalysisModel(List<RegisterDayReportTrendModel> models,
                                                                         Date startTime,
                                                                         Date endTime) {

        List<RegisterTrendAnalysisModel> list = new ArrayList<RegisterTrendAnalysisModel>();
        Date minDate = startTime;
        Date maxDate = endTime;
        if (endTime == null) {
            maxDate = Calendar.getInstance().getTime();
        }

        Map<String, RegisterTrendAnalysisModel> map = new HashMap<String, RegisterTrendAnalysisModel>();
        if (models != null && models.size() > 0) {
            for (RegisterDayReportTrendModel model : models) {
                RegisterTrendAnalysisModel analysisModel = new RegisterTrendAnalysisModel();
                analysisModel.setDate(model.getDate());
                analysisModel.setDateStr(DateUtil.formatDay(model.getDate()));
                analysisModel.setNewRegister(model.getNewRegister());
                analysisModel.setTotalRregister(model.getTotalRregister());
                map.put(analysisModel.getDateStr(), analysisModel);
            }
        }
        long totalRegister = 0L;
        while (true) {
            if (minDate.after(maxDate)) {
                break;
            }
            String formatDay = DateUtil.formatDay(minDate);
            if (map.containsKey(formatDay)) {
                RegisterTrendAnalysisModel model = map.get(formatDay);
                totalRegister = model.getTotalRregister();
                list.add(model);
            } else {
                RegisterTrendAnalysisModel analysisModel = new RegisterTrendAnalysisModel();
                analysisModel.setDate(minDate);
                analysisModel.setDateStr(DateUtil.formatDay(minDate));
                analysisModel.setNewRegister(0);
                analysisModel.setTotalRregister(totalRegister);
                list.add(analysisModel);
            }
            minDate = DateUtil.getNextDay(minDate);
        }
        return list;
    }

}
