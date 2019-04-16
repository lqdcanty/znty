/*
Navicat MySQL Data Transfer

Source Server         : mysql_root
Source Server Version : 50716
Source Host           : 192.168.8.4:3306
Source Database       : sports-dmp

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2018-06-15 15:07:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_info`;
CREATE TABLE `sys_info` (
  `Id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `name` varchar(16) COLLATE utf8mb4_bin NOT NULL COMMENT '系统名称',
  `create_user_name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '系统创建者',
  `icon` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '系统图标',
  `dispaly` tinyint(1) DEFAULT NULL COMMENT '1:展示 0:不展示',
  `host` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'varchar系统域名',
  `gmt_create` datetime DEFAULT NULL COMMENT 'datetime',
  `gmt_modifed` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`Id`),
  KEY `sys_name_index` (`name`) USING BTREE COMMENT '系统名称索引'
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of sys_info
-- ----------------------------
INSERT INTO `sys_info` VALUES ('1', '系统管理', '超级管理员', 'fa-gear', '1', 'localhost', '2018-02-26 18:22:13', '2018-05-24 17:59:31');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_id` bigint(20) DEFAULT NULL COMMENT '系统父级目录id',
  `menu_name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '系统目录名称',
  `menu_url` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '系统目录url',
  `type` varchar(12) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '类型menu,url;button',
  `permission_code` varchar(24) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '权限码',
  `is_self_sys` tinyint(1) DEFAULT NULL COMMENT '是否内部系统：1.是，0.否',
  `new_window` tinyint(1) DEFAULT NULL COMMENT '是否是新窗口打开1:新窗口打开0当前框架打开',
  `is_open` tinyint(1) DEFAULT NULL COMMENT '是否展开1:展开 0:不展开',
  `status` tinyint(1) DEFAULT NULL COMMENT '使用状态 1启用0禁用',
  `parent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('2', '1', '系统配置', null, '', 'MjAxODA0MjUxATA4PMg=', '1', '0', '0', '1', null);
INSERT INTO `sys_menu` VALUES ('3', '1', '用户管理', '/system/user/manager', 'url', 'MjAxODA0MjUxATA4FMg=', '1', '0', '0', '1', '2');
INSERT INTO `sys_menu` VALUES ('5', '1', '权限管理', '/system/sys/roleList', 'url', 'MjAxODA0MjUxATA4KMg=', '1', '0', '0', '1', '2');
INSERT INTO `sys_menu` VALUES ('6', '1', '系统文档', '/doc.html', 'url', 'MjAxODA0MjUxATA4EMg=', '1', '0', '0', '1', '79');
INSERT INTO `sys_menu` VALUES ('11', '1', '菜单配置', '/system/sys/menuList', 'url', 'MjAxODA0MjUxATA4NMg=', '1', '0', '1', '1', '2');
INSERT INTO `sys_menu` VALUES ('12', '1', '系统配置', '/system/sys/sysList', 'url', 'MjAxODA0MjUxOTA4NMg=', '1', '0', '0', '1', '2');
INSERT INTO `sys_menu` VALUES ('13', '1', 'Swagger', '/swagger-ui.html', 'url', 'MjAxODA0MjUxOTM4NMg=', '1', '0', '0', '1', '79');
INSERT INTO `sys_menu` VALUES ('79', '1', '系统文档', '', 'url', 'MjAxODA0MjUxOTM4NDg=', '1', '0', '0', '1', null);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色名称',
  `uuid` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建者id',
  `create_user_name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建者名称',
  `status` varchar(12) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '状态 正常：normal锁定：lock',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_name` (`role_name`) USING BTREE,
  KEY `id` (`id`,`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级管理员', 'admindefult', 'admin', 'normal', '2018-06-13 19:02:00', null);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `role_name` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色名称',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '系统目录id',
  `menu_name` varchar(30) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '系统目录名称',
  `uuid` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_user_name` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建者名称',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `role_permiss_index` (`role_id`,`role_name`,`menu_id`,`menu_name`) USING BTREE COMMENT '角色权限索引'
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('1', '1', '超级管理员', '2', '系统配置', 'admindefult', 'admin', '2018-03-08 10:15:38', null);
INSERT INTO `sys_role_permission` VALUES ('4', '1', '超级管理员', '3', '用户管理', 'admindefult', 'admin', '2018-03-08 10:15:38', null);
INSERT INTO `sys_role_permission` VALUES ('6', '1', '超级管理员', '5', '权限管理', 'admindefult', 'admin', '2018-03-08 10:15:38', null);
INSERT INTO `sys_role_permission` VALUES ('7', '1', '超级管理员', '6', '系统文档', 'admindefult', 'admin', '2018-03-08 10:15:38', null);
INSERT INTO `sys_role_permission` VALUES ('8', '1', '超级管理员', '7', '报表引擎', 'admindefult', 'admin', '2018-03-08 10:15:38', null);
INSERT INTO `sys_role_permission` VALUES ('9', '1', '超级管理员', '10', '报表文档', 'admindefult', 'admin', '2018-03-08 10:15:38', null);
INSERT INTO `sys_role_permission` VALUES ('10', '1', '超级管理员', '11', '菜单配置', 'admindefult', 'admin', '2018-03-08 10:15:38', null);
INSERT INTO `sys_role_permission` VALUES ('11', '1', '超级管理员', '12', '系统配置', 'admindefult', 'admin', '2018-03-08 10:15:38', null);
INSERT INTO `sys_role_permission` VALUES ('12', '1', '超级管理员', '13', 'Swagger', 'admindefult', 'admin', '2018-03-08 10:15:38', null);
INSERT INTO `sys_role_permission` VALUES ('13', '1', '超级管理员', '79', '系统文档', 'admindefult', 'admin', '2018-06-15 15:05:51', null);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(20) COLLATE utf8mb4_bin NOT NULL COMMENT '用户唯一ID(内部)',
  `user_name` varchar(16) COLLATE utf8mb4_bin NOT NULL COMMENT '登陆名(登陆)',
  `password` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '密码',
  `real_name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实名',
  `nick_name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL,
  `email` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `phone` varchar(24) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '手机号',
  `gender` varchar(8) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '性别(男：man；女:woman；未知:unknow)',
  `status` varchar(12) COLLATE utf8mb4_bin DEFAULT '1' COMMENT '用户是否锁定(1:可使用；0:禁用)',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `remark` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `identity` varchar(24) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '身份(由第三方系统子指定)',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid_index` (`uuid`) USING BTREE,
  UNIQUE KEY `user_name_index` (`user_name`) USING BTREE,
  KEY `user_index` (`uuid`,`user_name`,`real_name`,`email`,`phone`) USING BTREE COMMENT '用户信息索引'
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admindefult', 'admin', 'E10ADC3949BA59ABBE56E057F20F883E', 'admin', '超级管理员', '765123274@qq.com', 'http://i-7.vcimg.com/trim/afbc4ce7d42d28e805f0d43c7c599120554463/trim.jpg', '18482190199', '未知', '1', '2018-06-15 15:06:35', '系统默认超级管理员', '超级管理员', '2018-02-26 00:10:29', '2018-06-01 18:39:23');

-- ----------------------------
-- Table structure for sys_user_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_log`;
CREATE TABLE `sys_user_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户唯一ID',
  `action_type` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '行为类型(login:登陆；loginout:退出登陆；sysadd:新增系统给；sysmodify:系统修改；menuadd:新增菜单；menumodify:菜单修改；other:其他)',
  `action` text COLLATE utf8mb4_bin COMMENT '行为记录',
  `ip` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '操作IP',
  `browser` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '浏览器信息',
  `view_url` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '访问URL',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `user_log_index` (`uuid`,`action_type`) USING BTREE COMMENT '用户日志索引'
) ENGINE=InnoDB AUTO_INCREMENT=1021 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of sys_user_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户唯一ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `role_name` varchar(24) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色名',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`Id`),
  KEY `role_id_fk` (`role_id`),
  KEY `role_name_fk` (`role_name`),
  CONSTRAINT `role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `role_name_fk` FOREIGN KEY (`role_name`) REFERENCES `sys_role` (`role_name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- ----------------------------
-- Table structure for sys_document
-- ----------------------------
DROP TABLE IF EXISTS `sys_document`;
CREATE TABLE `sys_document` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `type` int(2) NOT NULL COMMENT '(1.内部文档2.外部文档)',
  `name` varchar(32) NOT NULL COMMENT '名称',
  `version` varchar(32) DEFAULT NULL COMMENT '版本',
  `intro` text COMMENT '简介',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `attachment` text COMMENT '附件JSON数据[{"sdk包下载":"http"},{"文档":""}]',
  `is_enable` tinyint(1) DEFAULT NULL COMMENT '是否可使用(1可使用 0不可使用)',
  `last_uid` int(11) DEFAULT NULL COMMENT '最后维护人uid',
  `last_uname` varchar(32) DEFAULT NULL COMMENT '最后维护人名称',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('3', 'admindefult', '1', '超级管理员', '2018-04-25 19:25:37', null);
