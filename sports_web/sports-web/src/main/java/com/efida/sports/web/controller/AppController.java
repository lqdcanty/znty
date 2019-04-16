package com.efida.sports.web.controller;

import com.alibaba.fastjson.JSON;
import com.efida.sport.admin.facade.model.*;
import com.efida.sport.admin.facade.model.open.MatchGroupItemModel;
import com.efida.sport.facade.enums.RegTerminalEnum;
import com.efida.sports.biz.GudongEnrollService;
import com.efida.sports.config.PlayerProConfig;
import com.efida.sports.entity.ApplyInfo;
import com.efida.sports.entity.PayOrder;
import com.efida.sports.entity.Player;
import com.efida.sports.enums.ApplyStatusEnum;
import com.efida.sports.enums.OrderStatusEnum;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.IApplyInfoService;
import com.efida.sports.service.IPayOrderService;
import com.efida.sports.service.IPlayerService;
import com.efida.sports.service.MsgService;
import com.efida.sports.service.dubbo.intergration.SpAthletesFacadeClient;
import com.efida.sports.service.dubbo.intergration.SpMatchFacadeClient;
import com.efida.sports.util.DateUtil;
import com.efida.sports.util.JsonResultUtil;
import com.efida.sports.util.SeqWorkerUtil;
import com.efida.sports.util.ServletUtil;
import com.efida.sports.web.vo.MatchDetailVo;
import com.efida.sports.web.vo.PayOrderDetailVo;
import com.efida.sports.web.vo.PlayerPropertyVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 咕咚h5对接
 * Created by wangyan on 2018/9/4.
 */
@RequestMapping("app")
@Controller
public class AppController {

    @Autowired
    private GudongEnrollService gudongEnrollService;

    @Autowired
    private SpMatchFacadeClient spMatchFacadeClient;

    @Autowired
    private IApplyInfoService applyInfoService;

    @Autowired
    private IPlayerService playerService;

    @Autowired
    private MsgService msgService;

    @Autowired
    private IPayOrderService   orderService;

    @Autowired
    private SpAthletesFacadeClient spAthletesFacadeClient;

    private static Logger log = LoggerFactory.getLogger(GameController.class);

    /**
     * 赛事详情页面
     *
     * @param model
     * @return
     */
    @RequestMapping("detail/{code}")
    public String detail(@PathVariable(value = "code") String code, Model model,HttpServletRequest request) {
        log.info("===================================app/detail===code="+code);
        SpMatchInfoModel matchDetail = null;
        try {
            matchDetail = spMatchFacadeClient.getMatchDetail(code);
        } catch (Exception e) {
            log.error("", e);
        }
        model.addAttribute("match", MatchDetailVo.getVo(matchDetail));
        Enumeration enu = request.getParameterNames();
        HashMap<String, Object> extProMap = new HashMap<String, Object>();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            extProMap.put(paraName, request.getParameter(paraName));
        }
        log.info("咕咚赛事页参数;{}", extProMap);
        return "pages/gudongCompetition";
    }

    @RequestMapping("item/{matchCode}")
    public String gudongEnter(@PathVariable(value = "matchCode") String matchCode,Model model,HttpServletRequest request) {
        SpAthletesEnrollModel athietes = null;
        List<MatchGroupItemModel> groups=null;
        try {
            athietes = spAthletesFacadeClient.getAthietes(matchCode);
            groups = spMatchFacadeClient.getMatchGroupItemList(matchCode);
            Enumeration enu = request.getParameterNames();
            HashMap<String, Object> extProMap = new HashMap<String, Object>();
            while (enu.hasMoreElements()) {
                String paraName = (String) enu.nextElement();
                extProMap.put(paraName, request.getParameter(paraName));
            }
            log.info("咕咚报名页参数;{}",extProMap);
            String strBackUrl = "http://" + request.getServerName() //服务器地址
                                + ":" + request.getServerPort()//端口号
                                + request.getContextPath()      //项目名称
                                + request.getServletPath()      //请求页面或其他地址
                                + "?" + (request.getQueryString()); //参数
            log.info("咕咚报名页参数strBackUrl;{}",strBackUrl);
         } catch (Exception e) {
            log.error("咕咚报名页参数获取报名参数信息错误{}", e);
        }
        List<PlayerPropertyVo> vos = PlayerPropertyVo.getVos(athietes);
        model.addAttribute("pros", vos);
        model.addAttribute("groups", groups);
        model.addAttribute("externalData", request.getParameter("external_data"));
        return "pages/gudongForm";
    }

    @RequestMapping("complete/order")
    public String gudongSuccess(String orderCode,String getMatchCode,Model model,HttpServletRequest request) {
        Enumeration enu = request.getParameterNames();
        HashMap<String, Object> extProMap = new HashMap<String, Object>();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            extProMap.put(paraName, request.getParameter(paraName));
        }
        log.info("咕咚成功页参数;{}",extProMap);
        try {
            PayOrder order = orderService.getOrderDetail(orderCode);
            if (order == null) {
                return "redirect:/item/"+getMatchCode;
            }
            ApplyInfo applyInfo = order.getApplyInfos().get(0);
            String matchCode = applyInfo.getMatchCode();
            SpMatchInfoModel matchDetail = spMatchFacadeClient.getMatchDetail(matchCode);
            MatchDetailVo matchVo = MatchDetailVo.getVo(matchDetail);
            SpAthletesEnrollModel athietes = null;
            String itemCode="";
            try {
                athietes = spAthletesFacadeClient.getAthietes(matchCode);
                String unitCode = applyInfo.getUnitCode();
                itemCode=applyInfo.getGroupEventCode();
                if (OrderStatusEnum.SUCCESS.getCode().equals(order.getOrderStatus())
                        && "lantianlvye".equals(unitCode)) {
                    List<FromTypeModel> enrollForm = athietes.getEnrollForm();
                    FromTypeModel fromType = new FromTypeModel();
                    fromType.setIsCustom(true);
                    fromType.setIsShow(true);
                    fromType.setIsRequired(true);
                    fromType.setName("参赛验证码");
                    fromType.setAttribute("参赛验证码");
                    enrollForm.add(fromType);
                }
            } catch (Exception e) {
                log.error("", e);
            }
            model.addAttribute("order", PayOrderDetailVo.getVo(order, athietes));
            model.addAttribute("match", matchVo);
            model.addAttribute("itemCode", itemCode);
        } catch (Exception e) {
            log.error("", e);
            // TODO: handle exception
        }
        return "pages/gudongSuccess";
    }

    @RequestMapping(value = "save/order", method = { RequestMethod.POST })
    @ResponseBody
    public Map<String, Object> saveOrder(String matchCode, String siteCode, String itemCode,
                                         @RequestParam(value = "externalData", required = false) String externalData,
                                         @RequestParam(value = "playerPhone", required = true) String playerPhone,
                                         @RequestParam(value = "verifyCode", required = true) String verifyCode,
                                         @RequestParam(value = "playerName", required = true) String playerName,
                                         @RequestParam(value = "sex", required = true) String sex,
                                         @RequestParam(value = "email", required = false) String email,
                                         @RequestParam(value = "playerHeight", required = false) String playerHeight,
                                         @RequestParam(value = "playerWeight", required = false) String playerWeight,
                                         @RequestParam(value = "playerBirth", required = false) String playerBirth,
                                         @RequestParam(value = "playerNationality", required = false) String playerNationality,
                                         @RequestParam(value = "playerAddress", required = false) String playerAddress,
                                         @RequestParam(value = "playerCerType", required = false) String playerCerType,
                                         @RequestParam(value = "playerCerNo", required = false) String playerCerNo,
                                         @RequestParam(value = "playerBloodType", required = false) String playerBloodType,
                                         @RequestParam(value = "playerNation", required = false) String playerNation,
                                         @RequestParam(value = "playerClothSize", required = false) String playerClothSize,
                                         @RequestParam(value = "playerWorkUnit", required = false) String playerWorkUnit,
                                         @RequestParam(value = "playerEmergencyName", required = false) String playerEmergencyName,
                                         @RequestParam(value = "playerEmergencyPhone", required = false) String playerEmergencyPhone,
                                         @RequestParam(value = "attOne", required = false) String attOne,
                                         @RequestParam(value = "attTwo", required = false) String attTwo,
                                         @RequestParam(value = "assettoId", required = false) String assettoId,
                                         @RequestParam(value = "attUrl", required = false) String attUrl,
                                         @RequestParam(value = "imgUrl", required = false) String imgUrl,
                                         Model model, HttpSession session, HttpServletRequest request) {

        try {
            Long number= applyInfoService.getApplyCountByEventCodeAndPhone(itemCode,playerPhone);
            if(  number != null && number > 0l ){
                return JsonResultUtil.getServerErrorResult("该手机号曾经已报名该比赛项，请不要重复报名!");
            }
            Player player = new Player();
            log.info("提交保存运动员信息：playerWeight:{},playerHeight:{},playerBirth：{},附件1：{},附件2:{}",
                    playerWeight, playerHeight, playerBirth, attOne, attTwo);
            String ip = ServletUtil.getIpAddress(request);
            if (StringUtils.isNotBlank(playerWeight)) {
                if (!isDouble(playerWeight)) {
                    return JsonResultUtil.getServerErrorResult("体重填写错误");
                }
                player.setPlayerWeight(Double.parseDouble(playerWeight));
            }
            if (StringUtils.isNotBlank(playerBirth)) {
                player.setPlayerBirth(DateUtil.strToDate1(playerBirth));
            }
            if (StringUtils.isNotBlank(playerHeight)) {
                player.setPlayerHeight(Integer.parseInt(playerHeight));
            }
            List<String> playerPro = PlayerProConfig.getInstance().getPlayerPro();
            Enumeration enu = request.getParameterNames();
            HashMap<String, Object> extProMap = new HashMap<String, Object>();
            while (enu.hasMoreElements()) {
                String paraName = (String) enu.nextElement();
                if (playerPro.contains(paraName) || "matchCode".equals(paraName) || "itemCode".equals(paraName) || "siteCode".equals(paraName)) {
                    continue;
                }
                extProMap.put(paraName, request.getParameter(paraName));
            }
            player.setExtProMap(extProMap);
            player.setPlayerAddress(playerAddress);
            player.setEmail(email);
            player.setImgUrl(imgUrl);
            player.setSex(sex);
            player.setPlayerAddress(playerAddress);
            player.setPlayerBloodType(playerBloodType);
            player.setPlayerCerNo(playerCerNo);
            player.setPlayerCerType(playerCerType);
            player.setPlayerClothSize(playerClothSize);
            player.setPlayerEmergencyName(playerEmergencyName);
            player.setPlayerEmergencyPhone(playerEmergencyPhone);
            player.setPlayerName(playerName);
            player.setPlayerNation(playerNation);
            player.setPlayerNationality(playerNationality);
            player.setPlayerPhone(playerPhone);
            player.setPlayerWorkUnit(playerWorkUnit);
            player.setVerifyCode(verifyCode);
            player.setAttOne(attOne);
            player.setAttTwo(attTwo);
            player.setAttUrl(attUrl);
            player.setAssettoId(assettoId);
            playerService.checkPlayer(player);
            boolean checkVerifyCode = msgService.checkVerifyCode(playerPhone, verifyCode);
            if (!checkVerifyCode) {
                return JsonResultUtil.getServerErrorResult("验证码错误，请重新输入");
            }
            ApplyInfo applyInfo = checkApplyInfo(siteCode,itemCode); //验证报名信息
            applyInfo.setExternalData(externalData);
            String orderCode = gudongEnrollService.createAppyInfo(applyInfo,player, RegTerminalEnum.apph5, ip);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderCode", orderCode);
            return JsonResultUtil.getSuccessResult(map);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("咕咚报名失败");
        }
    }

    public Player getPlayer(){
        Player player = new Player();
        return player;
    }

    /**
     *  验证报名信息
     * @param siteCode
     * @param itemCode
     * @return
     */
    public ApplyInfo checkApplyInfo(String siteCode,String itemCode){
        ApplyInfo apply = new ApplyInfo();
        SpMatchAllInfoModel item = spMatchFacadeClient.getItemDetail(siteCode, itemCode);
        if (item == null) {
            throw new BusinessException("比赛项不存在,请重新选择");
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
        SpMatchGroupModel groupInfo = item.getGroupInfo();
        SpMatchModel matchInfo = item.getMatchInfo();
        SpPlayingAreaModel siteInfo = item.getPayAreaInfo();
        SpProjectTypeModel typeInfo = item.getProjectTypeInfo();

        apply.setApplyAmount(Long.valueOf(itemInfo.getEntryFee()));
        apply.setApplyCode(SeqWorkerUtil.generateTimeSequence());
        apply.setApplyStatus(ApplyStatusEnum.SUCCESS.getCode());
        apply.setEventCode(itemInfo.getEventCode());
        apply.setGroupEventCode(itemInfo.getItemCode());
        apply.setEventEndTime(itemInfo.getEndTime());
        apply.setEventName(itemInfo.getItemName());
        apply.setEventStartTime(itemInfo.getStartTime());
        SpGroupItemModel groupItemInfo = item.getGroupItemInfo();
        apply.setEventType(groupItemInfo.getItemType());
        apply.setRegistrationNum(groupItemInfo.getGroupLimit());
        apply.setChannelCode(RegTerminalEnum.WEICHAT.getCode());
        apply.setExpireTime(new Date());
        apply.setApplyTime(new Date());
        if (typeInfo != null) {
            apply.setGameCode(typeInfo.getGameCode());
            apply.setGameName(typeInfo.getGameName());
        }
        if (matchInfo != null) {
            apply.setMatchCode(matchInfo.getMatchCode());
            apply.setMatchName(matchInfo.getMatchName());
            apply.setUnitCode(matchInfo.getUnitCode());
        }
        if (groupInfo != null) {
            apply.setMatchGroupCode(groupInfo.getAreaGroupCode());
            apply.setMatchGroupName(groupInfo.getGroupName());
        }

        if (siteInfo != null) {
            apply.setSiteCode(siteInfo.getFieldCode());
            apply.setSiteName(siteInfo.getFieldName());
        }
        return apply;
    }

    private Boolean isDouble(String str) {
        try {
            if(Double.parseDouble(str)>0){
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
            // TODO: handle exception
        }
    }

}
