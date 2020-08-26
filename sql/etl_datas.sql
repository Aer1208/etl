/*
Navicat MySQL Data Transfer

Source Server         : 10.36.1.58
Source Server Version : 50560
Source Host           : 10.36.1.58:3306
Source Database       : etl

Target Server Type    : MYSQL
Target Server Version : 50560
File Encoding         : 65001

Date: 2019-08-12 15:28:06
*/

SET FOREIGN_KEY_CHECKS=0;

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
-- Records of t_job_param
-- ----------------------------


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
-- Records of t_job_lock_obj
-- ----------------------------

-- ----------------------------
-- Table structure for t_job_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_job_ref`;
CREATE TABLE `t_job_ref` (
  `JOB_ID` int(11) NOT NULL COMMENT '作业编号',
  `REF_JOB_ID` int(11) NOT NULL COMMENT '依赖作业编号',
  `REF_TYPE` int(11) NOT NULL COMMENT '作业依赖类型',
  week_offset int(11) comment '星期几'
  PRIMARY KEY (`JOB_ID`,`REF_JOB_ID`,`REF_TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_job_ref
-- ----------------------------

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
   user_id  int default null comment '用户id',
  PRIMARY KEY (`JOB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_job_def
-- ----------------------------

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
  user_id int default null comment '用户id',
  PRIMARY KEY (`DS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_ds_def
-- ----------------------------

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
-- Records of t_role_menu
-- ----------------------------
INSERT INTO `t_role_menu` VALUES ('1', '1');
INSERT INTO `t_role_menu` VALUES ('1', '2');
INSERT INTO `t_role_menu` VALUES ('1', '3');
INSERT INTO `t_role_menu` VALUES ('1', '11');
INSERT INTO `t_role_menu` VALUES ('1', '12');
INSERT INTO `t_role_menu` VALUES ('1', '13');
INSERT INTO `t_role_menu` VALUES ('1', '21');
INSERT INTO `t_role_menu` VALUES ('1', '22');
INSERT INTO `t_role_menu` VALUES ('1', '23');
INSERT INTO `t_role_menu` VALUES ('1', '24');
INSERT INTO `t_role_menu` VALUES ('1', '25');
INSERT INTO `t_role_menu` VALUES ('1', '31');
INSERT INTO `t_role_menu` VALUES ('1', '32');
INSERT INTO `t_role_menu` VALUES ('1', '33');
INSERT INTO `t_role_menu` VALUES ('1', '34');
INSERT INTO `t_role_menu` VALUES ('1', '35');
INSERT INTO `t_role_menu` VALUES ('2', '2');
INSERT INTO `t_role_menu` VALUES ('2', '3');
INSERT INTO `t_role_menu` VALUES ('2', '21');
INSERT INTO `t_role_menu` VALUES ('2', '22');
INSERT INTO `t_role_menu` VALUES ('2', '23');
INSERT INTO `t_role_menu` VALUES ('2', '24');
INSERT INTO `t_role_menu` VALUES ('2', '25');
INSERT INTO `t_role_menu` VALUES ('2', '31');
INSERT INTO `t_role_menu` VALUES ('2', '32');
INSERT INTO `t_role_menu` VALUES ('2', '33');
INSERT INTO `t_role_menu` VALUES ('2', '34');
INSERT INTO `t_role_menu` VALUES ('3', '2');
INSERT INTO `t_role_menu` VALUES ('3', '21');
INSERT INTO `t_role_menu` VALUES ('3', '22');
INSERT INTO `t_role_menu` VALUES ('3', '23');
INSERT INTO `t_role_menu` VALUES ('3', '34');

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
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES ('1', '系统管理', 'icon-sys', '0', null, 'system', null, '1');
INSERT INTO `t_menu` VALUES ('2', '作业监控', 'icon-sys', '0', null, 'monitor', null, '1');
INSERT INTO `t_menu` VALUES ('3', '作业配置', 'icon-sys', '0', null, 'manager', null, '1');
INSERT INTO `t_menu` VALUES ('11', '用户管理', 'icon-users', '1', '/system/user_index', 'system:user_index', 'menu', '1');
INSERT INTO `t_menu` VALUES ('12', '角色管理', 'icon-role', '1', '/system/role_index', 'system:role_index', 'menu', '1');
INSERT INTO `t_menu` VALUES ('13', '菜单管理', 'icon-set', '1', '/system/menu_index', 'system:menu_index', 'menu', '1');
INSERT INTO `t_menu` VALUES ('21', '作业运行监控', 'icon-nav', '2', '/monitor/inst_index', 'monitor:inst_index', 'menu', '1');
INSERT INTO `t_menu` VALUES ('22', '数据源监控', 'icon-nav', '2', '/monitor/ds_inst_index', 'monitor:ds_inst_index', 'menu', '1');
INSERT INTO `t_menu` VALUES ('23', '普通作业监控', 'icon-nav', '2', '/monitor/job_inst_index', 'monitor:job_inst_index', 'menu', '1');
INSERT INTO `t_menu` VALUES ('24', '队列管理', 'icon-nav', '2', '/monitor/queue_index', 'monitor:queue_index', 'menu', '1');
INSERT INTO `t_menu` VALUES ('25', '事件管理', 'icon-nav', '2', '/monitor/event_index', 'monitor:event_index', 'menu', '1');
INSERT INTO `t_menu` VALUES ('31', '数据源列表', 'icon-nav', '3', '/manager/ds_index', 'manager:ds_index', 'menu', '1');
INSERT INTO `t_menu` VALUES ('32', '作业列表', 'icon-nav', '3', '/manager/job_index', 'manager:job_index', 'menu', '1');
INSERT INTO `t_menu` VALUES ('33', '锁对象监控', 'icon-nav', '2', '/monitor/lock_index', 'monitor:lock_index', 'menu', '1');
INSERT INTO `t_menu` VALUES ('34', '错误实例监控', 'icon-nav', '2', '/monitor/err_inst_index', 'monitor:err_inst_index', 'menu', '1');
INSERT INTO `t_menu` VALUES ('35', '无依赖的作业管理', 'icon-nav', '3', '/manager/not_ref_job_index', 'manager:not_ref_job_index', 'menu', '1');

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES ('1', '1');
INSERT INTO `t_user_role` VALUES ('2', '2');
INSERT INTO `t_user_role` VALUES ('3', '3');
INSERT INTO `t_user_role` VALUES ('4', '2');
INSERT INTO `t_user_role` VALUES ('5', '2');
INSERT INTO `t_user_role` VALUES ('6', '2');
INSERT INTO `t_user_role` VALUES ('7', '2');
INSERT INTO `t_user_role` VALUES ('8', '2');
INSERT INTO `t_user_role` VALUES ('9', '3');
INSERT INTO `t_user_role` VALUES ('10', '3');
INSERT INTO `t_user_role` VALUES ('11', '3');
INSERT INTO `t_user_role` VALUES ('12', '3');

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
-- Records of t_job_event
-- ----------------------------

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
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '管理员', 'ADMIN', '1');
INSERT INTO `t_role` VALUES ('2', '开发人员', 'DEV', '1');
INSERT INTO `t_role` VALUES ('3', '分析人员', 'SYS', '1');

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
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'admin', '43442676c74ae59f219c2d87fd6bad52', '8d78869f470951332959580424d4bf4f', '管理员', '管理员', '1');
INSERT INTO `t_user` VALUES ('2', 'xiaohf', '4280d89a5a03f812751f504cc10ee8a5', null, '肖海方', '大数据研发', '2');
SET FOREIGN_KEY_CHECKS=1;
