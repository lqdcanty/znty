package com.efida.sport.facade.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author zengbo
 * @version $Id: GoodsOrderCountModel.java, v 0.1 2018年10月10日 下午2:44:20 zengbo Exp $
 */
public class GoodsOrderCountModel implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    /**奖章支付时间*/
    private Date              date;

    /**奖章数据量*/
    private long              total;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}
