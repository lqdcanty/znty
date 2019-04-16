/*
SQLyog Ultimate v11.27 (32 bit)
MySQL - 5.7.20-log : Database - sport_weichat
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`sport_weichat` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */;

USE `sport_weichat`;

/*Table structure for table `apply_info` */

DROP TABLE IF EXISTS `apply_info`;

CREATE TABLE `apply_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `apply_code` varchar(20) COLLATE utf8mb4_bin NOT NULL COMMENT '报名信息唯一编号',
  `player_code` varchar(20) COLLATE utf8mb4_bin NOT NULL COMMENT '运动员唯一编号',
  `pay_order_code` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单唯一编号',
  `apply_amount` bigint(20) DEFAULT NULL COMMENT '报名费用',
  `apply_status` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '报名状态(待付款,成功)',
  `unit_code` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '承办方唯一标识',
  `game_code` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '项目编号',
  `game_name` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '项目名称(大类名称:跳绳等)',
  `match_code` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '赛事编号',
  `match_name` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '赛事名称(例如：XXX创办跳绳比赛)',
  `match_group_name` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '赛事分组名称(例如:男子组，女子组)',
  `match_group_code` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '赛事分组编号',
  `event_code` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '比赛项目编号',
  `event_name` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '比赛项目名称(例如：单摇)',
  `event_start_time` datetime DEFAULT NULL COMMENT '项目开始时间',
  `is_delet` tinyint(4) DEFAULT NULL COMMENT '是否删除(1:是,0:否)',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `game_end_time` datetime DEFAULT NULL COMMENT '项目结束时间',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Data for the table `apply_info` */

/*Table structure for table `pay_order` */

DROP TABLE IF EXISTS `pay_order`;

CREATE TABLE `pay_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_code` varchar(20) COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `register_code` varchar(20) COLLATE utf8mb4_bin NOT NULL COMMENT '创建用户唯一标识',
  `order_status` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '状态(待支付,成功,失败,废弃)',
  `order_amount` bigint(20) DEFAULT NULL COMMENT '订单金额(扣除折扣，代金券等购买商品实际应付金额)',
  `order_time` datetime DEFAULT NULL COMMENT '创建时间',
  `order_ip` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'ip',
  `order_referer_url` varchar(522) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '从哪个页面链接过来',
  `order_return_url` varchar(600) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '页面回调url',
  `notify_url` varchar(600) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '后台异步通知url',
  `order_period` smallint(6) DEFAULT NULL COMMENT '订单有效期(单位分钟)',
  `cancel_reason` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '取消原因',
  `pay_way_code` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付网关code',
  `pay_way_name` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付网关名称',
  `remark` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `trx_no` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '交易流水号',
  `expire_time` datetime DEFAULT NULL COMMENT '订单到期时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Data for the table `pay_order` */

/*Table structure for table `player` */

DROP TABLE IF EXISTS `player`;

CREATE TABLE `player` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `player_code` varchar(20) COLLATE utf8mb4_bin NOT NULL COMMENT '运动员唯一标识',
  `player_phone` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '运动员电话号码',
  `player_name` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '运动员名称',
  `sex` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '性别',
  `register_code` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户唯一编号',
  `email` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
  `player_height` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '身高',
  `player_weight` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '体重',
  `player_birth` date DEFAULT NULL COMMENT '生日',
  `player_nationality` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '国籍',
  `player_address` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '地址',
  `player_cer_type` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '证件类型',
  `player_cer_no` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '证件号码',
  `player_blood_type` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '血型',
  `player_nation` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '民族',
  `player_cloth_size` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '衣服尺码',
  `player_emergency_name` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '紧急联系人',
  `player_emergency_phone` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '紧急联系人电话',
  `att_url` varchar(60) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '附件地址',
  `img_url` varchar(60) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像地址',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Data for the table `player` */

/*Table structure for table `register` */

DROP TABLE IF EXISTS `register`;

CREATE TABLE `register` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `register_code` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户唯一编号',
  `account` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登录账号',
  `password` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(60) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '昵称',
  `headimg_url` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `gender` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '性别',
  `province` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '身份',
  `city` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '城市',
  `country` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '国家',
  `reg_terminal` varchar(12) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '注册来源(app,微信,微博)',
  `last_login_time` datetime DEFAULT NULL COMMENT '最近登录时间',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Data for the table `register` */

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键(数据库自动生成)',
  `nick_name` varchar(60) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '昵称',
  `register_code` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '账号唯一标识',
  `platform` varchar(12) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台来源(微信,微博,qq)',
  `app_id` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '微信公众号appId',
  `open_id` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户的唯一标识',
  `headimg_url` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户头像',
  `gender` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '性别',
  `province` varchar(40) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户个人资料填写的省份',
  `city` varchar(40) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '普通用户个人资料填写的城市',
  `country` varchar(40) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '国家',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `app_id` (`app_id`,`open_id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Data for the table `users` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
