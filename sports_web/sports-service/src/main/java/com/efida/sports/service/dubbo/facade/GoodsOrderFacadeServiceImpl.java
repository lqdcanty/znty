package com.efida.sports.service.dubbo.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.efida.sport.facade.model.DefaultResult;
import com.efida.sport.facade.model.GoodsOrderCountModel;
import com.efida.sport.facade.service.GoodsOrderFacadeService;
import com.efida.sports.mapper.GoodsOrderEntityMapper;

/**
 * 奖章统计
 * 
 * @author zengbo
 * @version $Id: GoodsOrderFacadeServiceImpl.java, v 0.1 2018年10月10日 下午3:15:55 zengbo Exp $
 */
@Service
public class GoodsOrderFacadeServiceImpl implements GoodsOrderFacadeService {

    private static Logger          logger = LoggerFactory
        .getLogger(GoodsOrderFacadeServiceImpl.class);

    @Autowired
    private GoodsOrderEntityMapper goodsOrderEntityMapper;

    @Override
    public DefaultResult<List<GoodsOrderCountModel>> queryGoodsOrderByDay(Date startTime,
                                                                          Date endTime,
                                                                          String isRepeat) {
        DefaultResult<List<GoodsOrderCountModel>> result = new DefaultResult<List<GoodsOrderCountModel>>();
        try {
            List<GoodsOrderCountModel> models = new ArrayList<GoodsOrderCountModel>();
            List<Map<String, Object>> list = goodsOrderEntityMapper.countMedalReceive(startTime,
                endTime, isRepeat);
            if (list != null && list.size() > 0) {
                for (Map<String, Object> map : list) {
                    GoodsOrderCountModel model = new GoodsOrderCountModel();
                    Date date = (Date) map.get("payTime");
                    Long total = (Long) map.get("count");
                    model.setDate(date);
                    model.setTotal(total);
                    models.add(model);
                }
            }
            result.setSuccess(true);
            result.setResultObj(models);
        } catch (Exception e) {
            logger.error("", e);
            result.setSuccess(false);
            result.setErrorMsg("查询失败");
        }
        return result;
    }

}
