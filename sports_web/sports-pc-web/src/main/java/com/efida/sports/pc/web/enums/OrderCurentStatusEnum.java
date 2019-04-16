/**
 * efida.com.cn Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.efida.sports.pc.web.enums;

/**
 * 
 * 
 * @author zengbo
 * @version $Id: OrderStatusEnum.java, v 0.1 2018年6月24日 下午4:29:28 zengbo Exp $
 */
public enum OrderCurentStatusEnum {
                                   /**
                                    * 待完善
                                    */
                                   WAIT_PERFECT("wait_perfect", "待完善"),

                                   /**
                                    * 赛事暂停
                                    */
                                   MATCH_PAUSE("match_pause", "暂停赛事"),
                                   /**
                                    * 报名截至
                                    */
                                   ENROLL_END("enroll_end", "报名截至"),

                                   /**待完善—比赛已结束
                                    */
                                   WAIT_END("wait_end", "待完善-比赛已结束"),

                                   /**
                                    * 待支付
                                    */
                                   WAIT_PAY("wait_pay", "待支付"),
                                   /**
                                    * 待支付－比赛已结束
                                    */
                                   WAIT_PAY_END("wait_pay_end", "待支付－比赛已结束"),
                                   /**
                                    * 待支付－超时
                                    */
                                   WAIT_PAY_OVERTIME("wait_pay_overtime", "待支付－超时"),
                                   /**
                                    * 报名成功
                                    */
                                   SUCCESS("success", "待支付－超时"),
                                   /**     
                                    * 失败
                                    */
                                   FAIL("fail", "失败"),
                                   /**
                                    * 取消
                                    */
                                   ABANDONED("abandoned", "取消");

    private String code;
    private String cname;

    private OrderCurentStatusEnum(String code, String cname) {
        this.code = code;
        this.cname = cname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

}
