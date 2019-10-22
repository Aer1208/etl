/*
Navicat MySQL Data Transfer

Source Server         : 10.36.1.58
Source Server Version : 50560
Source Host           : 10.36.1.58:3306
Source Database       : etl

Target Server Type    : MYSQL
Target Server Version : 50560
File Encoding         : 65001

Date: 2019-08-12 15:27:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_ds_def
-- ----------------------------
DROP TABLE IF EXISTS `t_ds_def`;
CREATE TABLE `t_ds_def` (
  `DS_ID` int(11) NOT NULL COMMENT '数据源唯一编码(6位）PK',
  `DS_NAME` varchar(100) DEFAULT NULL COMMENT '数据源名称描述',
  `SRC_TAB_NAM` varchar(100) DEFAULT NULL COMMENT '数据来源表名',
  `SRC_DB_NAM` varchar(20) DEFAULT NULL COMMENT '数据来源的数据库名',
  `SRC_DB_TYP` varchar(10) DEFAULT NULL COMMENT '默认MYSQL',
  `SRC_SERV_IP` varchar(16) DEFAULT NULL COMMENT '数据源来源于服务器IP地址',
  `SRC_SERV_PORT` int(11) DEFAULT NULL COMMENT '数据源的数据端口',
  `TARGET_PATH` varchar(100) DEFAULT NULL COMMENT '导出Hdfs路径地址',
  `CRON_DESC` varchar(100) DEFAULT NULL COMMENT '定时任务描述，参见系统crontab说明',
  `FIELD_DEL` varchar(10) DEFAULT NULL COMMENT '导出文件的分隔符字段',
  `EXPORT_COLS` varchar(2000) DEFAULT NULL COMMENT '导出字段分隔符列表，默认全字段导出',
  `WHERE_EXP` varchar(400) DEFAULT NULL COMMENT '导出条件表达式，默认为空',
  `PRIORTY` int(11) DEFAULT NULL COMMENT '作业运行优先级',
  `DS_VALID` int(11) DEFAULT NULL COMMENT '1：有效；0：失效',
  `JOB_CYCLE` int(11) DEFAULT '1' COMMENT '作业运行周期',
  `CYCLE_UNIT` smallint(6) DEFAULT '1' COMMENT '作业运行周期单位:0:小时，1：天，2：月，3：年',
  user_id int default null comment '用户id'
  PRIMARY KEY (`DS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_err_inst
-- ----------------------------
DROP TABLE IF EXISTS `t_err_inst`;
CREATE TABLE `t_err_inst` (
  `INST_ID` bigint(20) NOT NULL COMMENT '实例ID',
  `STATUS` smallint(6) NOT NULL COMMENT '作业运行状态，0:出错，1:重跑出错作业',
  PRIMARY KEY (`INST_ID`,`STATUS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_inst_log
-- ----------------------------
DROP TABLE IF EXISTS `t_inst_log`;
CREATE TABLE `t_inst_log` (
  `LOG_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `INST_ID` bigint(20) DEFAULT NULL COMMENT '实例ID',
  `JOB_LOG` varchar(8192) DEFAULT NULL COMMENT '运行日志',
  PRIMARY KEY (`LOG_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=327273 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_job_def
-- ----------------------------
DROP TABLE IF EXISTS `t_job_def`;
CREATE TABLE `t_job_def` (
  `JOB_ID` int(11) NOT NULL COMMENT '作业数据源编码（8位）  PK                                                                   ',
  `JOB_NAME` varchar(100) DEFAULT NULL COMMENT '作业名称描述                                                                                ',
  `CMD_NAM` varchar(50) DEFAULT NULL COMMENT '作业运行命令                                                                                ',
  `CMD_PATH` varchar(100) DEFAULT NULL COMMENT '作业运行命令所在的路径                                                                      ',
  `CMD_TYP` smallint(6) DEFAULT NULL COMMENT '1、SHELL\r\n2、java\r\n3、perl\r\n4、procedure                                                     ',
  `JOB_VALID` smallint(6) DEFAULT NULL COMMENT '0:临时封，能产生事件，但是不运行\r\n1：正常，能产生事件，能运行\r\n2：永久封，不产生事件，也不运行',
  `JOB_GROUP` varchar(20) DEFAULT NULL COMMENT '作业组                                                                                      ',
  `MAX_INSTANCE` smallint(6) DEFAULT NULL COMMENT '作业组下运行同时运行的线程数 ',
  `PRIORTY` int(11) DEFAULT NULL COMMENT '作业运行优先级',
  `CRON_DESC` varchar(255) DEFAULT NULL COMMENT '作业定时任务表达式',
  `JOB_CYCLE` int(11) DEFAULT NULL COMMENT '作业运行周期',
  `CYCLE_UNIT` int(11) DEFAULT NULL COMMENT '作业运行周期单位：\r\n0：小时\r\n1：天\r\n2：月\r\n3：年',
  user_id  int default null comment '用户id'
  PRIMARY KEY (`JOB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_job_event
-- ----------------------------
DROP TABLE IF EXISTS `t_job_event`;
CREATE TABLE `t_job_event` (
  `EVENT_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '事件ID',
  `JOB_ID` int(11) DEFAULT NULL COMMENT '作业编号',
  `INST_ID` bigint(20) DEFAULT NULL COMMENT '触发作业实例号',
  `DATA_DATE` bigint(20) DEFAULT NULL COMMENT '数据日期',
  `REF_JOB_ID` int(11) DEFAULT NULL COMMENT '被触发作业编号',
  `REF_TYPE` int(11) DEFAULT NULL COMMENT '作业依赖类型，1：直接依赖\r\n2：天作业依赖小时作业\r\n3：月作业依赖日作业\r\n4：年作业依赖月作业',
  PRIMARY KEY (`EVENT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=145772 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_job_inst
-- ----------------------------
DROP TABLE IF EXISTS `t_job_inst`;
CREATE TABLE `t_job_inst` (
  `INST_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '实例编号',
  `JOB_ID` int(11) DEFAULT NULL COMMENT '作业编号',
  `DATA_DATE` bigint(20) DEFAULT NULL COMMENT '数据日期',
  `START_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '作业运行开始日期',
  `END_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '作业运行结束日期',
  `STATUS` smallint(6) DEFAULT NULL COMMENT '作业运行状态',
  PRIMARY KEY (`INST_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=327277 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_job_lock_obj
-- ----------------------------
DROP TABLE IF EXISTS `t_job_lock_obj`;
CREATE TABLE `t_job_lock_obj` (
  `job_id` int(11) NOT NULL COMMENT '作业ID',
  `lock_obj` varchar(100) NOT NULL COMMENT '锁对象',
  `lock_type` int(11) NOT NULL COMMENT '锁对象类型，1：写锁，0：读锁',
  PRIMARY KEY (`job_id`,`lock_obj`,`lock_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_job_param
-- ----------------------------
DROP TABLE IF EXISTS `t_job_param`;
CREATE TABLE `t_job_param` (
  `JOB_ID` int(11) NOT NULL COMMENT '作业数据源编码（8位）  PK',
  `PARAM_SEQ` smallint(6) NOT NULL COMMENT '参数序号  PK             ',
  `PARAM_ID` varchar(20) NOT NULL COMMENT '参数ID                   ',
  `PARAM_NAME` varchar(255) DEFAULT NULL COMMENT '参数名称',
  `PARAM_DEF` varchar(2000) DEFAULT NULL COMMENT '参数定义                 ',
  `PARAM_TYP` smallint(6) DEFAULT NULL COMMENT '0：整数\r\n1：字符串      ',
  `PARAM_VALID` smallint(6) DEFAULT NULL COMMENT '0：无效\r\n1：有效        ',
  PRIMARY KEY (`JOB_ID`,`PARAM_SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_job_queue
-- ----------------------------
DROP TABLE IF EXISTS `t_job_queue`;
CREATE TABLE `t_job_queue` (
  `QUEUE_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '实例编号',
  `JOB_ID` int(20) DEFAULT NULL COMMENT '作业编号',
  `DATA_DATE` bigint(11) DEFAULT NULL COMMENT '数据日期',
  `PRIORTY` int(11) DEFAULT NULL COMMENT '优先级',
  PRIMARY KEY (`QUEUE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=347015 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_job_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_job_ref`;
CREATE TABLE `t_job_ref` (
  `JOB_ID` int(11) NOT NULL COMMENT '作业编号',
  `REF_JOB_ID` int(11) NOT NULL COMMENT '依赖作业编号',
  `REF_TYPE` int(11) NOT NULL COMMENT '作业依赖类型',
  PRIMARY KEY (`JOB_ID`,`REF_JOB_ID`,`REF_TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_job_ref_cnt
-- ----------------------------
DROP TABLE IF EXISTS `t_job_ref_cnt`;
CREATE TABLE `t_job_ref_cnt` (
  `JOB_ID` int(11) NOT NULL DEFAULT '0' COMMENT '作业编号 PK',
  `REF_JOB_ID` int(11) NOT NULL DEFAULT '0' COMMENT '依赖作业编号 PK',
  `REF_TYPE` int(11) NOT NULL DEFAULT '0' COMMENT '2：天作业依赖小时作业\r\n   3：月作业依赖日作业\r\n   4：年作业依赖月作业',
  `DATA_DATE` bigint(20) NOT NULL DEFAULT '0' COMMENT '触发依赖时间',
  `TOT_CNT` int(11) DEFAULT NULL COMMENT '需要完成作业数',
  `SUCC_CNT` int(11) DEFAULT NULL COMMENT '已完成作业数',
  PRIMARY KEY (`JOB_ID`,`REF_JOB_ID`,`REF_TYPE`,`DATA_DATE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(32) DEFAULT NULL COMMENT '菜单名称',
  `menu_icon` varchar(32) DEFAULT NULL COMMENT '菜单图标',
  `parent_id` int(11) DEFAULT NULL COMMENT '父菜单ID',
  `url` varchar(100) DEFAULT NULL COMMENT '菜单url',
  `permission` varchar(100) DEFAULT NULL COMMENT '菜单权限',
  `perm_type` varchar(20) DEFAULT NULL COMMENT '权限类型，menu:菜单权限，button：页面权限',
  `status` int(11) DEFAULT '1' COMMENT '菜单权限，1:生效；0:失效',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(32) DEFAULT NULL COMMENT '角色名称',
  `role_code` varchar(32) DEFAULT NULL COMMENT '角色编码',
  `status` int(11) DEFAULT '1' COMMENT '角色状态, 1:生效，0：失效',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `login_name` varchar(32) NOT NULL COMMENT '登录名',
  `password` varchar(64) NOT NULL COMMENT '登录密码',
  `salt` varchar(32) DEFAULT NULL COMMENT '加密',
  `user_name` varchar(10) DEFAULT NULL COMMENT '用户姓名',
  `part_name` varchar(10) DEFAULT NULL COMMENT '用户部门',
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
