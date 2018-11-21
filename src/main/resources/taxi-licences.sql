/*
Navicat MySQL Data Transfer

Source Server         : RDS_Test
Source Server Version : 50616
Source Host           : rm-bp1cj68xrmcrqk78c.mysql.rds.aliyuncs.com:3306
Source Database       : taxi-licences

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2018-10-31 10:29:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for authority_info
-- ----------------------------
DROP TABLE IF EXISTS `authority_info`;
CREATE TABLE `authority_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `authority_name` varchar(20) NOT NULL COMMENT '权限名',
  `authority_group` varchar(32) NOT NULL COMMENT '权限组名',
  `uri` varchar(128) NOT NULL COMMENT '接口url',
  `associated_control` varchar(64) DEFAULT NULL COMMENT '关联控件',
  `remark` varchar(128) DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1-是，0-否',
  `gmt_create` datetime NOT NULL,
  `creator` varchar(20) NOT NULL,
  `gmt_modify` datetime NOT NULL,
  `modifier` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of authority_info
-- ----------------------------
INSERT INTO `authority_info` VALUES ('1', 'API TEST', 'TEST', '/web/user/list', null, null, '\0', '2018-10-31 10:16:02', '0', '2018-10-31 10:16:05', '0');

-- ----------------------------
-- Table structure for role_authority_info
-- ----------------------------
DROP TABLE IF EXISTS `role_authority_info`;
CREATE TABLE `role_authority_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL COMMENT '角色id（role_info）',
  `authority_id` bigint(20) NOT NULL COMMENT '权限id',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1-是，0-否',
  `gmt_create` datetime NOT NULL,
  `creator` varchar(20) NOT NULL,
  `gmt_modify` datetime NOT NULL,
  `modifier` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_authority_info
-- ----------------------------
INSERT INTO `role_authority_info` VALUES ('1', '1', '1', '\0', '2018-10-31 10:25:25', '0', '2018-10-31 10:25:29', '0');

-- ----------------------------
-- Table structure for role_info
-- ----------------------------
DROP TABLE IF EXISTS `role_info`;
CREATE TABLE `role_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) NOT NULL COMMENT '角色名称',
  `role_code` varchar(32) NOT NULL COMMENT '角色编码，纯英文字符',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '1-是，0-否',
  `gmt_create` datetime NOT NULL,
  `creator` varchar(20) NOT NULL,
  `gmt_modify` datetime NOT NULL,
  `modifier` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_info
-- ----------------------------
INSERT INTO `role_info` VALUES ('1', '系统管理员', 'manager', '\0', '2018-10-31 10:15:13', '0', '2018-10-31 10:15:16', '0');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `user_id` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL COMMENT '用户名，登陆账号，也作为联系方式',
  `nickname` varchar(20) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `login_password` varchar(255) DEFAULT NULL COMMENT '密码',
  `user_from` int(11) NOT NULL COMMENT '用户来源：10-导入，20-APP注册，30-后台添加',
  `type` int(30) NOT NULL COMMENT '用户类型:0-（super_admin）超级管理员，10（USER_WEB）-后端用户',
  `status` int(1) NOT NULL COMMENT '账号状态:10-正常，20-冻结，30-拉黑',
  `is_deleted` bit(1) NOT NULL COMMENT '是否删除：1-是，0-否',
  `creator` varchar(50) NOT NULL,
  `gmt_create` datetime NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `gmt_modify` datetime NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('0', 'admin', null, '超级管理员', null, 'e10adc3949ba59abbe56e057f20f883e', '10', '0', '10', '\0', '0', NOW(), '0', NOW());
INSERT INTO `user_info` VALUES ('021', 'manager', null, 'manager', null, 'e10adc3949ba59abbe56e057f20f883e', '30', '10', '10', '\0', '0', NOW(), '0', NOW());

-- ----------------------------
-- Table structure for user_role_info
-- ----------------------------
DROP TABLE IF EXISTS `user_role_info`;
CREATE TABLE `user_role_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `is_deleted` bit(1) NOT NULL COMMENT '是否删除：1-是，0-否',
  `creator` varchar(50) NOT NULL,
  `gmt_create` datetime NOT NULL,
  `modifier` varchar(50) NOT NULL,
  `gmt_modify` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role_info
-- ----------------------------
INSERT INTO `user_role_info` VALUES ('1', '021', '1', '\0', '0', '2018-10-31 10:14:50', '0', '2018-10-31 10:14:52');
