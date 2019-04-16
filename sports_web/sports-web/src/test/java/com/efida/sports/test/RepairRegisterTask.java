//package com.efida.sports.test;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.efida.sports.entity.Register;
//import com.efida.sports.service.IApplyInfoService;
//
//public class RepairRegisterTask implements Runnable {
//
//    private IApplyInfoService applySerivce;
//
//    private static Logger     log  = LoggerFactory.getLogger(RepairRegisterTask.class);
//
//    private Register          data = null;
//
//    private int               currentNum;
//
//    public int getCurrentNum() {
//        return currentNum;
//    }
//
//    public void setCurrentNum(int currentNum) {
//        this.currentNum = currentNum;
//    }
//
//    public RepairRegisterTask(Register item, IApplyInfoService applySerivce) {
//        this.data = item;
//        this.applySerivce = applySerivce;
//    }
//
//    @Override
//    public void run() {
//        applySerivce.repairRegisterChannelCode4Apply(this.data);
//        log.info("修记录成功， crrentNum：" + this.currentNum + ",register_code:"
//                 + this.data.getRegisterCode());
//
//        int nu = this.currentNum % 100;
//        if (nu == 0) {
//            log.info("修记录成功， crrentNum：" + this.currentNum + ",apply_code:"
//                     + this.data.getRegisterCode());
//        }
//    }
//
//}
