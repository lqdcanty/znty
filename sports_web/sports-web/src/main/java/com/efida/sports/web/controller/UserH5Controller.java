package com.efida.sports.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.efida.easy.ucenter.facade.constants.UcenterConstants;
import com.efida.easy.ucenter.facade.enums.GenderEnum;
import com.efida.easy.ucenter.facade.model.AuthToken;
import com.efida.easy.ucenter.facade.model.RegisterModel;
import com.efida.sports.constants.Constants;
import com.efida.sports.enums.ApplyStatusEnum;
import com.efida.sports.enums.ErrorCodeEnum;
import com.efida.sports.model.MessageInfoModel;
import com.efida.sports.service.IApplyInfoService;
import com.efida.sports.service.MsgService;
import com.efida.sports.service.UcenterAdapterService;
import com.efida.sports.service.dubbo.intergration.UcenterRegisterFacadeClient;
import com.efida.sports.util.JsonResultUtil;
import com.efida.sports.web.util.CookiesUtil;
import com.efida.sports.web.vo.RegisterVo;

/**
 * Created by wnagyan on 2018/7/18.
 */
@RequestMapping("user")
@Controller
public class UserH5Controller {

    @Autowired
    private IApplyInfoService           iApplyInfoService;

    @Autowired
    private MsgService                  msgService;

    @Autowired
    private UcenterAdapterService       ucenterAdapterService;
    @Autowired
    private UcenterRegisterFacadeClient registerFacadeClient;

    @Value("${ucenter-domain}")
    public String                       ucenterDomain;
    @Value("${apply-domain}")
    public String                       applyDomain;

    @RequestMapping("register/card")
    public String registerCard() {
        return "pages/register_card";
    }

    private static Logger log = LoggerFactory.getLogger(UserH5Controller.class);

    /**
     * 获取登陆状态
     * @param token
     */
    @ResponseBody
    @RequestMapping(value = "register/check", method = { RequestMethod.GET })
    public Map<String, Object> check(@RequestParam(value = "token", required = true) String token,
                                     @RequestParam(value = "t", required = true) String t,
                                     HttpSession session) {
        RegisterModel register = ucenterAdapterService.getAppAuthRegister(token);
        if (register == null) {
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            register = auth.getRegister();
        }
        if (register == null) {
            return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
        } else {
            RegisterModel user = registerFacadeClient
                .getRegsiterByRegisterCode(register.getRegisterCode());
            if (user == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.SERVER_ERROR.getCode(), "用户不存在");
            }
            Map<String, Object> resultObject = new HashMap<>();
            resultObject.put("headimgUrl", user.getHeadimgUrl());
            return JsonResultUtil.getSuccessResult("用户登录", resultObject);
        }
    }

    @ResponseBody
    @RequestMapping("get/card")
    public Map<String, Object> getCard(@RequestParam(value = "token", required = true) String token,
                                       HttpSession session) {
        //获取当前用户
        log.info("get/card--------------token" + token);
        RegisterModel register = ucenterAdapterService.getAppAuthRegister(token);
        if (register == null) {
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            register = auth.getRegister();
        }
        RegisterModel user = registerFacadeClient
            .getRegsiterByRegisterCode(register.getRegisterCode());
        log.info("get/card--------------user" + user.getAccount() + "    " + user.getNickName());
        if (user == null) {
            return JsonResultUtil.getFailResult(ErrorCodeEnum.SERVER_ERROR.getCode(), "用户未存在");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("register", RegisterVo.getVo(user));
        return JsonResultUtil.getSuccessResult(map);

    }

    @ResponseBody
    @RequestMapping("save/card")
    public Map<String, Object> saveCard(@RequestParam(value = "token", required = true) String token,
                                        @RequestParam(value = "realName", required = true) String realName,
                                        @RequestParam(value = "gender", required = true) String gender,
                                        @RequestParam(value = "verifyCode", required = true) String verifyCode,
                                        @RequestParam(value = "phone", required = true) String phone,
                                        HttpSession session) {
        //获取当前用户
        RegisterModel register = ucenterAdapterService.getAppAuthRegister(token);
        if (register == null) {
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            register = auth.getRegister();
        }
        if (register != null) {
            RegisterModel user = registerFacadeClient
                .getRegsiterByRegisterCode(register.getRegisterCode());
            if (user == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.SERVER_ERROR.getCode(), "用户未存在");
            }
            if (!user.getAccount().equalsIgnoreCase(phone)) {
                boolean checkVerifyCode = msgService.checkVerifyCode(phone, verifyCode);
                if (!checkVerifyCode) {
                    return JsonResultUtil.getServerErrorResult("验证码错误，请重新输入");
                }
            }
            GenderEnum genDerenum = GenderEnum.getEnumByCode(gender);
            user.setGender(genDerenum);
            user.setRealName(realName);
            user.setPhone(phone);
            registerFacadeClient.updateRegsiterInfo(user);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("register", RegisterVo.getVo(user));
            return JsonResultUtil.getSuccessResult(map);
        } else {
            return JsonResultUtil.getServerErrorResult("用户未登录");
        }
    }

    @RequestMapping(value = "wx/update", method = { RequestMethod.POST })
    @ResponseBody
    public Map<String, Object> updateRegister(HttpSession session,
                                              @RequestParam(value = "headimgUrl", required = false) String headimgUrl,
                                              @RequestParam(value = "nickName", required = false) String nickName) {
        log.info("get/card--------------wx/update     headimgUrl" + headimgUrl);
        log.info("get/card--------------wx/update      nickName" + nickName);
        try {

            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            RegisterModel user = registerFacadeClient
                .getRegsiterByRegisterCode(auth.getRegisterCode());
            if (user == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.SERVER_ERROR.getCode(), "用户未存在");
            }
            if (StringUtils.isNotBlank(headimgUrl)) {
                user.setHeadimgUrl(headimgUrl);
            }
            if (StringUtils.isNotBlank(nickName)) {
                user.setNickName(nickName);
            }
            registerFacadeClient.updateRegsiterInfo(user);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("register", RegisterVo.getVo(user));
            map.put("token", user.getToken());
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            return JsonResultUtil.getServerErrorResult("更新用户信息失败");
        }
    }

    /**
     * 退出登录
     *
     * @param session
     * @return
     */
    @RequestMapping("wx/quit/login")
    @ResponseBody
    public Map<String, Object> quitLogin(HttpSession session, HttpServletResponse response) {
        try {

            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth != null) {
                session.removeAttribute(UcenterConstants.UCENTER_H5_AUTH_TOKEN);
            }
            CookiesUtil.setCookie(response, Constants.COOKIE_AUTH_TOKEN, "", 0);
            return JsonResultUtil.getSuccessResult("退出登录");
        } catch (Exception e) {
            return JsonResultUtil.getServerErrorResult("退出登录失败！");
        }
    }

    /**
     * 获取用户报名成功信息
     * @param currentPage
     * @param pageSize
     * @param session
     * @return
     */
    @RequestMapping(value = "getApplyInfo", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> getApplyInfo(@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                            HttpSession session) {
        try {
            //获取当前用户
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            List<MessageInfoModel> list = iApplyInfoService.selectApplyInfo(auth.getRegisterCode(),
                ApplyStatusEnum.SUCCESS.getCode(), currentPage, pageSize);
            Integer numbers = iApplyInfoService.selectIsReadNum(auth.getRegisterCode(),
                ApplyStatusEnum.SUCCESS.getCode(), "");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("applyInfo", list);
            map.put("total", numbers);
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            return JsonResultUtil.getServerErrorResult("获取用户报名成功信息失败");
        }
    }

    /**
     * 获取用户报名成功未读消息数
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "unreadMessage", method = { RequestMethod.GET })
    public Map<String, Object> unreadMessage(HttpSession session) {
        try {
            //获取当前用户
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            Integer numbers = iApplyInfoService.selectIsReadNum(auth.getRegisterCode(),
                ApplyStatusEnum.SUCCESS.getCode(), "1");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("numbers", numbers);
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            return JsonResultUtil.getServerErrorResult("获取用户报名成功未读消息数失败");
        }
    }

    /**
     * 更新用户报名成功阅读消息
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateMessage", method = { RequestMethod.GET })
    public Map<String, Object> updateMessage(HttpSession session) {
        try {
            //获取当前用户
            AuthToken auth = ucenterAdapterService.getH5AuthToken(session);
            if (auth == null) {
                return JsonResultUtil.getFailResult(ErrorCodeEnum.NOT_LOGGED.getCode(), "用户未登录");
            }
            Integer numbers = iApplyInfoService.updateIsRead(auth.getRegisterCode(),
                ApplyStatusEnum.SUCCESS.getCode(), "1", "0");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("numbers", numbers);
            return JsonResultUtil.getSuccessResult(map);
        } catch (Exception e) {
            return JsonResultUtil.getServerErrorResult("更新用户报名成功阅读消息失败");
        }
    }
}
