//package com.efida.sports.test;
//
//import com.efida.sports.biz.GudongEnrollService;
//import com.efida.sports.entity.GudongMatch;
//import com.efida.sports.service.GudongMatchService;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by wangyan on 2018/9/10.
// */
//public class GudongEnrollServiceTest extends BaseTest {
//
//    @Autowired
//    private GudongEnrollService gudongEnrollService;
//
//    @Autowired
//    private GudongMatchService gudongMatchService;
//
//    @Test
//    public void notifyGudong() {
//        final String orderCode="20180907094921hd6h01";
//        gudongEnrollService.notifyGudong(orderCode);
//    }
//
//    @Test
//    public void getAllData() {
//        Map<String,String> map=new HashMap<String,String>();
//        List<GudongMatch> list = gudongMatchService.getAllData();
//        for (GudongMatch  gudongMatch: list){
//            map.put(gudongMatch.getCode(),gudongMatch.getGudongCode());
//        }
//        System.out.println("打印map集合=" + map);
//    }
//
//}
