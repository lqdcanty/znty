package com.efida.sports.app.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sport.dmp.facade.model.ScoreCertModel;
import com.efida.sport.dmp.facade.result.DmpPageResult;
import com.efida.sports.enums.ErrorCodeEnum;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.service.dubbo.intergration.ScoreCertFacadeClient;
import com.efida.sports.service.dubbo.intergration.UcenterLoginFacadeClient;
import com.efida.sports.util.JsonResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController()
@Api(value = "certApi", tags = { "证书相关接口" })
@RequestMapping(value = "/api/cert", produces = "application/json")
public class ScoreCertController {

    private static Logger            log = LoggerFactory.getLogger(ScoreCertController.class);

    @Autowired
    private ScoreCertFacadeClient    scoreCertFacadeClient;

    @Autowired
    private UcenterLoginFacadeClient loginServiceClient;

    /**
     * 获取用户的证书
     * 获取已查看和未查看的证书
     *  分页查询
     * @param token
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取证书列表", notes = "根据token获取我的证书列表")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "currentPage", value = "当前页数(不传默认取第一页)", required = false, dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "每页大小(不传默认为10)", required = false, dataType = "int", paramType = "query") })
    @RequestMapping(value = "getScoreCertList", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> getScoreCertList(@RequestParam(value = "token", required = true) String token,
                                                @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        log.info("**************************************getScoreCertList      token=" + token);

        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            DmpPageResult<ScoreCertModel> result = scoreCertFacadeClient
                .getRegisterPageScoreCert(register.getRegisterCode(), currentPage, pageSize);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("scoreCert", result.getList());
            hashMap.put("pageSize", result.getPageSize());
            hashMap.put("currentPage", result.getCurrentPage());
            hashMap.put("allRow", result.getAllRow());
            hashMap.put("share", getShare());
            hashMap.put("totalPage", result.getTotalPage());

            return JsonResultUtil.getSuccessResult(hashMap);
        } catch (BusinessException e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult(e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResultUtil.getServerErrorResult("获取证书列表失败,请稍后重试！！！");
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
    @ApiOperation(value = "修改证书状态", notes = "查看后修改证书的状态")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "certCode", value = "证书编号", required = true, dataType = "String", paramType = "form") })
    @RequestMapping(value = "updeteCertStatus", method = { RequestMethod.POST })
    @ResponseBody
    public Map<String, Object> updeteCertStatus(@RequestParam(value = "certCode", required = true) String certCode) {
        log.info(
            "**************************************updeteCertStatus      certCode=" + certCode);

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
            return JsonResultUtil.getServerErrorResult("修改证书失败,请稍后重试！！！");
        }
    }

    /**
     * 获取用户最新未查看的证书
     * @return
     */
    @ApiOperation(value = "获取未查看证书", notes = "根据token获取用户未查看证书")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query") })
    @RequestMapping(value = "getScoreCert", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> getScoreCert(@RequestParam(value = "token", required = true) String token) {

        log.info("**************************************getScoreCert      token=" + token);

        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            String registerCode = register.getRegisterCode();
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
}
