package com.efida.sports.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.efida.easy.ucenter.facade.enums.GenderEnum;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sports.app.vo.RegisterVo;
import com.efida.sports.enums.ApplyStatusEnum;
import com.efida.sports.enums.ErrorCodeEnum;
import com.efida.sports.model.MessageInfoModel;
import com.efida.sports.service.IApplyInfoService;
import com.efida.sports.service.MsgService;
import com.efida.sports.service.dubbo.intergration.UcenterLoginFacadeClient;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;
import com.efida.sports.util.JsonResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Created by wangyan on 2018/7/23.
 */
@RestController()
@Api(value = "userApi", tags = { "用户相关接口" })
@RequestMapping(value = "/api/user", produces = "application/json")
public class UserController {

    @Autowired
    private UcenterLoginFacadeClient    loginServiceClient;

    @Autowired
    private UcenterRegisterFacadeClient regsiterFacadeClient;

    @Autowired
    private IApplyInfoService           iApplyInfoService;

    @Autowired
    private MsgService                  msgService;

    @RequestMapping(value = "get", method = { RequestMethod.GET })
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息，前置条件：用户必须为登录状态下")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query") })
    @ResponseBody
    public Map<String, Object> getRegister(@RequestParam(value = "token", required = true) String token) {
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            register = regsiterFacadeClient.getRegsiterByRegisterCode(register.getRegisterCode());
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.SERVER_ERROR.getCode(), "用户不存在");
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("register", RegisterVo.getVo(register));
            map.put("token", register.getToken());
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            return JsonResultUtil.getServerErrorResult("获取用户信息失败");
        }
    }

    @RequestMapping(value = "update", method = { RequestMethod.POST })
    @ApiOperation(value = "更新用户信息", notes = "更新用户信息，前置条件：用户必须为登录状态下")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "headimgUrl", value = "用户头像", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "nickName", value = "用户昵称", required = false, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query") })
    @ResponseBody
    public Map<String, Object> updateRegister(@RequestParam(value = "headimgUrl", required = false) String headimgUrl,
                                              @RequestParam(value = "nickName", required = false) String nickName,
                                              @RequestParam(value = "token", required = true) String token) {
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            String registerCode = register.getRegisterCode();
            register = regsiterFacadeClient.getRegsiterByRegisterCode(registerCode);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户不存在");
            }
            register.setNickName(nickName);
            register.setHeadimgUrl(headimgUrl);
            register = regsiterFacadeClient.updateRegsiterInfo(register);
            loginServiceClient.refreshAppToken(token, register);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("register", RegisterVo.getVo(register));
            map.put("token", token);
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            return JsonResultUtil.getServerErrorResult("更新用户信息失败");
        }
    }

    @RequestMapping(value = "save/card", method = { RequestMethod.POST })
    @ApiOperation(value = "更新用户报名卡信息", notes = "更新用户报名卡信息，前置条件：用户必须为登录状态下")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "realName", value = "真实姓名", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "gender", value = "性别(1:男,2:女,3:未知)", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "verifyCode", value = "验证码", required = true, dataType = "String", paramType = "form"),
                         @ApiImplicitParam(name = "phone", value = "手机号码", required = true, dataType = "String", paramType = "form") })
    @ResponseBody
    public Map<String, Object> saveCard(@RequestParam(value = "token", required = true) String token,
                                        @RequestParam(value = "realName", required = true) String realName,
                                        @RequestParam(value = "gender", required = true) String gender,
                                        @RequestParam(value = "verifyCode", required = true) String verifyCode,
                                        @RequestParam(value = "phone", required = true) String phone) {
        //获取当前用户
        RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
        if (register != null) {
            String registerCode = register.getRegisterCode();
            register = regsiterFacadeClient.getRegsiterByRegisterCode(registerCode);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.SERVER_ERROR.getCode(), "用户未存在");
            }
            boolean checkVerifyCode = msgService.checkVerifyCode(phone, verifyCode);
            if (!checkVerifyCode) {
                return JsonResultUtil.getServerErrorResult("验证码错误，请重新输入");
            }
            register.setGender(GenderEnum.getEnumByCode(gender));
            register.setRealName(realName);
            register.setPhone(phone);
            regsiterFacadeClient.updateRegsiterInfo(register);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("register", RegisterVo.getVo(register));
            map.put("token", register.getToken());
            return JsonResultUtil.getSuccessResult(map);
        } else {
            return JsonResultUtil.getServerErrorResult("用户未登录");
        }
    }

    @RequestMapping(value = "getApplyInfo", method = { RequestMethod.GET })
    @ApiOperation(value = "获取用户报名成功信息", notes = "获取用户报名成功信息，前置条件：用户必须为登录状态下")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query"),
                         @ApiImplicitParam(name = "currentPage", value = "当前页数(不传默认取第一页)", required = false, dataType = "int", paramType = "query"),
                         @ApiImplicitParam(name = "pageSize", value = "每页大小(不传默认为10)", required = false, dataType = "int", paramType = "query") })
    @ResponseBody
    public Map<String, Object> getApplyInfo(@RequestParam(value = "token", required = true) String token,
                                            @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            List<MessageInfoModel> list = iApplyInfoService.selectApplyInfo(
                register.getRegisterCode(), ApplyStatusEnum.SUCCESS.getCode(), currentPage,
                pageSize);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("applyInfo", list);
            map.put("token", token);
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            return JsonResultUtil.getServerErrorResult("获取用户报名成功信息失败");
        }
    }

    @RequestMapping(value = "unreadMessage", method = { RequestMethod.GET })
    @ApiOperation(value = "获取用户报名成功未读消息数", notes = "获取用户报名成功未读消息数，前置条件：用户必须为登录状态下")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query") })
    @ResponseBody
    public Map<String, Object> unreadMessage(@RequestParam(value = "token", required = true) String token) {
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            Integer numbers = iApplyInfoService.selectIsReadNum(register.getRegisterCode(),
                ApplyStatusEnum.SUCCESS.getCode(), "1");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("numbers", numbers);
            map.put("token", token);
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            return JsonResultUtil.getServerErrorResult("获取用户报名成功未读消息数失败");
        }
    }

    @RequestMapping(value = "updateMessage", method = { RequestMethod.GET })
    @ApiOperation(value = "更新用户报名成功阅读消息", notes = "更新用户报名成功阅读消息，前置条件：用户必须为登录状态下")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "调用成功"),
                            @ApiResponse(code = 500, message = "接口系统异常") })
    @ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "query") })
    @ResponseBody
    public Map<String, Object> updateMessage(@RequestParam(value = "token", required = true) String token) {
        try {
            RegisterModel register = loginServiceClient.getRegisterByAppToken(token);
            if (register == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            Integer numbers = iApplyInfoService.updateIsRead(register.getRegisterCode(),
                ApplyStatusEnum.SUCCESS.getCode(), "1", "0");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("numbers", numbers);
            map.put("token", token);
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            return JsonResultUtil.getServerErrorResult("更新用户报名成功阅读消息失败");
        }
    }

}
