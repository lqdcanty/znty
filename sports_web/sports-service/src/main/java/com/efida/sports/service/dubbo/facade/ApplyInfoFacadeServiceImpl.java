/**
 * 
 */
package com.efida.sports.service.dubbo.facade;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.efida.sport.admin.facade.exception.BusinessException;
import com.efida.sport.facade.model.ApplyInfoModel;
import com.efida.sport.facade.model.DefaultResult;
import com.efida.sport.facade.service.ApplyInfoFacadeService;
import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.entity.LeaderInfo;
import com.efida.sports.entity.Player;
import com.efida.sports.service.IPayOrderService;
import com.efida.sports.service.dubbo.facade.cover.ApplyInfoCover;
import com.efida.sports.service.dubbo.facade.cover.LeaderCover;
import com.efida.sports.service.dubbo.facade.cover.PlayerCover;

/**
 * @author Administrator
 *
 */
@Service
public class ApplyInfoFacadeServiceImpl implements ApplyInfoFacadeService {

    private static Logger    logger = LoggerFactory.getLogger(ApplyInfoFacadeServiceImpl.class);
    @Autowired
    private IPayOrderService payOrderService;

    @Override
    public DefaultResult<Boolean> syncApplyInfo(ApplyInfoModel applyInfoModel) {

        DefaultResult<Boolean> dr = new DefaultResult<Boolean>();
        try {
            ApplyInfo applyInfo = ApplyInfoCover.model2endity(applyInfoModel);
            List<Player> players = PlayerCover.models2entitys(applyInfoModel.getPlayers());
            LeaderInfo learnInfo = LeaderCover.getLearnInfo(applyInfoModel);
            String str = payOrderService.createDmpApply(applyInfo, learnInfo, players);
            dr.setResultObj(true);
            dr.setErrorMsg(str);
            dr.setSuccess(true);
        } catch (BusinessException e) {
            dr.setResultObj(false);
            dr.setSuccess(false);
            dr.setErrorMsg(e.getMessage());
            logger.error("同步失败,unitCode:" + applyInfoModel.getUnitCode() + ",idempotentId:"
                         + applyInfoModel.getIdempotentId(),
                e);
        } catch (Exception e) {
            dr.setResultObj(false);
            dr.setSuccess(false);
            dr.setErrorMsg("同步失败" + e.getMessage());
            logger.error("同步失败,unitCode:" + applyInfoModel.getUnitCode() + ",idempotentId:"
                         + applyInfoModel.getIdempotentId(),
                e);
        }
        return dr;

    }

}
