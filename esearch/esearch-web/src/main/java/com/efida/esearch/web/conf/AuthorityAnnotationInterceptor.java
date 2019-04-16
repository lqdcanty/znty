package com.efida.esearch.web.conf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.efida.esearch.exception.EbusinessException;
import com.efida.esearch.service.intergration.auth.EAuthService;
import com.efida.esearch.service.intergration.auth.EUserService;
import com.efida.esearch.utils.ServletUtil;

import cn.evake.auth.dubbo.open.annotation.Authority;
import cn.evake.auth.dubbo.open.annotation.AuthorityTypeEnum;
import cn.evake.auth.dubbo.open.utils.AuthAnnotationUtil;
import cn.evake.auth.dubbo.open.utils.OpenUserInfoUtil;

/**
 * 权限认证拦截器
 * @author wang yi
 * @desc 
 * @version $Id: AuthorityAnnotationInterceptor.java, v 0.1 2018年2月22日 下午7:20:12 wang yi Exp $
 */
public class AuthorityAnnotationInterceptor extends HandlerInterceptorAdapter {

    private Logger       logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EUserService userService;

    @Autowired
    private EAuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        logger.debug("用户请求--->" + requestURI);
        //是否放行用户
        boolean isPermit = false;
        if (handler instanceof HandlerMethod) {
            Authority authority = AuthAnnotationUtil.getAnthAnnotation((HandlerMethod) handler);
            if (authority != null) {
                if (AuthorityTypeEnum.NoAuthority == authority.value()) {
                    // 不验证权限，不验证是否登录
                    isPermit = true;
                }
                if (AuthorityTypeEnum.NoValidate == authority.value()) {
                    // 验证权限，验证是否登录
                    isPermit = authService.checkIsLogin(OpenUserInfoUtil.getUserUid(request),
                        OpenUserInfoUtil.getUserAuthToken(request));
                }
                //当开启验证以及校验 去校验数据
                if (AuthorityTypeEnum.Validate == authority.value()) {
                    // 验证登录及权限
                    isPermit = authService.checkIsLimit(OpenUserInfoUtil.getUserUid(request),
                        authority.permissionCode());
                }
            } else {
                isPermit = true;
            }
            // 未通过验证
            if (!isPermit) {
                throw new EbusinessException("非法访问");
            }
        }
        return true;
    }

    /** 
     * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse reponse, Object handler,
                           ModelAndView model) throws Exception {
        final String ctx = ServletUtil.getCtx(request);
        request.setAttribute("ctx", ctx);
    }

    /** 
     * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse, Object o,
                                Exception e) throws Exception {

    }
}