/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sport.facade.service;

import com.efida.sport.facade.enums.GenderEnum;
import com.efida.sport.facade.enums.PlatformEnum;
import com.efida.sport.facade.enums.RegTerminalEnum;
import com.efida.sport.facade.model.DefaultResult;
import com.efida.sport.facade.model.PagingResult;
import com.efida.sport.facade.model.RegisterModel;

/**
 * 
 * @author zoutao
 * @version $Id: registerFacadeService.java, v 0.1 2018年8月5日 下午1:40:53 zoutao Exp $
 */
public interface RegisterFacadeService {
    /**
     * 根据手机号查询用户
     * 用户不存在 会自动创建一条    dmp平台调用
     * 
     * @param phoneNum
     * @return
     */
    DefaultResult<RegisterModel> getOrInsertRegisterByPhone(String phoneNum);

    /**
     * 根据条件查询用户记录
     * 
     * @param nickName  昵称 ：模糊匹配
     * @param reanlName 真实姓名：模糊匹配
     * @param reanlName 电话号码 
     * 
     * @param platform  第三方平台来源
     * @param gender    性别
     * @param regTerm   注册来源   
     * @param sort       排序字段
     * @param isAsc      是否升序 true:升序 ,false：降序
     * @param currentPage 当前页数
     * @param PageSize    每页大小
     * @return
     */

    DefaultResult<PagingResult<RegisterModel>> queryRegisterModel(String nickName, String reanlName,
                                                                  String phoneNum,
                                                                  PlatformEnum platform,
                                                                  GenderEnum gender,
                                                                  RegTerminalEnum regTerm,
                                                                  String sort, boolean isAsc,
                                                                  int currentPage, int PageSize);

}
