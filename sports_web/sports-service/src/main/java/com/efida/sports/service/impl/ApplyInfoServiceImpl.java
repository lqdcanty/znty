package com.efida.sports.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sport.admin.facade.model.SpMatchAllInfoModel;
import com.efida.sport.admin.facade.model.SpMatchModel;
import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.mapper.ApplyInfoMapper;
import com.efida.sports.model.MessageInfoModel;
import com.efida.sports.service.IApplyInfoService;
import com.efida.sports.service.dubbo.intergration.SpMatchFacadeClient;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zoutao
 * @since 2018-05-18
 */
@Service
public class ApplyInfoServiceImpl extends ServiceImpl<ApplyInfoMapper, ApplyInfo>
                                  implements IApplyInfoService {

    @Autowired
    private SpMatchFacadeClient         spMatchFacadeClient;

    @Autowired
    private ApplyInfoMapper             applyMapper;

    @Autowired
    private UcenterRegisterFacadeClient ucenterRegisterFacadeClient;

    @Override
    public List<ApplyInfo> getApplyInfoByOrderCode(String orderCode) {

        Wrapper<ApplyInfo> wrapper = new EntityWrapper<ApplyInfo>();
        wrapper.eq("pay_order_code", orderCode);
        List<ApplyInfo> infos = selectList(wrapper);
        return infos;
    }

    @Override
    public void applySuccess(String orderCode) {
        applyMapper.applySuccess(orderCode);
    }

    @Override
    public void applySuccess(String orderCode, Date applyTime) {
        ApplyInfo info = new ApplyInfo();
        info.setPayOrderCode(orderCode);
        info.setApplyTime(applyTime);
        applyMapper.updateApplySuccess(info);
    }

    @Override
    public Long getApplyCountByEventCode(String eventCode) {
        return applyMapper.getApplyCountByEventCode(eventCode);
    }

    @Override
    public List<ApplyInfo> selectApplyInfoByDate(String sendStatus) {
        if ("fail".equals(sendStatus)) {
            return applyMapper.queryBeforeDayFailData();
        }
        return applyMapper.queryBeforeDayData();
        //        Wrapper<ApplyInfo> wrapper = new EntityWrapper<ApplyInfo>();
        //        if (StringUtils.isNotBlank(sendStatus)) {
        //            wrapper.eq("send_status", sendStatus);
        //        }
        //        List<ApplyInfo> infos = selectList(wrapper);
        //        return infos;
    }

    @Override
    public void updateSendStatus(String applyCode, String sendStatus) {
        ApplyInfo info = new ApplyInfo();
        info.setApplyCode(applyCode);
        info.setSendStatus(sendStatus);
        applyMapper.updateSendStatus(info);
    }

    public List<ApplyInfo> selectUnSycInfos(int pageNumber, int pageSize) {

        Map<String, Object> map = new HashMap<String, Object>();

        int start = (pageNumber - 1) * pageSize;
        map.put("start", start);
        map.put("limit", pageSize);

        List<ApplyInfo> items = this.applyMapper.selectUnSycInfos(map);

        return items;
    }

    @Override
    public List<ApplyInfo> getApplyInfosByAassetoidAndMatchCode(String aassettoId,
                                                                String matchCode) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aassettoId", aassettoId);
        map.put("matchCode", matchCode);
        List<ApplyInfo> infos = applyMapper.getApplyInfosByAassetoidAndMatchCode(map);

        return infos;
    }

    @Override
    public List<MessageInfoModel> selectApplyInfo(String registerCode, String applyStatus,
                                                  int pageNumber, int pageSize) {

        Map<String, Object> map = new HashMap<String, Object>();
        int start = (pageNumber - 1) * pageSize;
        map.put("start", start);
        map.put("limit", pageSize);
        map.put("registerCode", registerCode);
        map.put("applyStatus", applyStatus);

        List<String> ids = new ArrayList<String>();
        List<MessageInfoModel> list = new ArrayList<MessageInfoModel>();
        List<ApplyInfo> applyInfos = applyMapper.selectApplyInfo(map);
        if (applyInfos != null && applyInfos.size() > 0) {
            for (ApplyInfo info : applyInfos) {
                ids.add(info.getId().toString());
                SpMatchAllInfoModel item = spMatchFacadeClient.getItemDetail(info.getSiteCode(),
                    info.getGroupEventCode());
                if (item == null) {
                    continue;
                }
                SpMatchModel model = item.getMatchInfo();
                if (model != null) {
                    info.setPoster(model.getPoster());
                }
                //封装一下返回信息
                list.add(MessageInfoModel.getVo(info));
            }
            if (ids != null && ids.size() > 0) {
                updateIsRead(registerCode, applyStatus, "1", "0");
            }
        }
        return list;
    }

    @Override
    public Integer updateIsRead(String registerCode, String applyStatus, String oldIsRead,
                                String newIsRead) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("registerCode", registerCode);
        map.put("applyStatus", applyStatus);
        map.put("oldIsRead", oldIsRead);
        map.put("newIsRead", newIsRead);

        return applyMapper.updateIsRead(map);
    }

    @Override
    public Integer selectIsReadNum(String registerCode, String applyStatus, String isRead) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("registerCode", registerCode);
        map.put("applyStatus", applyStatus);
        map.put("isRead", isRead);

        return applyMapper.selectIsReadNum(map);
    }

    @Override
    public List<ApplyInfo> queryGameingData() {
        return applyMapper.queryBeforeDayData();
    }

    @Override
    public ApplyInfo getApplyInfoIdempotentId(String idempotentId) {
        Wrapper<ApplyInfo> wrapper = new EntityWrapper<ApplyInfo>();
        wrapper.eq("idempotent_id", idempotentId);
        return selectOne(wrapper);
    }

    @Override
    public List<ApplyInfo> queryApplyInfoByPhoneAndMachInfo(String matchCode, String siteCode,
                                                            Set<String> phones) {

        if (StringUtils.isEmpty(matchCode) || StringUtils.isEmpty(siteCode)
            || CollectionUtils.isEmpty(phones)) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("matchCode", matchCode);
        map.put("siteCode", siteCode);
        map.put("phones", phones);

        return this.applyMapper.querySuccessApplyInfoByPhoneAndMachInfo(map);
    }

    @Override
    public List<ApplyInfo> getApplyInfosByOrderCodes(List<String> orderCodes) {

        return this.applyMapper.getApplyInfosByOrderCodes(orderCodes);
    }

    @Override
    public List<ApplyInfo> applyInfosByidempotent(String idempotent) {
        Wrapper<ApplyInfo> wrapper = new EntityWrapper<ApplyInfo>();
        wrapper.eq("idempotent_id", idempotent);
        wrapper.orderBy("gmt_modified", false);
        return selectList(wrapper);
    }

    @Override
    public List<String> selectRepetitionIdempotentId(int limit) {

        return applyMapper.selectRepetitionIdempotentId(limit);
    }

    public List<ApplyInfo> selectInfosByRegisterCodeIsNull(int startIndex, int limit) {
        return applyMapper.selectInfosByRegisterCodeIsNull(startIndex, limit);
    }

    @Override
    public void repairRegisterChannelCode4Apply(RegisterModel register) {
        if (register == null) {
            return;
        }
        ApplyInfo info = getApplyInfoByRegisterCodeAndDmp(register.getRegisterCode());
        if (info == null) {
            return;
        }
        register.setChannelCode(info.getUnitCode());
        ucenterRegisterFacadeClient.updateRegsiterInfo(register);
    }

    private ApplyInfo getApplyInfoByRegisterCodeAndDmp(String registerCode) {

        Wrapper<ApplyInfo> wrapper = new EntityWrapper<ApplyInfo>();
        wrapper.eq("channel_code", "dmp");
        wrapper.eq("register_code", registerCode);
        return selectOne(wrapper);
    }

    /**
     * 查询咕咚报名成功同步失败的报名信息
     * @return
     */
    public List<ApplyInfo> synchronousData(int pageSize) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("limit", pageSize);
        return applyMapper.selectSynchronousData(map);
    }

    /**
     * 修改咕咚报名成功同步状态
     *
     * @param apply
     */
    public void updateSynchronousData(ApplyInfo apply) {
        applyMapper.updateSynchronousData(apply);
    }


    @Override
    public Long getApplyCountByEventCodeAndPhone(String eventCode,String phone) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("eventCode", eventCode);
        map.put("phone", phone);
        return applyMapper.getApplyCountByEventCodeAndPhone(map);
    }

}
