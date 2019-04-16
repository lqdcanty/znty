package com.efida.sports.test;

import com.efida.sports.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by wangyan on 2018/11/1.
 */
public class MsgServiceTest  extends BaseTest  {


    @Autowired
    private MsgService msgService;

//    @Test
    public  void  sendSuccessMessage(){
        String matchName="测试身份信息";
        String groupName="测试";
        String mobile="15801297662";
        String itemName="单人项";
        Date startTime=new Date();
        String address="河北省唐山市开平区";
        Date endTime=new Date();
        String partCode="";
        msgService.sendSuccessMessage(matchName,groupName,mobile,itemName, startTime, address, endTime, partCode);
    }

}
