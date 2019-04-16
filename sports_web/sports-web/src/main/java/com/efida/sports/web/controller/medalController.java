package com.efida.sports.web.controller;

import com.efida.sports.entity.PayOrder;
import com.efida.sports.enums.OrderStatusEnum;
import com.efida.sports.service.IPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wnagyan on 2018/7/18.
 */
@Controller
public class medalController {

    @Autowired
    private IPayOrderService orderServce;

    @RequestMapping("medalPage")
    public String medalPage() {
        return "pages/medalPage";
    }
    @RequestMapping("addressList")
    public String addressList() {
        return "pages/addressList";
    }
    @RequestMapping("medalOrderPage")
    public String medalOrderPage() {
        return "pages/medalOrderPage";
    }
    @RequestMapping("myMedal")
    public String myMedal() {
        return "pages/my_medal";
    }
    @RequestMapping("myCertificate")
    public String myCertificate() {
        return "pages/certificate";
    }
//    @RequestMapping("medalPage")
//    public String medalPage() {
//        return "pages/medalPage";
//    }
    @RequestMapping("orderCenter")
    public String orderCenter() {
        return "pages/orderCenter";
    }
    @RequestMapping("orderPaySuccess")
    public String orderPaySuccess(String payordercode) {
        PayOrder order = orderServce.getOrderDetail(payordercode);
        if (order == null) {
            return "redirect:/game/type";
        }
        if (OrderStatusEnum.SUCCESS.getCode().equals(order.getOrderStatus())) {
            return "pages/orderPaySuccess";
        }
        return "redirect:/orderCenter";
    }
    @RequestMapping("payForMedal")
    public String payForMedal() {
        return "pages/payForMedal";
    }
}
