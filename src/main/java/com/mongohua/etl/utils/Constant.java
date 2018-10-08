package com.mongohua.etl.utils;

/**
 * 系统常量
 * @author xiaohf
 */
public class Constant {

    /**
     * 普通作业的最小编码值
     */
    public final static int MIN_JOB_ID=20000000;
    /**
     * 数据源作业的编码最大编码值
     */
    public final static int MAX_DS_ID=19999999;

    /**
     * 新增事件
     */
    public final  static String ADD="add";

    /**
     * 更新事件
     */
    public final static String UPDATE="update";

    /**
     * 最大运行进程数
     */
    public final static String MAX_RUN_CNT="max_run_cnt";

    /**
     * 目录分隔符
     */
    public final static String PATH_SPLIT_STR="/";

    /**
     * 按小时偏移量
     */
    public final static int HOUR=0;
    /**
     * 按日偏移计算日期
     */
    public final static int DAY=1;
    /**
     * 按月偏移计算日期
     */
    public final static int MONTH=2;
    /**
     * 按年偏移计算日期
     */
    public final static  int YEAR=3;


    /**
     * MD5加密算法加密次数
     */
    public final static int MD5_CNT=2;
}
