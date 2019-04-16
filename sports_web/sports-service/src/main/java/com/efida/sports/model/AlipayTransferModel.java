package com.efida.sports.model;

/**
 * 支付宝转账返回参数
 * 
 * @author zoutao
 * @version $Id: AlipayTransferModel.java, v 0.1 2018年5月15日 下午4:06:44 zoutao Exp $
 */
public class AlipayTransferModel {
    /**
     * 是否成功
     */
    private Boolean isSuccess;
    /**
     * 状态码
     */
    private String  code;
    /**
     * 状态码描述
     */
    private String  msg;
    /**
     * 平台订单号
     */
    private String  outBizNo;
    /**
     * 流水号
     */
    private String  trxNo;
    /**
     * 支付时间
     */
    private String  payDate;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOutBizNo() {
        return outBizNo;
    }

    public void setOutBizNo(String outBizNo) {
        this.outBizNo = outBizNo;
    }

    public String getTrxNo() {
        return trxNo;
    }

    public void setTrxNo(String trxNo) {
        this.trxNo = trxNo;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

}
