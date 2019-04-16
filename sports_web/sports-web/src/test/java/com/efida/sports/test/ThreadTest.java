//package com.efida.sports.test;
//
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.efida.sports.entity.Register;
//import com.efida.sports.service.IApplyInfoService;
//import com.efida.sports.service.IPayOrderService;
//import com.efida.sports.service.IRegisterService;
//
//public class ThreadTest extends BaseTest {
//
//    @Autowired
//    private IPayOrderService  payOrderService;
//    @Autowired
//    private IRegisterService  registerService;
//
//    @Autowired
//    private IApplyInfoService applyInfoService;
//
//    private static Logger     log = LoggerFactory.getLogger(ThreadTest.class);
//
//    @Test
//    public void batchRepairRegisterChannelCode() {
//        int limit = 1000;
//        int num = 0;
//        while (true) {
//            long startTime = System.currentTimeMillis(); //获取开始时间
//            /**初始化集合**/
//            List<Register> registes = registerService.selectDmpRegister(num, limit);
//            for (Register register : registes) {
//                applyInfoService.repairRegisterChannelCode4Apply(register);
//            }
//            num++;
//            if (registes.size() < limit) {
//                break;
//            }
//            long endTime = System.currentTimeMillis(); //获取开始时间
//
//            log.info("修复1000条数据用时：" + (endTime - startTime) / 1000 + "秒");
//        }
//    }
//
//    @Test
//    public void ThreadbatchRepairRegisterChannelCode() {
//        ExecutorService executor = Executors.newFixedThreadPool(20);
//        int limit = 1000;
//        int num = 0;
//        int currentNum = 1;
//        while (true) {
//            long startTime = System.currentTimeMillis(); //获取开始时间
//            /**初始化集合**/
//            List<Register> registes = registerService.selectDmpRegister(num, limit);
//            for (Register register : registes) {
//                RepairRegisterTask task = new RepairRegisterTask(register, applyInfoService);
//                task.setCurrentNum(currentNum++);
//                executor.execute(task);
//            }
//            num++;
//            if (registes.size() < limit) {
//                break;
//            }
//        }
//        executor.shutdown();
//        while (!executor.isTerminated()) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//
//            }
//        }
//    }
//
//}
