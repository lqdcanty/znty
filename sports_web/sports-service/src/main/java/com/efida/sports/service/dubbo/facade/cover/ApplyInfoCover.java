/**
 * 
 */
package com.efida.sports.service.dubbo.facade.cover;

import java.util.Date;

import com.efida.sport.facade.model.ApplyInfoModel;
import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.enums.ApplyStatusEnum;
import com.efida.sports.enums.SendStatusEnum;

/**
 * @author Administrator
 *
 */
public class ApplyInfoCover {

    public static ApplyInfo model2endity(ApplyInfoModel model) {
        if (model == null) {
            return null;
        }
        ApplyInfo apply = new ApplyInfo();
        apply.setApplyAmount(model.getApplyAmount());
        apply.setApplyStatus(ApplyStatusEnum.SUCCESS.getCode());
        apply.setApplyTime(model.getApplyTime());
        apply.setChannelCode("dmp");
        apply.setEventCode(model.getEventCode());
        apply.setEventEndTime(model.getEventEndTime());
        apply.setEventName(model.getEventName());
        apply.setEventStartTime(model.getEventStartTime());
        apply.setExpireTime(new Date());
        apply.setGameCode(model.getGameCode());
        apply.setGameName(model.getGameName());
        apply.setGmtCreate(new Date());
        apply.setGmtModified(new Date());
        apply.setGroupEventCode(model.getGroupEventCode());
        apply.setIsDelet(0);
        apply.setMatchCode(model.getMatchCode());
        apply.setMatchName(model.getMatchName());
        apply.setMatchGroupCode(model.getMatchGroupCode());
        apply.setMatchGroupName(model.getMatchGroupName());
        apply.setSiteCode(model.getSiteCode());
        apply.setSiteName(model.getSiteName());
        apply.setUnitCode(model.getUnitCode());
        apply.setSync((byte) 1);
        apply.setSendStatus(SendStatusEnum.SUCCESS.getCode());
        apply.setEventType(model.getEventType());
        apply.setIdempotentId(model.getIdempotentId());
        apply.setRegistrationNum(model.getRegistrationNum());
        return apply;
    }

}
