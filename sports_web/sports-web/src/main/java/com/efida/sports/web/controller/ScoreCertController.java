package com.efida.sports.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.sport.dmp.facade.model.ScoreCertModel;
import com.efida.sport.dmp.facade.result.DmpPageResult;
import com.efida.sports.enums.ErrorCodeEnum;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.UcenterAdapterService;
import com.efida.sports.service.dubbo.intergration.ScoreCertFacadeClient;
import com.efida.sports.util.JsonResultUtil;

@RequestMapping("score/cert")
@Controller
public class ScoreCertController {

    @Autowired
    private ScoreCertFacadeClient scoreCertFacadeClient;

    @Autowired
    private UcenterAdapterService ucenterAdapterService;

    private static Logger         log = LoggerFactory.getLogger(ScoreCertController.class);

    /**
     * 获取用户最新未查看的证书
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getScoreCert", method = { RequestMethod.GET })
    public Map<String, Object> getScoreCert(HttpSession session) {
        try {
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            String registerCode = auth.getRegisterCode();
            ScoreCertModel scoreCertModel = scoreCertFacadeClient
                .getRegisterNewestScoreCert(registerCode);
            if (scoreCertModel == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(),
                    "不存在未查看的证书");
            }
            scoreCertFacadeClient.viewScoreCert(scoreCertModel.getScoreCertCode());
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("scoreCert", scoreCertModel);
            hashMap.put("share", getShare());
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取未查看证书失败,请稍后重试！！！");
        }
    }

    /**
     * 获取用户的证书
     * 获取已查看和未查看的证书
     *  分页查询
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getScoreCertList", method = { RequestMethod.GET })
    public Map<String, Object> getScoreCertList(@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                                HttpSession session) {

        try {
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            DmpPageResult<ScoreCertModel> result = scoreCertFacadeClient
                .getRegisterPageScoreCert(auth.getRegisterCode(), currentPage, pageSize);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("scoreCert", result.getList());
            hashMap.put("pageSize", result.getPageSize());
            hashMap.put("currentPage", result.getCurrentPage());
            hashMap.put("allRow", result.getAllRow());
            hashMap.put("share", getShare());
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取比赛日期失败,请稍后重试！！！");
        }
    }

    private Map<String, Object> getShare() {
        Map<String, Object> map = new HashMap<>();
        map.put("title", "首届全国智能体育大赛");
        map.put("content", "我已成功完成首届全国智能体育大赛项目，快来和我一起领取奖章吧。");
        //map.put("shareUrl", "http://zntywx.efida.com.cn/game/type");
        map.put("shareUrl", "http://wx.zntyydh.com/game/type");
        map.put("picUrl",
            "http://zntyfdfs.efida.com.cn/group1/M00/00/10/rBADF1ugs8SATDpNAABNjjKZHYo219.png");
        return map;
    }

    /**
     * 修改证书状态
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updeteCertStatus", method = { RequestMethod.POST })
    public Map<String, Object> updeteCertStatus(@RequestParam(value = "certCode", required = true) String certCode) {
        log.info("*****************************updeteCertStatus      certCode=" + certCode);
        try {
            Boolean isUpdate = scoreCertFacadeClient.viewScoreCert(certCode);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("isUpdate", isUpdate);
            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取比赛日期失败,请稍后重试！！！");
        }
    }

}
