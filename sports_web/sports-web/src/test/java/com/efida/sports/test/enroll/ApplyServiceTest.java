///**
// * efida.com.cn Inc.
// * Copyright (c) 2004-2018 All Rights Reserved.
// */
//package com.efida.sports.test.enroll;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.baomidou.mybatisplus.plugins.Page;
//import com.efida.sports.entity.ApplyInfo;
//import com.efida.sports.entity.PayOrder;
//import com.efida.sports.exception.BusinessException;
//import com.efida.sports.service.IApplyInfoService;
//import com.efida.sports.service.IPayOrderService;
//import com.efida.sports.test.BaseTest;
//
///**
// *
// * @author zhiyang
// * @version $Id: ApplyServiceTest.java, v 0.1 2018年8月2日 下午7:19:18 zhiyang Exp $
// */
//public class ApplyServiceTest extends BaseTest {
//
//    @Autowired
//    private IPayOrderService  iPayOrderService;
//    @Autowired
//    private IApplyInfoService iApplyInfoService;
//
//    @Test
//    public void testPayOrder() {
//        Page<PayOrder> result = iPayOrderService.selectEnrollInfoByStatus(1, 2, "2718777635702784",
//            null);
//        Assert.assertTrue(result != null);
//    }
//
//    @Test
//    public void testQuery() {
//
//        String matchCode = "match201806071138466020";
//        String siteCode = "field201806071141221766";
//        Set<String> phones = new HashSet<String>();
//        phones.add("18628380221");
//        ApplyInfo info = new ApplyInfo();
//
//        try {
//            info.setMatchCode(matchCode);
//            info.setSiteCode(siteCode);
//            info.setApplyCode("201806071902218546");
//            List<ApplyInfo> items = this.iApplyInfoService
//                .queryApplyInfoByPhoneAndMachInfo(matchCode, siteCode, phones);
//            Assert.assertTrue(items != null);
//        } catch (Exception ex) {
//            Assert.fail("查询失败");
//        }
//        try {
//
//            this.iPayOrderService.checkUniteStrategy("qixing", info);
//        } catch (BusinessException ex) {
//
//        }
//    }
//
//}
