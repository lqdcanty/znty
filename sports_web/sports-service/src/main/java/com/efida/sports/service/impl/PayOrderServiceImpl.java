package com.efida.sports.service.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.efida.easy.ucenter.facade.enums.TerminalEnum;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sport.admin.facade.enums.MatchRegEums;
import com.efida.sport.admin.facade.model.FromTypeModel;
import com.efida.sport.admin.facade.model.SpAthletesEnrollModel;
import com.efida.sport.admin.facade.model.SpGroupItemModel;
import com.efida.sport.admin.facade.model.SpMatchAllInfoModel;
import com.efida.sport.admin.facade.model.SpMatchGroupModel;
import com.efida.sport.admin.facade.model.SpMatchInfoModel;
import com.efida.sport.admin.facade.model.SpMatchModel;
import com.efida.sport.admin.facade.model.SpPlayingAreaModel;
import com.efida.sport.admin.facade.model.SpProjectTypeModel;
import com.efida.sport.facade.enums.RegTerminalEnum;
import com.efida.sports.config.WeichatConfig;
import com.efida.sports.constants.Constants;
import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.entity.ApplyPlayer;
import com.efida.sports.entity.LeaderInfo;
import com.efida.sports.entity.PayOrder;
import com.efida.sports.entity.Player;
import com.efida.sports.entity.PlayerEx;
import com.efida.sports.entity.SiteOrderDto;
import com.efida.sports.enums.ApplyStatusEnum;
import com.efida.sports.enums.EventTypeEnums;
import com.efida.sports.enums.MatchStatusEnum;
import com.efida.sports.enums.OrderStatusEnum;
import com.efida.sports.enums.OrderTypeEnum;
import com.efida.sports.enums.PayGateWayEnum;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.mapper.PayOrderMapper;
import com.efida.sports.model.MatchShortInfo;
import com.efida.sports.service.CacheService;
import com.efida.sports.service.IApplyInfoService;
import com.efida.sports.service.IApplyPlayerService;
import com.efida.sports.service.IGoodsOrderService;
import com.efida.sports.service.ILeaderInfoService;
import com.efida.sports.service.IPayOrderService;
import com.efida.sports.service.IPlayerService;
import com.efida.sports.service.MsgService;
import com.efida.sports.service.dubbo.intergration.SpAthletesFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpMatchFacadeClient;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;
import com.efida.sports.util.AmountUtils;
import com.efida.sports.util.DateUtil;
import com.efida.sports.util.GudongSynchronousDataUtil;
import com.efida.sports.util.RegexUtils;
import com.efida.sports.util.SeqWorkerUtil;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zoutao
 * @since 2018-05-18
 */
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder>
                                 implements IPayOrderService {

    private static Logger               log = LoggerFactory.getLogger(PayOrderServiceImpl.class);

    @Autowired
    private IApplyInfoService           applyInfoService;

    @Autowired
    private SpMatchFacadeClient         spMatchFacadeClient;

    @Autowired
    private UcenterRegisterFacadeClient ucenterRegisterFacadeClient;
    @Autowired
    private SpAthletesFacadeClient      spAthletesFacadeClient;

    @Autowired
    private WeichatConfig               weichatConfig;

    @Autowired
    private IPlayerService              playerService;

    @Autowired
    private MsgService                  msgService;

    @Autowired
    private UcenterRegisterFacadeClient ucenterRegsiterFacadeClient;

    @Autowired
    private CacheService                cacheService;
    @Autowired
    private PayOrderMapper              payOrderMapper;

    @Autowired
    private IPlayerService              iPlayerService;

    @Autowired
    private IApplyPlayerService         applyPlayerService;
    @Autowired
    private ILeaderInfoService          learnService;
    @Autowired
    private ILeaderInfoService          leaderInfoService;

    @Autowired
    private IGoodsOrderService          goodsOrderService;

    @Override
    public PayOrder getOrderByCode(String orderCode) {
        Wrapper<PayOrder> wrapper = new EntityWrapper<PayOrder>();
        wrapper.eq("order_code", orderCode);
        PayOrder payOrder = selectOne(wrapper);
        return payOrder;

    }

    @Override
    public void completePaySuccess(String orderCode, String trxNo, PayGateWayEnum payGetWay) {
        this.completePay(orderCode, trxNo, payGetWay);
        sendSuccessMessage(orderCode);
        try {
            final String orderCode2 = orderCode;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    GudongSynchronousDataUtil.notifyGudong(orderCode2);
                }
            }).start();
        } catch (Exception e) {
            log.info("同步咕咚报名订单{}信息报错{}", orderCode, e.toString());
        }
    }

    @Transactional
    private void completePay(String orderCode, String trxNo, PayGateWayEnum payGetWay) {
        PayOrder order = getOrderByCode(orderCode);
        if (OrderStatusEnum.SUCCESS.getCode().equals(order.getOrderStatus())) {
            return;
        }
        order.setGmtModified(Calendar.getInstance().getTime());
        if (payGetWay == PayGateWayEnum.WEICHAT_PAY) {
            order.setOrderReturnUrl(weichatConfig.redirectUri);
        }
        if (payGetWay != null) {
            order.setPayWayCode(payGetWay.getCode());
            order.setPayWayName(payGetWay.getCname());
        }
        order.setOrderStatus(OrderStatusEnum.SUCCESS.getCode());
        order.setTrxNo(trxNo);
        order.setPayTime(Calendar.getInstance().getTime());
        updateById(order);

        //商城订单
        if (OrderTypeEnum.mall.getCode().equals(order.getOrderType())) {
            this.goodsOrderService.paySuccess(orderCode, order.getPayTime(), payGetWay.getCode(),
                payGetWay.getCname());
        } else {
            //报名订单
            List<ApplyInfo> infos = applyInfoService.getApplyInfoByOrderCode(orderCode);
            String unitCode = "lantianlvye";
            for (ApplyInfo info : infos) {
                if (!unitCode.equals(info.getUnitCode())) {
                    continue;
                }
                List<Player> players = iPlayerService
                    .selectPlayerByApplyInfoCode(info.getApplyCode());
                if (CollectionUtils.isEmpty(players)) {
                    continue;
                }
                for (Player player : players) {
                    String str = player.getExtPro();
                    JSONObject json = null;
                    if (StringUtils.isEmpty(str)) {
                        json = new JSONObject();
                    } else {
                        json = (JSONObject) JSONObject.parse(str);
                    }
                    json.put("参赛验证码", getUniqueCode());
                    player.setExtPro(json.toJSONString());
                    this.iPlayerService.updateById(player);
                }
            }
            applyInfoService.applySuccess(orderCode, order.getPayTime());
        }
    }

    private String getUniqueCode() {

        String str = "SP";
        String base = "0123456789ABCDEF";
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            str += base.charAt(random.nextInt(base.length()));
        }
        return str;
    }

    /**
     * 发送成功消息
     * @param register
     * @param payOrder
     */
    @Async
    public void sendSuccessMessage(String orderCode) {
        try {
            PayOrder payOrder = getOrderDetail(orderCode);
            RegisterModel register = ucenterRegsiterFacadeClient
                .getRegsiterByRegisterCode(payOrder.getRegisterCode());
            List<ApplyInfo> infos = payOrder.getApplyInfos();
            String value = "";
            try {
                value = cacheService.get(Constants.MESSAGE_VERIFY_CODE + register.getAccount() + "_"
                                         + payOrder.getOrderCode());
            } catch (Exception e) {
                log.error("", e);
            }

            if (infos != null && infos.size() > 0 && !Constants.MESSAGE_TREU.equals(value)) {
                for (ApplyInfo info : infos) {
                    SpMatchAllInfoModel spmodel = spMatchFacadeClient
                        .getItemDetail(info.getSiteCode(), info.getGroupEventCode());
                    List<Player> players = iPlayerService
                        .selectPlayerByApplyInfoCode(info.getApplyCode());
                    if (players != null && players.size() > 0) {

                        Set<String> phones = new HashSet<String>();
                        boolean alreadSleep = false;
                        for (Player play : players) {
                            String partCode = "";
                            if (StringUtils.isBlank(play.getPlayerPhone())) {
                                continue;
                            }

                            if (phones.contains(play.getPlayerPhone())) {
                                continue;
                            }
                            //                            if (!alreadSleep) {
                            //                                try {
                            //                                    Thread.sleep(30000);
                            //                                    alreadSleep = true;
                            //                                } catch (InterruptedException e) {
                            //                                }
                            //
                            //                            }

                            if ("lantianlvye".equals(info.getUnitCode())) {
                                String extPro = play.getExtPro();
                                if (StringUtils.isNotBlank(extPro)) {
                                    JSONObject obj = JSON.parseObject(extPro);
                                    partCode = (String) obj.get("参赛验证码");
                                }
                            }
                            phones.add(play.getPlayerPhone());
                            msgService.sendSuccessMessage(info.getMatchName(),
                                info.getMatchGroupName(), play.getPlayerPhone(),
                                spmodel.getGroupItemInfo().getItemName(),
                                spmodel.getGroupItemInfo().getStartTime(),
                                spmodel.getPayAreaInfo().getFieldAddress(),
                                spmodel.getGroupItemInfo().getEndTime(), partCode);

                        }

                    }
                    cacheService.put(Constants.MESSAGE_VERIFY_CODE + register.getAccount() + "_"
                                     + payOrder.getOrderCode(),
                        Constants.MESSAGE_TREU, 1000 * 50);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public PayOrder getOrderDetail(String orderCode) {
        PayOrder order = getOrderByCode(orderCode);
        if (order != null) {
            List<ApplyInfo> applyInfos = applyInfoService.getApplyInfoByOrderCode(orderCode);
            order.setApplyInfos(applyInfos);
            List<Player> playes = null;
            if (applyInfos != null && applyInfos.size() > 0) {
                for (ApplyInfo applyInfo : applyInfos) {

                    //TODO: 优化为  直接查询赛场
                    if (!StringUtils.isEmpty(applyInfo.getSiteCode())) {
                        String address = spMatchFacadeClient
                            .getFieldAddress(applyInfo.getSiteCode());
                        applyInfo.setAddress(address);
                    } else {
                        applyInfo.setAddress("");
                    }

                }
                ApplyInfo applyInfo = applyInfos.get(0);
                playes = playerService.selectPlayerByApplyInfoCode(applyInfo.getApplyCode());

                if (EventTypeEnums.group.getCode().equals(applyInfo.getEventType())) {
                    LeaderInfo lead = leaderInfoService
                        .getLearnInfoByApplyCode(applyInfo.getApplyCode());
                    order.setLeaderInfo(lead);
                }
            }
            order.setPlayers(playes);
        }
        return order;
    }

    @Override
    @Transactional
    public PayOrder cancelOrder(String orderCode, String registerCode) {
        PayOrder order = getOrderByCode(orderCode);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!registerCode.equals(order.getRegisterCode())) {
            throw new BusinessException("该订单不属于当前用户");
        }
        if (OrderStatusEnum.SUCCESS.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException("该订单已成功报名不允许取消");
        }
        order.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        updateById(order);
        List<ApplyInfo> applyInfo = applyInfoService.getApplyInfoByOrderCode(orderCode);
        if (applyInfo != null && applyInfo.size() > 0) {
            for (ApplyInfo applyInfo2 : applyInfo) {
                applyInfo2.setExpireTime(Calendar.getInstance().getTime());
            }
            applyInfoService.updateBatchById(applyInfo);
        }

        return order;
    }

    /**
     *   事务操作顺序
    register
    
    order,
    
    player
    appply
    leader
     * @see com.efida.sports.service.IPayOrderService#addApplyOrder(java.util.List, java.lang.String, com.efida.sports.entity.Player, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    @Transactional
    public PayOrder addApplyOrder(List<String> itemCodes, String fileidCode, Player player,
                                  String registerCode, String ip, String referer,
                                  RegTerminalEnum terminal) {
        final String orderCode = SeqWorkerUtil.generateTimeSequence();
        final String playerCode = SeqWorkerUtil.generateTimeSequence();
        Date time = Calendar.getInstance().getTime();
        Date expireTime = getExpireTime();
        //1.创建运动员信息
        player.setGmtCreate(time);
        player.setGmtModified(time);
        player.setPlayerCode(playerCode);
        player.setRegisterCode(registerCode);
        //3.创建报名信息
        Long orderAmount = 0L;
        ApplyInfo apply = null;
        ArrayList<ApplyInfo> applys = new ArrayList<ApplyInfo>();
        for (String itemCode : itemCodes) {
            apply = new ApplyInfo();
            SpMatchAllInfoModel item = spMatchFacadeClient.getItemDetail(fileidCode, itemCode);
            if (item == null) {
                throw new BusinessException("比赛项不纯在,请重新选择");
            }
            SpGroupItemModel itemInfo = item.getGroupItemInfo();
            String status = itemInfo.getStatus();
            if (status == null || "0".equals(status)) {
                throw new BusinessException(itemInfo.getItemName() + "已失效,请重新选择比赛项");
            }
            int personLimit = itemInfo.getPersonLimit();
            Long applyCount = applyInfoService.getApplyCountByEventCode(itemInfo.getItemCode());
            if (personLimit != 0 && personLimit < applyCount) {
                throw new BusinessException(itemInfo.getItemName() + "报名人数已满,请重新选择必晒项");
            }
            if (itemInfo.getEndTime() == null || new Date().after(itemInfo.getEndTime())) {
                throw new BusinessException(itemInfo.getItemName() + "比赛已结束,请重新选择");
            }
            orderAmount += itemInfo.getEntryFee();
            SpMatchGroupModel groupInfo = item.getGroupInfo();
            SpMatchModel matchInfo = item.getMatchInfo();
            SpPlayingAreaModel siteInfo = item.getPayAreaInfo();
            SpProjectTypeModel typeInfo = item.getProjectTypeInfo();

            apply.setApplyAmount(Long.valueOf(itemInfo.getEntryFee()));
            apply.setApplyCode(SeqWorkerUtil.generateTimeSequence());
            apply.setApplyStatus(ApplyStatusEnum.WAIT_PAY.getCode());
            apply.setEventCode(itemInfo.getEventCode());
            apply.setGroupEventCode(itemInfo.getItemCode());
            apply.setEventEndTime(itemInfo.getEndTime());
            apply.setEventName(itemInfo.getItemName());
            apply.setEventStartTime(itemInfo.getStartTime());
            apply.setChannelCode(terminal.getCode());
            apply.setExpireTime(expireTime);
            if (typeInfo != null) {
                apply.setGameCode(typeInfo.getGameCode());
                apply.setGameName(typeInfo.getGameName());
            }
            apply.setGmtCreate(time);
            apply.setGmtModified(time);
            apply.setIsDelet(0);
            if (matchInfo != null) {
                apply.setMatchCode(matchInfo.getMatchCode());
                apply.setMatchName(matchInfo.getMatchName());
                apply.setUnitCode(matchInfo.getUnitCode());

            }
            if (groupInfo != null) {
                apply.setMatchGroupCode(groupInfo.getAreaGroupCode());
                apply.setMatchGroupName(groupInfo.getGroupName());
            }
            apply.setPayOrderCode(orderCode);
            apply.setRegisterCode(registerCode);
            if (siteInfo != null) {
                apply.setSiteCode(siteInfo.getFieldCode());
                apply.setSiteName(siteInfo.getFieldName());
            }
            applys.add(apply);

        }
        PayOrder order = new PayOrder();
        order.setExpireTime(expireTime);
        order.setRegisterCode(registerCode);
        order.setGmtCreate(time);
        order.setGmtModified(expireTime);
        order.setNotifyUrl(weichatConfig.notifyUrl);
        order.setOrderCode(orderCode);
        order.setOrderType(OrderTypeEnum.APPLY.getCode());
        order.setOrderIp(ip);
        order.setOrderRefererUrl(referer);
        order.setOrderTime(time);
        order.setOrderPeriod(Constants.APPLY_TIME_OUT);
        order.setOrderStatus(OrderStatusEnum.WAIT_PAY.getCode());
        order.setRemark("报名费用");
        order.setOrderAmount(orderAmount);
        insert(order);

        player.setExtPro(JSON.toJSONString(player.getExtProMap()));
        //校验运动员属性 
        SpAthletesEnrollModel athiete = spAthletesFacadeClient.getAthietes(apply.getMatchCode());
        checkPlayerAttr(player, athiete, apply.getMatchCode(), null);
        playerService.insertOrUpdate(player);

        for (ApplyInfo applyInfo : applys) {
            applyInfoService.insert(applyInfo);
            ApplyPlayer applyPlayer = new ApplyPlayer();
            applyPlayer.setApplyCode(applyInfo.getApplyCode());
            applyPlayer.setPlayerCode(player.getPlayerCode());
            //创建中间表数据
            applyPlayerService.createApplyPlayer(applyPlayer);
        }

        //3.创建订单数据

        return order;
    }

    private Date getExpireTime() {
        int applyTimeOut = Constants.APPLY_TIME_OUT;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, applyTimeOut);
        return calendar.getTime();
    }

    /**
     * 查询报名订单
     * 
     * @param currentPage
     * @param pageSize
     * @param registerCode
     * @param orderStatus
     * @return
     */
    public Page<PayOrder> selectEnrollInfoByStatus(int currentPage, int pageSize,
                                                   String registerCode,
                                                   OrderStatusEnum orderStatus) {
        String st = "";
        if (orderStatus != null) {
            st = orderStatus.getCode();
        }
        log.info("selectEnrollInfoByStatus start,currentPage:" + currentPage + ",pageSize:"
                 + pageSize + ",registerCode:" + registerCode + ", orderStatus：" + st);
        long start = System.currentTimeMillis();
        Page<PayOrder> page = new Page<PayOrder>(currentPage, pageSize, "order_time", false);
        Wrapper<PayOrder> wrapper = new EntityWrapper<PayOrder>();
        wrapper.eq("register_code", registerCode);
        wrapper.eq("order_type", "apply");
        if (orderStatus != null) {
            wrapper.eq("order_status", orderStatus.getCode());
            if (orderStatus == OrderStatusEnum.WAIT_PAY) {
                wrapper.gt("expire_time", new Date());
            }
        }

        Page<PayOrder> selectPage = selectPage(page, wrapper);
        List<PayOrder> records = selectPage.getRecords();
        log.info("订单查询结果：" + selectPage.getRecords());
        if (CollectionUtils.isEmpty(records)) {
            return selectPage;
        }

        List<String> orderCodes = new ArrayList<String>();
        for (PayOrder payOrder : records) {
            orderCodes.add(payOrder.getOrderCode());
        }

        List<ApplyInfo> applyInfos = applyInfoService.getApplyInfosByOrderCodes(orderCodes);
        //异常数据
        if (applyInfos == null) {
            applyInfos = new ArrayList<ApplyInfo>();
        }
        List<String> applyCodes = new ArrayList<String>();
        Set<String> set = new HashSet<String>();
        for (ApplyInfo appInfo : applyInfos) {
            applyCodes.add(appInfo.getApplyCode());
            if (!StringUtils.isEmpty(appInfo.getMatchCode())) {
                set.add(appInfo.getMatchCode());
            }
        }

        Map<String, MatchShortInfo> matchMap = queryMatchInfos(set);
        for (ApplyInfo appInfo : applyInfos) {
            MatchShortInfo matchInfo = matchMap.get(appInfo.getMatchCode());
            if (matchInfo != null) {
                appInfo.setPoster(matchInfo.getPoster());
                appInfo.setStatus(matchInfo.getStatusDesc());
                appInfo.setStatusCode(matchInfo.getStatusCode());
            }
        }

        List<PlayerEx> selectPlayer = null;
        if (!CollectionUtils.isEmpty(applyCodes)) {
            selectPlayer = iPlayerService.selectPlayersByApplyInfoCodes(applyCodes);
        }
        for (int i = 0; i < records.size(); i++) {

            Date matchDate = null;
            PayOrder record = records.get(i);
            Long orderAmount = record.getOrderAmount();
            String amountStr = AmountUtils.changeF2Y(orderAmount);
            record.setOrderAmountStr(amountStr);
            List<ApplyInfo> tempApplyInfos = new ArrayList<ApplyInfo>();
            Set<String> tempApplyCodes = new HashSet<String>();
            for (ApplyInfo applyInfo : applyInfos) {
                if (applyInfo.getPayOrderCode().equals(record.getOrderCode())) {
                    tempApplyInfos.add(applyInfo);
                    tempApplyCodes.add(applyInfo.getApplyCode());
                    if (matchDate == null) {
                        MatchShortInfo matchInfo = matchMap.get(applyInfo.getMatchCode());
                        matchDate = matchInfo == null ? null : matchInfo.getMatchDate();
                    }
                }
            }
            record.setApplyInfos(tempApplyInfos);
            List<Player> players = new ArrayList<Player>();
            if (!CollectionUtils.isEmpty(selectPlayer)) {
                for (int j = 0; j < selectPlayer.size(); j++) {
                    if (tempApplyCodes.contains(selectPlayer.get(j).getApplyCode())) {
                        players.add(selectPlayer.get(j));
                    }
                }
            }
            record.setPlayers(players);
            record.setApplyInfos(tempApplyInfos);
            record.setApplyInfoSize(tempApplyInfos.size());

            if (OrderStatusEnum.WAIT_PAY.getCode().equals(record.getOrderStatus())) {
                record.setOverplusTime(0);
                boolean bool = DateUtil.compareDate(matchDate, new Date());
                if (bool) {
                    boolean bl = DateUtil.compareDate(record.getExpireTime(), new Date());
                    if (bl) {
                        Integer plusTime = (int) (record.getExpireTime().getTime()
                                                  - new Date().getTime())
                                           / 1000;
                        record.setOverplusTime(plusTime);
                    }
                }
            }
            Date orderTime = record.getOrderTime();
            record.setOrderTimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderTime));
        }

        log.info("selectEnrollInfoByStatus,currentPage:" + currentPage + ",pageSize:" + pageSize
                 + ",registerCode:" + registerCode + ",spent:"
                 + (System.currentTimeMillis() - start));
        return selectPage;
    }

    /**
     * 
     * 
     * @param matchCodes
     * @return
     */
    private Map<String, MatchShortInfo> queryMatchInfos(Set<String> matchCodes) {
        Map<String, MatchShortInfo> map = new HashMap<String, MatchShortInfo>();

        try {

            if (matchCodes == null) {
                return null;
            }
            long start = System.currentTimeMillis();
            Iterator<String> iter = matchCodes.iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                MatchShortInfo matchInfo = queryMatchInfos(key);
                map.put(key, matchInfo);
            }
            log.info("queryMatchInfos, matchCodes size:" + matchCodes.size() + ",spent:"
                     + (System.currentTimeMillis() - start) + "ms。");

        } catch (Exception ex) {
            log.error("matchCodes：" + matchCodes.toString(), ex);
        }
        return map;

    }

    private MatchShortInfo queryMatchInfos(String matchCode) {

        SpMatchInfoModel model = spMatchFacadeClient.getMatchDetail(matchCode);
        if (model == null) {
            return null;
        }
        Date matchDate = null;
        MatchShortInfo ms = new MatchShortInfo();

        matchDate = model.getEndTime();
        ms.setMatchDate(matchDate);
        ms.setPoster(model.getPoster());
        if (MatchRegEums.pause.getCode().equals(model.getRegStatus())) {
            ms.setStatusDesc(MatchStatusEnum.getDescByCode(MatchStatusEnum.PAUSE.getCode()));
            ms.setStatusCode(MatchStatusEnum.PAUSE.getCode());
        } else if (new Date().after(model.getRegTime())) {
            ms.setStatusDesc(MatchStatusEnum.getDescByCode(MatchStatusEnum.ABORT.getCode()));
            ms.setStatusCode("报名已截止");
        } else if (new Date().after(model.getEndTime())) {
            ms.setStatusDesc(MatchStatusEnum.getDescByCode(MatchStatusEnum.END.getCode()));
            ms.setStatusCode("已结束");
        } else {
            Date endTime = model.getEndTime();
            boolean bool = DateUtil.compareDate(endTime, new Date());
            if (bool) {
                ms.setStatusDesc(MatchStatusEnum.getDescByCode(MatchStatusEnum.GOIND.getCode()));
                ms.setStatusCode(MatchStatusEnum.GOIND.getCode());
            } else {
                ms.setStatusDesc(MatchStatusEnum.getDescByCode(MatchStatusEnum.END.getCode()));
                ms.setStatusCode(MatchStatusEnum.END.getCode());
            }
        }

        return ms;
    }

    //TODO    此处需要优化
    @Override
    public Page<PayOrder> selectPagePageOrderByStatus(int currentPage, int pageSize,
                                                      String registerCode,
                                                      OrderStatusEnum orderStatus) {
        return this.selectEnrollInfoByStatus(currentPage, pageSize, registerCode, orderStatus);

    }

    private Page<PayOrder> oldback(int currentPage, int pageSize, String registerCode,
                                   OrderStatusEnum orderStatus) {

        long start = System.currentTimeMillis();
        Page<PayOrder> page = new Page<PayOrder>(currentPage, pageSize, "order_time", false);
        Wrapper<PayOrder> wrapper = new EntityWrapper<PayOrder>();
        wrapper.eq("register_code", registerCode);
        if (orderStatus != null) {
            wrapper.eq("order_status", orderStatus.getCode());
            if (orderStatus == OrderStatusEnum.WAIT_PAY) {
                wrapper.gt("expire_time", new Date());
            }
        }

        Page<PayOrder> selectPage = selectPage(page, wrapper);
        List<PayOrder> records = selectPage.getRecords();
        log.info("订单查询结果：" + selectPage.getRecords());
        if (records != null && records.size() > 0) {
            for (PayOrder payOrder : records) {
                String amountStr = AmountUtils.changeF2Y(payOrder.getOrderAmount());
                payOrder.setOrderAmountStr(amountStr);
                List<ApplyInfo> applyInfos = applyInfoService
                    .getApplyInfoByOrderCode(payOrder.getOrderCode());
                List<Player> players = new ArrayList<Player>();
                Date matchDate = null;
                //TODO  如果团体报名支持多项报名 此处代码会有问题
                for (int i = 0; i < applyInfos.size(); i++) {
                    ApplyInfo info = applyInfos.get(i);
                    if (i == 0) {
                        List<Player> selectPlayer = iPlayerService
                            .selectPlayerByApplyInfoCode(info.getApplyCode());
                        if (selectPlayer != null && selectPlayer.size() > 0) {
                            players.addAll(players);
                        }
                    }

                    SpMatchInfoModel model = spMatchFacadeClient
                        .getMatchDetail(info.getMatchCode());
                    if (model != null) {
                        if (matchDate == null) {
                            matchDate = model.getEndTime();
                        }
                        info.setPoster(model.getPoster());
                        if (MatchRegEums.pause.getCode().equals(model.getRegStatus())) {
                            info.setStatus(
                                MatchStatusEnum.getDescByCode(MatchStatusEnum.PAUSE.getCode()));
                            info.setStatusCode(MatchStatusEnum.PAUSE.getCode());
                        } else {
                            Date regTime = model.getRegTime();
                            Date endTime = model.getEndTime();
                            boolean regbool = DateUtil.compareDate(regTime, new Date());
                            boolean bool = DateUtil.compareDate(new Date(), endTime);
                            if (bool) {
                                info.setStatus(
                                    MatchStatusEnum.getDescByCode(MatchStatusEnum.END.getCode()));
                                info.setStatusCode(MatchStatusEnum.END.getCode());
                            } else {
                                if (regbool) {
                                    info.setStatus(MatchStatusEnum
                                        .getDescByCode(MatchStatusEnum.GOIND.getCode()));
                                    info.setStatusCode(MatchStatusEnum.GOIND.getCode());
                                } else {
                                    info.setStatus(MatchStatusEnum
                                        .getDescByCode(MatchStatusEnum.ABORT.getCode()));
                                    info.setStatusCode(MatchStatusEnum.ABORT.getCode());
                                }
                            }
                            //                            if (regbool && startbool) {
                            //                                info.setStatus(
                            //                                    MatchStatusEnum.getDescByCode(MatchStatusEnum.ABORT.getCode()));
                            //                                info.setStatusCode(MatchStatusEnum.ABORT.getCode());
                            //                            } else {
                            //                                Date endTime = model.getEndTime();
                            //                                boolean bool = DateUtil.compareDate(endTime, new Date());
                            //                                if (bool) {
                            //                                    info.setStatus(MatchStatusEnum
                            //                                        .getDescByCode(MatchStatusEnum.GOIND.getCode()));
                            //                                    info.setStatusCode(MatchStatusEnum.GOIND.getCode());
                            //                                } else {
                            //                                    info.setStatus(MatchStatusEnum
                            //                                        .getDescByCode(MatchStatusEnum.END.getCode()));
                            //                                    info.setStatusCode(MatchStatusEnum.END.getCode());
                            //                                }
                            //                            }
                        }
                    }
                }
                payOrder.setApplyInfos(applyInfos);
                payOrder.setApplyInfoSize(applyInfos.size());
                //TODO 此处查询对应的运动员信息不知道有啥用  后期审查代码优化
                payOrder.setPlayers(players);
                if (OrderStatusEnum.WAIT_PAY.getCode().equals(payOrder.getOrderStatus())) {
                    payOrder.setOverplusTime(0);
                    boolean bool = DateUtil.compareDate(matchDate, new Date());
                    if (bool) {
                        boolean bl = DateUtil.compareDate(payOrder.getExpireTime(), new Date());
                        if (bl) {
                            Integer plusTime = (int) (payOrder.getExpireTime().getTime()
                                                      - new Date().getTime())
                                               / 1000;
                            payOrder.setOverplusTime(plusTime);
                        }
                    }
                }
                Date orderTime = payOrder.getOrderTime();
                payOrder
                    .setOrderTimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderTime));
            }
        }

        log.info("selectPagePageOrderByStatus,currentPage:" + currentPage + ",pageSize:" + pageSize
                 + ",registerCode:" + registerCode + ",spent:"
                 + (System.currentTimeMillis() - start));
        return selectPage;
    }

    /**
     *    事务操作顺序
    register
    
    order,
    
    player
    appply
    leader
     * @see com.efida.sports.service.IPayOrderService#createWaitComplete(java.util.List, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    @Transactional
    public PayOrder createWaitComplete(List<String> itemCodes, String siteCode, String ip,
                                       String registerCode, RegTerminalEnum terminal) {

        final String orderCode = SeqWorkerUtil.generateTimeSequence();
        Date time = Calendar.getInstance().getTime();
        //        Date expireTime = getExpireTime();
        //1.创建报名信息
        Long orderAmount = 0L;
        ArrayList<ApplyInfo> applys = new ArrayList<ApplyInfo>();

        for (String itemCode : itemCodes) {
            ApplyInfo apply = new ApplyInfo();
            SpMatchAllInfoModel item = spMatchFacadeClient.getItemDetail(siteCode, itemCode);
            if (item == null) {
                throw new BusinessException("比赛项不纯在,请重新选择");
            }
            log.info("获取比赛项详情,参数siteCode:{}, itemCode:{},返回结果：{}", siteCode, itemCode,
                JSON.toJSONString(item));
            SpGroupItemModel itemInfo = item.getGroupItemInfo();
            String status = itemInfo.getStatus();
            if (status == null || "0".equals(status)) {
                throw new BusinessException(itemInfo.getItemName() + "已失效,请重新选择比赛项");
            }
            int personLimit = itemInfo.getPersonLimit();
            Long applyCount = applyInfoService.getApplyCountByEventCode(itemInfo.getItemCode());
            if (personLimit != 0 && personLimit < applyCount) {
                throw new BusinessException(itemInfo.getItemName() + "报名人数已满,请重新选择必晒项");
            }
            if (itemInfo.getEndTime() == null || new Date().after(itemInfo.getEndTime())) {
                throw new BusinessException(itemInfo.getItemName() + "比赛已结束,请重新选择");
            }
            orderAmount += itemInfo.getEntryFee();
            SpMatchGroupModel groupInfo = item.getGroupInfo();
            SpMatchModel matchInfo = item.getMatchInfo();
            SpPlayingAreaModel siteInfo = item.getPayAreaInfo();
            SpProjectTypeModel typeInfo = item.getProjectTypeInfo();

            apply.setApplyAmount(Long.valueOf(itemInfo.getEntryFee()));
            apply.setApplyCode(SeqWorkerUtil.generateTimeSequence());
            apply.setApplyStatus(ApplyStatusEnum.WAIT_COMPLETE.getCode());
            apply.setEventCode(itemInfo.getEventCode());
            apply.setGroupEventCode(itemInfo.getItemCode());
            apply.setEventEndTime(itemInfo.getEndTime());
            apply.setEventName(itemInfo.getItemName());
            apply.setEventStartTime(itemInfo.getStartTime());
            apply.setChannelCode(terminal.getCode());
            SpGroupItemModel groupItemInfo = item.getGroupItemInfo();
            apply.setEventType(groupItemInfo.getItemType());
            apply.setRegistrationNum(groupItemInfo.getGroupLimit());
            apply.setExpireTime(new Date());
            if (typeInfo != null) {
                apply.setGameCode(typeInfo.getGameCode());
                apply.setGameName(typeInfo.getGameName());
            }
            apply.setGmtCreate(time);
            apply.setGmtModified(time);
            apply.setIsDelet(0);
            if (matchInfo != null) {
                apply.setMatchCode(matchInfo.getMatchCode());
                apply.setMatchName(matchInfo.getMatchName());
                apply.setUnitCode(matchInfo.getUnitCode());
            }
            if (groupInfo != null) {
                apply.setMatchGroupCode(groupInfo.getAreaGroupCode());
                apply.setMatchGroupName(groupInfo.getGroupName());
            }
            apply.setPayOrderCode(orderCode);
            apply.setRegisterCode(registerCode);

            if (siteInfo != null) {
                apply.setSiteCode(siteInfo.getFieldCode());
                apply.setSiteName(siteInfo.getFieldName());
            }
            applys.add(apply);
        }
        //3.创建订单数据
        PayOrder order = new PayOrder();
        order.setExpireTime(new Date());
        order.setRegisterCode(registerCode);
        order.setGmtCreate(time);
        order.setOrderIp(ip);
        order.setGmtModified(new Date());
        order.setOrderCode(orderCode);
        order.setOrderType(OrderTypeEnum.APPLY.getCode());
        order.setOrderTime(time);
        order.setOrderPeriod(Constants.APPLY_TIME_OUT);
        order.setOrderStatus(OrderStatusEnum.WAIT_COMPLETE.getCode());
        order.setRemark("报名费用");
        order.setOrderAmount(orderAmount);
        insert(order);
        for (ApplyInfo applyInfo : applys) {
            applyInfoService.insert(applyInfo);
        }
        order.setApplyInfos(applys);
        return order;
    }

    public PayOrder createWaitComplete(List<SiteOrderDto> sites, String ip, String registerCode) {

        final String orderCode = SeqWorkerUtil.generateTimeSequence();
        //final String playerCode = SeqWorkerUtil.generateTimeSequence();
        Date time = Calendar.getInstance().getTime();
        //        Date expireTime = getExpireTime();
        //1.创建运动员信息
        //3.创建报名信息
        Long orderAmount = 0L;
        ArrayList<ApplyInfo> applys = new ArrayList<ApplyInfo>();

        for (SiteOrderDto site : sites) {
            for (String itemCode : site.getEventdCodes()) {
                ApplyInfo apply = new ApplyInfo();
                SpMatchAllInfoModel item = spMatchFacadeClient.getItemDetail(site.getSiteCode(),
                    itemCode);
                if (item == null) {
                    throw new BusinessException("比赛项不纯在,请重新选择");
                }
                log.info("获取比赛项详情,参数siteCode:{}, itemCode:{},返回结果：{}", site.getSiteCode(), itemCode,
                    JSON.toJSONString(item));
                SpGroupItemModel itemInfo = item.getGroupItemInfo();
                String status = itemInfo.getStatus();
                if (status == null || "0".equals(status)) {
                    throw new BusinessException(itemInfo.getItemName() + "已失效,请重新选择比赛项");
                }
                int personLimit = itemInfo.getPersonLimit();
                Long applyCount = applyInfoService.getApplyCountByEventCode(itemInfo.getItemCode());
                if (personLimit != 0 && personLimit < applyCount) {
                    throw new BusinessException(itemInfo.getItemName() + "报名人数已满,请重新选择必晒项");
                }
                if (itemInfo.getEndTime() == null || new Date().after(itemInfo.getEndTime())) {
                    throw new BusinessException(itemInfo.getItemName() + "比赛已结束,请重新选择");
                }
                orderAmount += itemInfo.getEntryFee();
                SpMatchGroupModel groupInfo = item.getGroupInfo();
                SpMatchModel matchInfo = item.getMatchInfo();
                SpPlayingAreaModel siteInfo = item.getPayAreaInfo();
                SpProjectTypeModel typeInfo = item.getProjectTypeInfo();

                apply.setApplyAmount(Long.valueOf(itemInfo.getEntryFee()));
                apply.setApplyCode(SeqWorkerUtil.generateTimeSequence());
                apply.setApplyStatus(ApplyStatusEnum.WAIT_COMPLETE.getCode());
                apply.setEventCode(itemInfo.getEventCode());
                apply.setGroupEventCode(itemInfo.getItemCode());
                apply.setEventEndTime(itemInfo.getEndTime());
                apply.setEventName(itemInfo.getItemName());
                apply.setEventStartTime(itemInfo.getStartTime());
                SpGroupItemModel groupItemInfo = item.getGroupItemInfo();
                apply.setEventType(groupItemInfo.getItemType());
                apply.setRegistrationNum(groupItemInfo.getGroupLimit());
                apply.setExpireTime(new Date());
                if (typeInfo != null) {
                    apply.setGameCode(typeInfo.getGameCode());
                    apply.setGameName(typeInfo.getGameName());
                }
                apply.setGmtCreate(time);
                apply.setGmtModified(time);
                apply.setIsDelet(0);
                if (matchInfo != null) {
                    apply.setMatchCode(matchInfo.getMatchCode());
                    apply.setMatchName(matchInfo.getMatchName());
                    apply.setUnitCode(matchInfo.getUnitCode());
                }
                if (groupInfo != null) {
                    apply.setMatchGroupCode(groupInfo.getAreaGroupCode());
                    apply.setMatchGroupName(groupInfo.getGroupName());
                }
                apply.setPayOrderCode(orderCode);
                apply.setRegisterCode(registerCode);

                if (siteInfo != null) {
                    apply.setSiteCode(siteInfo.getFieldCode());
                    apply.setSiteName(siteInfo.getFieldName());
                }
                applyInfoService.insert(apply);
                applys.add(apply);
            }
        }
        //3.创建订单数据
        PayOrder order = new PayOrder();
        order.setExpireTime(new Date());
        order.setRegisterCode(registerCode);
        order.setGmtCreate(time);
        order.setOrderIp(ip);
        order.setGmtModified(new Date());
        order.setOrderCode(orderCode);
        order.setOrderType(OrderTypeEnum.APPLY.getCode());
        order.setOrderTime(time);
        order.setOrderPeriod(Constants.APPLY_TIME_OUT);
        order.setOrderStatus(OrderStatusEnum.WAIT_COMPLETE.getCode());
        order.setRemark("报名费用");
        order.setOrderAmount(orderAmount);
        insert(order);
        order.setApplyInfos(applys);
        return order;
        //创建运动员
        //        Player player = new Player();
        //        player.setPlayerCode(playerCode);
        //        player.setRegisterCode(registerCode);
        //        player.setGmtCreate(new Date());
        //        player.setRegisterCode(registerCode);
        //        playerService.insert(player);
    }

    /**
     *   事务操作顺序
    register
    
    order,
    
    player
    appply
    leader
     * @see com.efida.sports.service.IPayOrderService#completeOrder(java.lang.String, java.lang.String, com.efida.sports.entity.Player)
     */
    @Override
    @Transactional
    public PayOrder completeOrder(String orderCode, String registerCode, Player player) {
        if (StringUtils.isBlank(orderCode)) {
            throw new BusinessException("订单编号不能为空");
        }
        PayOrder order = getOrderByCode(orderCode);
        if (null == order) {
            throw new BusinessException("订单不存在");
        }
        //修改报名表
        List<ApplyInfo> applyInfos = applyInfoService.getApplyInfoByOrderCode(orderCode);
        if (applyInfos == null || applyInfos.size() < 1) {
            throw new BusinessException("报名信息不存在");
        }
        List<String> codes = new ArrayList<String>();
        for (ApplyInfo applyInfo : applyInfos) {
            codes.add(applyInfo.getApplyCode());
        }
        Date time = Calendar.getInstance().getTime();
        Date expireTime = getExpireTime();
        //1.修改订单数据
        order.setExpireTime(expireTime);
        order.setRegisterCode(registerCode);
        order.setGmtModified(time);
        order.setGmtModified(new Date());
        order.setOrderCode(orderCode);
        order.setOrderType(OrderTypeEnum.APPLY.getCode());
        order.setOrderTime(time);
        order.setOrderPeriod(Constants.APPLY_TIME_OUT);
        order.setOrderStatus(OrderStatusEnum.WAIT_PAY.getCode());
        updateById(order);

        String matchCode = applyInfos.get(0).getMatchCode();
        SpAthletesEnrollModel athiete = spAthletesFacadeClient.getAthietes(matchCode);
        checkPlayerAttr(player, athiete, matchCode, codes);

        ApplyInfo applyInfo = applyInfos.get(0);
        Player savePlayer = null;
        Player oldPlayer = null;
        //如果是个人报名
        if (EventTypeEnums.personal.getCode().equals(applyInfo.getEventType())) {
            oldPlayer = playerService.getPersonalPlayerByApplyCode(applyInfo.getApplyCode());
            savePlayer = handlePlayerInfo(oldPlayer, player);
            savePlayer.setRegisterCode(registerCode);
            savePlayer.setExtPro(JSON.toJSONString(player.getExtProMap()));
            playerService.insertOrUpdate(savePlayer);
        }
        //修改报名信息
        for (ApplyInfo apply : applyInfos) {
            checkApplyInfo(applyInfo);
            apply.setGmtModified(time);
            apply.setApplyStatus(ApplyStatusEnum.WAIT_PAY.getCode());
            apply.setExpireTime(getExpireTime());
            ApplyPlayer applyPlayer = new ApplyPlayer();
            applyPlayer.setApplyCode(apply.getApplyCode());
            applyPlayer.setPlayerCode(savePlayer.getPlayerCode());
            //创建中间表数据
            applyPlayerService.createApplyPlayer(applyPlayer);
        }
        //修改报名状态
        applyInfoService.updateBatchById(applyInfos);

        return order;
    }

    /**
    * 验证运动员表单数据
     * @throws Exception 
    */
    private void checkPlayerAttr(Player player, SpAthletesEnrollModel athiete, String matchCode,
                                 List<String> applyCodes) {
        if (athiete == null || athiete.getEnrollForm() == null
            || athiete.getEnrollForm().size() < 1) {
            return;
        }
        Map<String, String> playerMap = objectToMap(player);
        Map<String, Object> extProMap = player.getExtProMap();
        List<FromTypeModel> list = athiete.getEnrollForm();
        for (FromTypeModel model : list) {
            //如果后台标准为必填项
            if (model.getIsRequired()) {
                String key = model.getAttribute();
                if (extProMap.containsKey(key)) {
                    Object object = extProMap.get(key);
                    if (object == null || StringUtils.isBlank(object.toString())) {
                        throw new BusinessException(model.getName() + "为必填项");
                    }
                    if (object.toString().length() > 24) {
                        throw new BusinessException(model.getName() + "长度不能超过24位");
                    }
                }
                if (playerMap.containsKey(key) && StringUtils.isBlank(playerMap.get(key))) {
                    throw new BusinessException(model.getName() + "为必填项");
                }
                String val = playerMap.get(key);
                if ("playerPhone".equals(key)) {
                    if (!RegexUtils.checkMobile(val)) {
                        throw new BusinessException("请填写正确的" + model.getName());
                    }
                }

                if ("email".equals(key)) {
                    if (!RegexUtils.checkEmail(val)) {
                        throw new BusinessException("请填写正确的" + model.getName());
                    }
                }
                if ("assettoId".equals(key)) {
                    if (StringUtils.isBlank(val)) {
                        throw new BusinessException(model.getName() + "不能为空");
                    }
                    if (!RegexUtils.checkAssettoId(val)) {
                        throw new BusinessException("请填写英文神力科莎账户ID");
                    }
                    //                    checkAassettoId(val, matchCode, applyCodes);
                }

                if ("playerCerType".equals(key)) {
                    if (!playerMap.containsKey("playerCerNo")
                        || StringUtils.isBlank(playerMap.get("playerCerNo"))) {
                        throw new BusinessException("证件号码不能为空");
                    }
                    String playerCerNo = playerMap.get("playerCerNo");
                    //如果证件类型是省份证
                    if ("1".equals(val)) {
                        if (!RegexUtils.checkIdCard(playerCerNo)) {
                            throw new BusinessException("请填写正确的身份证号码");
                        }
                        //                        if (!playerMap.containsKey("attOne")
                        //                            || StringUtils.isBlank(playerMap.get("attOne"))) {
                        //                            throw new BusinessException("请上传身份证正面");
                        //                        }
                        //                        if (!playerMap.containsKey("attTwo")
                        //                            || StringUtils.isBlank(playerMap.get("attTwo"))) {
                        //                            throw new BusinessException("请上传身份证反面");
                        //                        }

                    }
                    if ("2".equals(val)) {
                        if (!RegexUtils.checkPassport(playerCerNo)) {
                            throw new BusinessException("请填写正确的护照号码");
                        }
                        //                        if (!playerMap.containsKey("attOne")
                        //                            || StringUtils.isBlank(playerMap.get("attOne"))) {
                        //                            throw new BusinessException("请上传护照照片");
                        //                        }

                    }
                    if ("4".equals(val)) {
                        if (!RegexUtils.checkCertificateOfficers(playerCerNo)) {
                            throw new BusinessException("请填写正确的军官证号码");
                        }
                        //                        if (!playerMap.containsKey("attOne")
                        //                            || StringUtils.isBlank(playerMap.get("attOne"))) {
                        //                            throw new BusinessException("请上传军官证照片");
                        //                        }
                    }

                }

            }

        }

    }

    private boolean checkAassettoId(String aassettoId, String matchCode, List<String> applyCodes) {
        List<ApplyInfo> list = applyInfoService.getApplyInfosByAassetoidAndMatchCode(aassettoId,
            matchCode);
        if (list == null || list.size() < 1) {
            return true;
        }
        Set<String> set = new HashSet<String>();
        if (applyCodes != null && applyCodes.size() > 0) {
            set.addAll(applyCodes);
            for (ApplyInfo applyInfo : list) {
                set.add(applyInfo.getApplyCode());
            }
            //如果集合长度未该表,表示占位的是自己
            if (set.size() > applyCodes.size()) {
                throw new BusinessException("神力科莎账户ID:" + aassettoId + "已存在报名记录,报名失败");
            }
        }
        if (list.size() > 0) {
            throw new BusinessException("神力科莎账户ID:" + aassettoId + "已存在报名记录,报名失败");
        }
        return true;
    }

    public static Map<String, String> objectToMap(Object obj) {
        Map<String, String> map = new HashMap<String, String>();
        if (obj == null) {
            return map;
        }
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter != null ? getter.invoke(obj) : "";
                map.put(key, String.valueOf(value));
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return map;
    }

    private Player handlePlayerInfo(Player old, Player player) {
        if (old == null) {
            player.setGmtCreate(new Date());
            player.setGmtModified(new Date());
            player.setPlayerCode(SeqWorkerUtil.generateTimeSequence());
            return player;
        }
        //如果之前存在运动员信息,将原来运动员属性复制到新运动员中
        player.setGmtModified(new Date());
        player.setId(old.getId());
        player.setPlayerCode(old.getPlayerCode());
        return player;
    }

    private void checkApplyInfo(ApplyInfo info) {
        String eventCode = info.getGroupEventCode();
        SpMatchAllInfoModel itemDetail = spMatchFacadeClient.getItemDetail(info.getSiteCode(),
            eventCode);
        if (itemDetail == null) {
            throw new BusinessException(info.getEventName() + "不存在,请重新选择");
        }
        SpGroupItemModel itemInfo = itemDetail.getGroupItemInfo();
        if (itemInfo == null) {
            throw new BusinessException(info.getEventName() + "不存在,请重新选择");
        }
        if ("0".equals(itemInfo.getStatus())) {
            throw new BusinessException(info.getEventName() + "已失效,请重新选择");
        }
        if (itemInfo.getEndTime() == null || new Date().after(itemInfo.getEndTime())) {
            throw new BusinessException(info.getEventName() + "比赛已结束,请重新选择");
        }
        if (itemInfo.getPersonLimit() != 0) {
            Long applyCount = applyInfoService.getApplyCountByEventCode(eventCode);
            if (applyCount >= itemInfo.getPersonLimit()) {
                throw new BusinessException(info.getEventName() + "报名人数已满,请重新选择");
            }
        }
        checkUniteStrategy(info.getUnitCode(), info);
    }

    /**
     * 不同承办方比赛提供不同检查策略;
     * 使用场景：
     * @param unitCode
     * @param info
     */

    public void checkUniteStrategy(String unitCode, ApplyInfo info) {

        if ("lantianlvye".equals(unitCode)) {

            String matchCode = info.getMatchCode();
            String siteCode = info.getSiteCode();
            List<Player> players = this.playerService
                .selectPlayerByApplyInfoCode(info.getApplyCode());
            Set<String> phones = new HashSet<String>();
            if (players != null) {
                for (Player pl : players) {
                    if (pl.getPlayerPhone() != null && pl.getPlayerPhone().length() > 2) {
                        phones.add(pl.getPlayerPhone());
                    }
                }
            }
            List<ApplyInfo> applyInfos = this.applyInfoService
                .queryApplyInfoByPhoneAndMachInfo(matchCode, siteCode, phones);
            if (!CollectionUtils.isEmpty(applyInfos)) {
                throw new BusinessException(info.getEventName() + "本次报名手机号已经有成功报名申请，不可重复报名!");

            }

        } else if ("youtu".equals(unitCode)) {
            //一个手机号报名1个比赛项
            String matchCode = info.getMatchCode();
            String eventCode = info.getSiteCode();
            //todo: validate 
            /*List<Player> players = this.playerService
                .selectPlayerByApplyInfoCode(info.getApplyCode());
            Set<String> phones = new HashSet<String>();
            if (players != null) {
                for (Player pl : players) {
                    if (pl.getPlayerPhone() != null && pl.getPlayerPhone().length() > 2) {
                        phones.add(pl.getPlayerPhone());
                    }
                }
            }
            List<ApplyInfo> applyInfos = this.applyInfoService
                .queryApplyInfoByPhoneAndMachInfo(matchCode, eventCode, phones);
            if (!CollectionUtils.isEmpty(applyInfos)) {
                throw new BusinessException(info.getEventName() + "本次报名手机号已经有成功报名申请，不可重复报名!");
            
            }
            */
        }
    }

    /**
     *   事务操作顺序
    register
    
    order,
    
    player
    appply
    leader
     * @see com.efida.sports.service.IPayOrderService#createTeamMembers(com.efida.sports.entity.Player, java.lang.String, java.lang.String)
     */
    @Override
    @Transactional
    public Player createTeamMembers(Player player, String orderCode, String registerCode) {
        if (StringUtils.isBlank(orderCode)) {
            throw new BusinessException("订单编号不能为空");
        }
        PayOrder order = getOrderByCode(orderCode);
        if (null == order) {
            throw new BusinessException("订单不存在");
        }
        List<ApplyInfo> applyInfos = applyInfoService.getApplyInfoByOrderCode(orderCode);
        if (applyInfos == null || applyInfos.size() < 1) {
            throw new BusinessException("报名信息不存在");
        }
        if (applyInfos.size() != 1) {
            throw new BusinessException("团体比赛智能选择一个比赛项");
        }
        ApplyInfo apply = applyInfos.get(0);
        if (EventTypeEnums.personal.getCode().equals(apply.getEventCode())) {
            throw new BusinessException("改比赛项不是团体赛,不能添加团体成员");
        }
        Player oldPlayer = null;
        if (StringUtils.isNotBlank(player.getPlayerCode())) {
            oldPlayer = playerService.getPlayerByPlayerCode(player.getPlayerCode());
        }
        //检查报名信息
        checkApplyInfo(apply);
        if (oldPlayer == null) {
            int count = applyPlayerService.getTotalTermNum(apply.getApplyCode());
            Integer registrationNum = apply.getRegistrationNum();
            if (registrationNum != null && registrationNum != 0 && count >= registrationNum) {
                throw new BusinessException(apply.getEventName() + "超出团队报名人数");
            }
        }
        Player savePlayer = handlePlayerInfo(oldPlayer, player);
        savePlayer.setExtPro(JSON.toJSONString(savePlayer.getExtProMap()));
        SpAthletesEnrollModel athiete = spAthletesFacadeClient.getAthietes(apply.getMatchCode());
        ArrayList<String> codes = new ArrayList<String>();
        codes.add(apply.getApplyCode());
        //检查运动员属性
        checkPlayerAttr(player, athiete, apply.getMatchCode(), codes);
        playerService.insertOrUpdate(savePlayer);
        apply.setGmtModified(new Date());
        apply.setApplyStatus(ApplyStatusEnum.WAIT_COMPLETE.getCode());
        ApplyPlayer applyPlayer = new ApplyPlayer();
        applyPlayer.setApplyCode(apply.getApplyCode());
        applyPlayer.setPlayerCode(savePlayer.getPlayerCode());
        //创建中间表数据
        applyPlayerService.createApplyPlayer(applyPlayer);
        return savePlayer;
    }

    @Override
    @Transactional
    public PayOrder completeTeamApply(LeaderInfo learn, String orderCode, String registerCode) {

        if (StringUtils.isBlank(orderCode)) {
            throw new BusinessException("订单编号不能为空");
        }
        PayOrder order = getOrderByCode(orderCode);
        if (null == order) {
            throw new BusinessException("订单不存在");
        }
        if (OrderStatusEnum.SUCCESS.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException("该订单已成功报名,不允许重复提交");
        }
        //修改报名表
        List<ApplyInfo> applyInfos = applyInfoService.getApplyInfoByOrderCode(orderCode);
        if (applyInfos == null || applyInfos.size() < 1) {
            throw new BusinessException("报名信息不存在");
        }
        if (applyInfos.size() != 1) {
            throw new BusinessException("团体比赛只能选择一个比赛项");
        }
        Date time = Calendar.getInstance().getTime();
        Date expireTime = getExpireTime();
        //1.修改订单数据
        order.setExpireTime(expireTime);
        order.setRegisterCode(registerCode);
        order.setGmtModified(time);
        order.setGmtModified(new Date());
        order.setOrderCode(orderCode);
        order.setOrderType(OrderTypeEnum.APPLY.getCode());
        order.setOrderTime(time);
        order.setOrderPeriod(Constants.APPLY_TIME_OUT);
        order.setOrderStatus(OrderStatusEnum.WAIT_PAY.getCode());
        updateById(order);

        ApplyInfo apply = applyInfos.get(0);
        //检查报名信息
        checkApplyInfo(apply);
        int count = applyPlayerService.getTotalTermNum(apply.getApplyCode());
        Integer registrationNum = apply.getRegistrationNum();
        if (registrationNum == null || registrationNum == 0) {
            if (count < 3) {
                throw new BusinessException(apply.getEventName() + "至少需要" + 2 + "名团队成员");
            }
        } else {
            if (count < registrationNum) {
                throw new BusinessException(
                    apply.getEventName() + "至少需要" + registrationNum + "名团队成员");
            }
        }
        apply.setGmtModified(time);
        apply.setApplyStatus(ApplyStatusEnum.WAIT_PAY.getCode());
        apply.setExpireTime(expireTime);
        //修改报名状态
        applyInfoService.updateById(apply);
        //检查领队基本信息
        insertLearnerInfo(learn, registerCode, apply.getApplyCode());

        return order;

    }

    @Override
    public void saveLearnerInfo(LeaderInfo learn, String registerCode, String orderCode) {
        List<ApplyInfo> applyInfos = applyInfoService.getApplyInfoByOrderCode(orderCode);
        if (applyInfos == null || applyInfos.size() < 1) {
            throw new BusinessException("报名信息不存在");
        }
        if (applyInfos.size() != 1) {
            throw new BusinessException("团体比赛智能选择一个比赛项");
        }
        if (learn.getTeamName().length() > 20) {
            throw new BusinessException("团队名称长度不能超过20位");
        }

        String applyCode = applyInfos.get(0).getApplyCode();
        LeaderInfo selLearn = learnService.getLearnInfoByApplyCode(applyCode);
        if (selLearn != null) {
            learn.setLeaderCode(selLearn.getApplyInfoCode());
            learn.setId(selLearn.getId());
        } else {
            learn.setLeaderCode(SeqWorkerUtil.buildSingleId());
        }
        learn.setRegisterCode(registerCode);
        learn.setGmtCreate(new Date());
        learn.setGmtModified(new Date());
        learn.setApplyInfoCode(applyCode);
        learnService.insertOrUpdate(learn);
    }

    private void insertLearnerInfo(LeaderInfo learn, String registerCode, String applyCode) {

        LeaderInfo selLearn = learnService.getLearnInfoByApplyCode(applyCode);
        if (learn == null) {
            throw new BusinessException("领队信息不能为空");
        }
        if (StringUtils.isBlank(learn.getLeaderName())) {
            throw new BusinessException("领队名称不能为空");
        }
        if (StringUtils.isBlank(learn.getLeaderPhone()) || learn.getLeaderPhone().length() != 11) {
            throw new BusinessException("领队电话号码填写错误");
        }
        if (StringUtils.isBlank(learn.getTeamName())) {
            throw new BusinessException("团队名称不能为空");
        }
        if (learn.getTeamName().length() > 20) {
            throw new BusinessException("团队名称长度不能超过20位");
        }
        if (selLearn != null) {
            learn.setLeaderCode(selLearn.getApplyInfoCode());
            learn.setId(selLearn.getId());
        } else {
            learn.setLeaderCode(SeqWorkerUtil.buildSingleId());
            learn.setGmtCreate(new Date());
        }
        learn.setRegisterCode(registerCode);
        learn.setGmtModified(new Date());
        learn.setApplyInfoCode(applyCode);
        learnService.insertOrUpdate(learn);
    }

    @Override
    public PayOrder savePlayer(String orderCode, String registerCode, Player player) {

        if (StringUtils.isNotBlank(player.getPlayerName())) {
            if (player.getPlayerName().length() > 24) {
                throw new BusinessException("运动员名称长度不能超过24位");
            }
        }

        if (StringUtils.isBlank(orderCode)) {
            throw new BusinessException("订单编号不能为空");
        }
        PayOrder order = getOrderByCode(orderCode);
        if (null == order) {
            throw new BusinessException("订单不存在");
        }
        if (OrderStatusEnum.SUCCESS.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException("该订单已成功报名,不允许重复提交");
        }
        //修改报名表
        List<ApplyInfo> applyInfos = applyInfoService.getApplyInfoByOrderCode(orderCode);
        if (applyInfos == null || applyInfos.size() < 1) {
            throw new BusinessException("报名信息不存在");
        }
        ApplyInfo applyInfo = applyInfos.get(0);
        Date time = Calendar.getInstance().getTime();
        Player savePlayer = null;
        Player oldPlayer = null;
        //如果是个人报名
        if (EventTypeEnums.personal.getCode().equals(applyInfo.getEventType())) {
            oldPlayer = playerService.getPersonalPlayerByApplyCode(applyInfo.getApplyCode());
            savePlayer = handlePlayerInfo(oldPlayer, player);
            savePlayer.setRegisterCode(registerCode);
            savePlayer.setExtPro(JSON.toJSONString(player.getExtProMap()));
            playerService.insertOrUpdate(savePlayer);
        } else {
            //如果是团体报名
            String playerCode = player.getPlayerCode();
            if (StringUtils.isNotBlank(playerCode)) {
                oldPlayer = playerService.getPlayerByPlayerCode(playerCode);
            }
            savePlayer = handlePlayerInfo(oldPlayer, player);
        }
        for (ApplyInfo apply : applyInfos) {
            checkApplyInfo(applyInfo);
            apply.setGmtModified(time);
            apply.setApplyStatus(ApplyStatusEnum.WAIT_COMPLETE.getCode());
            ApplyPlayer applyPlayer = new ApplyPlayer();
            applyPlayer.setApplyCode(apply.getApplyCode());
            applyPlayer.setPlayerCode(savePlayer.getPlayerCode());
            //创建中间表数据
            applyPlayerService.createApplyPlayer(applyPlayer);
        }

        //修改报名状态
        applyInfoService.updateBatchById(applyInfos);
        //3.创建订单数据
        order.setExpireTime(time);
        order.setRegisterCode(registerCode);
        order.setGmtModified(time);
        order.setGmtModified(new Date());
        order.setOrderCode(orderCode);
        order.setOrderTime(time);
        order.setOrderPeriod(Constants.APPLY_TIME_OUT);
        order.setOrderStatus(OrderStatusEnum.WAIT_COMPLETE.getCode());
        updateById(order);
        return order;

    }

    @Override
    public PayOrder checkOrderCanPay(String orderCode) {
        if (StringUtils.isBlank(orderCode)) {
            throw new BusinessException("订单编号不能为空");
        }
        PayOrder payOrder = getOrderDetail(orderCode);
        if (payOrder == null) {
            throw new BusinessException("订单不存在");
        }
        List<ApplyInfo> applyInfos = payOrder.getApplyInfos();
        for (ApplyInfo applyInfo : applyInfos) {
            checkApplyInfo(applyInfo);
        }
        return payOrder;
    }

    @Override
    public void batchUpdateOrderCreater(String oldRegisterCode, String newRegisterCode) {
        payOrderMapper.batchUpdateOrderCreater(oldRegisterCode, newRegisterCode);
    }

    /**
     * 
     * 
     * @param apply
     * @param player
     * @param regTerminal
     * @return
     */
    @Transactional
    @Override
    public String createApply(ApplyInfo apply, Player player, TerminalEnum terminal, String ip) {
        if (player == null) {
            throw new BusinessException("运动员不能为空");
        }
        String phoneNum = player.getPlayerPhone();
        apply.setEventType(EventTypeEnums.personal.getCode());

        if (StringUtils.isEmpty(phoneNum)) {
            throw new BusinessException("手机号不能为空");
        }
        RegisterModel register = ucenterRegisterFacadeClient.getRegsiterByAccount(phoneNum);
        final String orderCode = SeqWorkerUtil.generateTimeSequence();
        final String applyCode = SeqWorkerUtil.generateTimeSequence();
        final String playerCode = SeqWorkerUtil.generateTimeSequence();
        String idempotentId = apply.getIdempotentId();
        if (StringUtils.isEmpty(idempotentId)) {
            idempotentId = applyCode;
        }

        //创建用户
        if (register == null) {
            register = new RegisterModel();
            register.setAccount(phoneNum);
            register.setRegTerminal(terminal);
            register.setRegTime(Calendar.getInstance().getTime());
            register.setLastLoginTime(new Date());
            register.setGmtCreate(new Date());
            register.setGmtModified(new Date());
            register.setChannelCode(apply.getUnitCode());
            register = ucenterRegisterFacadeClient.createRegsiter(register);
        }
        String registerCode = register.getRegisterCode();
        //2.创建订单数据
        Date time = Calendar.getInstance().getTime();
        PayOrder order = new PayOrder();
        order.setExpireTime(time);
        order.setRegisterCode(registerCode);
        order.setGmtCreate(time);
        order.setGmtModified(time);
        order.setOrderIp(ip);
        order.setOrderCode(orderCode);
        order.setOrderType(OrderTypeEnum.APPLY.getCode());
        order.setOrderTime(apply.getApplyTime());
        order.setPayTime(apply.getApplyTime());
        order.setOrderStatus(OrderStatusEnum.SUCCESS.getCode());
        order.setRemark("报名费用");
        order.setOrderAmount(apply.getApplyAmount());
        insert(order);
        String playerPhone = player.getPlayerPhone();
        if (StringUtils.isEmpty(playerPhone)) {
            throw new BusinessException("运动员联系电话不能为空");
        }
        player.setPlayerCode(playerCode);
        player.setGmtCreate(time);
        player.setGmtModified(time);
        player.setRegisterCode(registerCode);
        playerService.insert(player);
        ApplyPlayer applyPlayer = new ApplyPlayer();
        applyPlayer.setApplyCode(applyCode);
        applyPlayer.setPlayerCode(player.getPlayerCode());
        //创建中间表数据
        applyPlayerService.createApplyPlayer(applyPlayer);

        apply.setApplyStatus(ApplyStatusEnum.SUCCESS.getCode());
        apply.setApplyCode(applyCode);
        apply.setGmtCreate(time);
        apply.setGmtModified(time);
        apply.setIsDelet(0);
        apply.setPayOrderCode(orderCode);
        apply.setRegisterCode(registerCode);
        apply.setSync((byte) 0);
        apply.setSyncUnitStatus("等待同步");
        apply.setIsRead(0);
        apply.setSyncUnit(0);
        apply.setSyncUnitStatus("等待同步");
        apply.setSyncUnitTotal(0);
        applyInfoService.insert(apply);
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    GudongSynchronousDataUtil.notifyGudong(orderCode);
                }
            }).start();
        } catch (Exception e) {
            log.info("同步咕咚报名订单{}信息报错{}", orderCode, e.toString());
        }

        return orderCode;
    }

    /**
     * 
        事务操作顺序
    register
    
    order,
    
    player
    appply
    leader
    
     * @see com.efida.sports.service.IPayOrderService#createDmpApply(com.efida.sports.entity.ApplyInfo, com.efida.sports.entity.LeaderInfo, java.util.List)
     */
    @Override
    @Transactional
    public String createDmpApply(ApplyInfo apply, LeaderInfo leader, List<Player> players) {
        if (players == null || players.size() < 1) {
            throw new BusinessException("运动员不能为空");
        }
        String phoneNum = "";
        boolean isGroup = EventTypeEnums.group.getCode().equals(apply.getEventType());
        if (isGroup) {
            phoneNum = leader.getLeaderPhone();
        }
        if (StringUtils.isBlank(phoneNum)) {
            phoneNum = players.get(0).getPlayerPhone();
        }
        if (StringUtils.isEmpty(phoneNum)) {
            throw new BusinessException("手机号不能为空");
        }
        RegisterModel register = ucenterRegsiterFacadeClient.getRegsiterByAccount(phoneNum);
        String idempotentId = apply.getIdempotentId();
        ApplyInfo applyInfo = applyInfoService.getApplyInfoIdempotentId(idempotentId);
        if (applyInfo != null) {
            applyInfo.setEventCode(apply.getEventCode());
            applyInfo.setEventName(apply.getEventName());
            applyInfo.setEventStartTime(apply.getEventStartTime());
            applyInfo.setEventEndTime(apply.getEventEndTime());
            applyInfo.setExpireTime(apply.getApplyTime());
            applyInfo.setGameCode(apply.getGameCode());
            applyInfo.setGameName(apply.getGameName());
            applyInfo.setGmtModified(new Date());
            applyInfo.setGroupEventCode(apply.getGroupEventCode());
            applyInfo.setIsDelet(0);
            applyInfo.setMatchCode(apply.getMatchCode());
            applyInfo.setMatchName(apply.getMatchName());
            applyInfo.setMatchGroupCode(apply.getMatchGroupCode());
            applyInfo.setMatchGroupName(apply.getMatchGroupName());
            applyInfo.setSiteCode(apply.getSiteCode());
            applyInfo.setSiteName(apply.getSiteName());
            applyInfo.setUnitCode(apply.getUnitCode());
            if (register != null) {
                applyInfo.setRegisterCode(register.getRegisterCode());
            }
            applyInfoService.updateById(applyInfo);
            PayOrder order = getOrderByCode(applyInfo.getPayOrderCode());
            if (order != null) {
                order.setPayTime(applyInfo.getApplyTime());
                order.setGmtModified(new Date());
                order.setOrderTime(applyInfo.getApplyTime());
                updateById(order);
            } else {
                log.warn("报名信息未找到对应的订单，报名编号：" + applyInfo.getApplyCode());
            }
            return "已经同步过,更新基本信息成功!";
        }
        //创建用户
        if (register == null) {
            register = new RegisterModel();
            register.setAccount(phoneNum);
            register.setRegTerminal(TerminalEnum.DMP);
            register.setRegTime(Calendar.getInstance().getTime());
            register.setLastLoginTime(new Date());
            register.setGmtCreate(new Date());
            register.setGmtModified(new Date());
            register.setRegisterCode(SeqWorkerUtil.generateTimeSequence());
            register.setChannelCode(apply.getUnitCode());
            ucenterRegisterFacadeClient.createRegsiter(register);
        }

        String registerCode = register.getRegisterCode();
        final String orderCode = SeqWorkerUtil.generateTimeSequence();
        final String applyCode = SeqWorkerUtil.generateTimeSequence();
        //2.创建订单数据
        Date time = Calendar.getInstance().getTime();
        PayOrder order = new PayOrder();
        order.setExpireTime(time);
        order.setRegisterCode(registerCode);
        order.setGmtCreate(time);
        order.setGmtModified(time);
        order.setOrderCode(orderCode);
        order.setOrderType(OrderTypeEnum.APPLY.getCode());
        order.setOrderTime(apply.getApplyTime());
        order.setPayTime(apply.getApplyTime());
        order.setOrderStatus(OrderStatusEnum.SUCCESS.getCode());
        order.setRemark("报名费用");
        order.setOrderAmount(apply.getApplyAmount());
        insert(order);
        for (Player player : players) {
            String playerPhone = player.getPlayerPhone();
            if (StringUtils.isEmpty(playerPhone)) {
                throw new BusinessException("运动员联系电话不能为空");
            }
            player.setGmtCreate(time);
            player.setGmtModified(time);
            player.setRegisterCode(registerCode);
            playerService.insertOrUpdate(player);
            ApplyPlayer applyPlayer = new ApplyPlayer();
            applyPlayer.setApplyCode(applyCode);
            applyPlayer.setPlayerCode(player.getPlayerCode());
            //创建中间表数据
            applyPlayerService.createApplyPlayer(applyPlayer);

        }

        apply.setApplyCode(applyCode);
        apply.setGmtCreate(time);
        apply.setGmtModified(time);
        apply.setIsDelet(0);
        apply.setPayOrderCode(orderCode);
        apply.setRegisterCode(registerCode);
        apply.setSync((byte) 1);
        applyInfoService.insert(apply);
        //创建领队信息
        if (isGroup) {
            leader.setRegisterCode(registerCode);
            leader.setApplyInfoCode(apply.getApplyCode());
            leader.setLeaderCode(SeqWorkerUtil.generateTimeSequence());
            leaderInfoService.insert(leader);
        }

        return "入库成功";
    }

    @Override
    public List<PayOrder> selectSettlementPayOrderList(Page<PayOrder> pagePayOrder,
                                                       Map<String, Object> params) {
        return payOrderMapper.selectSettlementPayOrderList(pagePayOrder, params);
    }

    @Override
    public List<PayOrder> selectSettlementPayOrderList(Map<String, Object> params) {
        return payOrderMapper.selectSettlementPayOrderList(params);
    }

    @Override
    public PayOrder selectSettlementPayOrderCount(Map<String, Object> params) {
        return payOrderMapper.selectSettlementPayOrderCount(params);
    }

    @Override
    public List<PayOrder> selectSettlementPayOrderDetail(Page<PayOrder> pagePayOrder,
                                                         Map<String, Object> params) {
        return payOrderMapper.selectSettlementPayOrderDetail(pagePayOrder, params);
    }

    /**
     *  删除顺序 : order, player applyPlayer ,apply_info  leader
     * @throws InterruptedException 
     */
    @Override
    public void processingRepetitionIdempotentId() throws InterruptedException {
        log.info("开始执行");
        int limit = 100;
        int num = 0;
        while (true) {
            long startTime = System.currentTimeMillis(); //获取开始时间

            List<String> idempotents = applyInfoService.selectRepetitionIdempotentId(limit);
            if (idempotents == null || idempotents.size() < 1) {
                return;
            }
            for (String idempotent : idempotents) {
                List<ApplyInfo> applyInfos = applyInfoService.applyInfosByidempotent(idempotent);
                //小于2 标识只有一条 不做操作
                if (applyInfos == null || applyInfos.size() < 2) {
                    continue;
                }
                //查询按修改时间倒序,删除跳过第一条
                for (int i = 1; i < applyInfos.size(); i++) {
                    ApplyInfo info = applyInfos.get(i);
                    delRepetitionData(info);
                }
            }
            long endTime = System.currentTimeMillis(); //获取开始时间
            num++;
            log.info(
                "执行记录数：" + idempotents.size() + "  ,花费时间时间： " + (endTime - startTime) / 1000 + "秒");
            log.info("执行修复数据程序,执行条数：" + num * limit);
            if (idempotents.size() < limit) {
                return;
            }
        }
    }

    @Override
    public void delRepetitionData(ApplyInfo info) {
        //删除订单order
        deleteOrderByApplyInfo(info);
        //删除player 和中间表
        deletePlayerByApplyInfo(info);
        //删除leader
        leaderInfoService.deleteByApplyCode(info.getApplyCode());
        //删除applyInfo
        applyInfoService.deleteById(info.getId());
    }

    private void deletePlayerByApplyInfo(ApplyInfo applyInfo) {
        if (applyInfo == null) {
            return;
        }
        String applyCode = applyInfo.getApplyCode();
        //删除运动员
        List<String> list = applyPlayerService.selectPlayerCodeByApplyCode(applyCode);
        if (list != null && list.size() > 0) {
            Wrapper<Player> wrapper = new EntityWrapper<>();
            wrapper.in("player_code", list);
            playerService.delete(wrapper);
        }

        //        playerService.deletPlayerByApplyInfoCode(applyCode);
        //删除中间表
        applyPlayerService.deleteByApplyCode(applyCode);

    }

    private void deleteOrderByApplyInfo(ApplyInfo applyInfo) {
        if (applyInfo == null) {
            return;
        }
        String orderCode = applyInfo.getPayOrderCode();
        PayOrder order = getOrderByCode(orderCode);
        if (order == null) {
            return;
        }
        deleteById(order.getId());
    }

    @Override
    public void repairRegisterCode(ApplyInfo info) {
        if (info == null || StringUtils.isNotBlank(info.getRegisterCode())) {
            return;
        }
        if (StringUtils.isBlank(info.getPayOrderCode())) {
            applyInfoService.deleteById(info.getId());
            return;
        }
        PayOrder payOrder = getOrderByCode(info.getPayOrderCode());
        if (payOrder == null) {
            applyInfoService.deleteById(info.getId());
            return;
        }
        info.setRegisterCode(payOrder.getRegisterCode());
        applyInfoService.updateById(info);

    }

}
