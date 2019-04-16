package com.efida.sports.model;

/**
 * 奖品Model
 * @author wang yi
 * @desc 
 * @version $Id: Gift.java, v 0.1 2018年10月18日 下午3:26:25 wang yi Exp $
 */
public class Gift {

    private String code;      //编号
    private String name;      //奖品名称
    private double prob;      //获奖概率
    private double startValue;
    private double endValue;

    public double getStartValue() {
        return startValue;
    }

    public void setStartValue(double startValue) {
        this.startValue = startValue;
    }

    public double getEndValue() {
        return endValue;
    }

    public void setEndValue(double endValue) {
        this.endValue = endValue;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProb() {
        return prob;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }
}