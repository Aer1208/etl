CREATE TABLE  T_DS_DEF (
DS_ID                INT                COMMENT '数据源唯一编码(6位）PK'
,DS_NAME              VARCHAR(100)      COMMENT '数据源名称描述'
,SRC_TAB_NAM          VARCHAR(30)       COMMENT '数据来源表名'
,SRC_DB_NAM           VARCHAR(20)       COMMENT '数据来源的数据库名'
,SRC_DB_TYP           VARCHAR(10)       COMMENT '默认MYSQL'
,SRC_SERV_IP          VARCHAR(16)       COMMENT '数据源来源于服务器IP地址'
,SRC_SERV_PORT        INT               COMMENT '数据源的数据端口'
,TARGET_PATH          VARCHAR(100)      COMMENT '导出Hdfs路径地址'
,CRON_DESC            VARCHAR(100)      COMMENT '定时任务描述，参见系统crontab说明'
,FIELD_DEL            VARCHAR(4)        COMMENT '导出文件的分隔符字段'
,EXPORT_COLS          VARCHAR(400)      COMMENT '导出字段分隔符列表，默认全字段导出'
,WHERE_EXP            VARCHAR(50)       COMMENT '导出条件表达式，默认为空'
,PRIORTY              INT               COMMENT '作业运行优先级'
,DS_VALID             INT               COMMENT '1：有效；0：失效'
,primary key (ds_id)
);

create table T_JOB_DEF(
JOB_ID                   INT           COMMENT '作业数据源编码（8位）  PK                                                                   '
,JOB_NAME                VARCHAR(100)  COMMENT '作业名称描述                                                                                '
,CMD_NAM                 VARCHAR(50)   COMMENT '作业运行命令                                                                                '
,CMD_PATH                VARCHAR(100)  COMMENT '作业运行命令所在的路径                                                                      '
,CMD_TYP                 SMALLINT      COMMENT '1、SHELL
2、java
3、perl
4、procedure                                                     '
,JOB_VALID               SMALLINT      COMMENT '0:临时封，能产生事件，但是不运行
1：正常，能产生事件，能运行
2：永久封，不产生事件，也不运行'
,JOB_GROUP               VARCHAR(20)   COMMENT '作业组                                                                                      '
,MAX_INSTANCE            SMALLINT      COMMENT '作业组下运行同时运行的线程数
,PRIORTY              INT               COMMENT '作业运行优先级'                                                        '
,primary key(job_id)
);

create table T_JOB_PARAM (
JOB_ID              INT           COMMENT '作业数据源编码（8位）  PK'
,PARAM_SEQ          SMALLINT      COMMENT '参数序号  PK             '
,PARAM_ID           VARCHAR(20)   COMMENT '参数ID                   '
,PARAM_DEF          VARCHAR(100)  COMMENT '参数定义                 '
,PARAM_TYP          SMALLINT      COMMENT '0：整数
1：字符串      '
,PARAM_VALID        SMALLINT      COMMENT '0：无效
1：有效        '
,primary key (job_id, param_seq, param_id)
);

create table T_JOB_REF (
JOB_ID		INT                  COMMENT '作业编号',
REF_JOB_ID		INT              COMMENT '依赖作业编号',
REF_TYPE		INT                COMMENT '作业依赖类型',
primary key (job_id, ref_job_id, ref_type)
);

CREATE TABLE `t_job_inst` (
  `INST_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '实例编号',
  `JOB_ID` int(11) DEFAULT NULL COMMENT '作业编号',
  `DATA_DATE` int(11) DEFAULT NULL COMMENT '数据日期',
  `START_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作业运行开始日期',
  `END_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作业运行结束日期',
  `STATUS` smallint(6) DEFAULT NULL COMMENT '作业运行状态',
  PRIMARY KEY (`INST_ID`)
)

CREATE TABLE T_INST_LOG (
LOG_ID   BIGINT PRIMARY KEY AUTO_INCREMENT  COMMENT '日志ID' ,
INST_ID  BIGINT  COMMENT '实例ID',
JOB_LOG  VARCHAR(4096) COMMENT '运行日志'
);

CREATE TABLE T_JOB_EVENT (
EVENT_ID		BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '事件ID',
JOB_ID		INT COMMENT '作业编号',
INST_ID		BIGINT COMMENT '触发作业实例号',
DATA_DATE	INT COMMENT '数据日期',
REF_JOB_ID		INT  COMMENT '被触发作业编号'
);

CREATE TABLE T_JOB_QUEUE (
QUEUE_ID		INT COMMENT '实例编号',
JOB_ID		INT  COMMENT '作业编号',
DATA_DATE		INT COMMENT '数据日期',
PRIORTY		INT COMMENT '优先级'
);

CREATE TABLE T_ERR_INST (
  INST_ID   BIGINT  COMMENT '实例ID' PRIMARY KEY,
  STATUS    smallint COMMENT '作业运行状态，0:出错，1:重跑出错作业'
);