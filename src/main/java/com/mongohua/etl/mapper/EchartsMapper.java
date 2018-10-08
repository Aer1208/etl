package com.mongohua.etl.mapper;

import com.mongohua.etl.model.Echarts;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统首页图表Mybatis接口
 * @author xiaohf
 */
public interface EchartsMapper {

    /**
     * 获取日期的运行最慢的TOP10的作业
     * @param dataDate
     * @param top
     * @return
     */
    public List<Echarts> topN(@Param("dataDate") int dataDate, @Param("top") int top);

    /**
     * 获取最近7天的作业运行时间
     * @return
     */
    public List<Echarts> getRunTime();

    /**
     * 监控所有作业运行情况
     * @return
     */
    public Echarts allJobMonitor();

    /**
     * 监控数据源运行情况
     * @return
     */
    public Echarts dsMonitor();

    /**
     * 监控普通作业运行情况
     * @return
     */
    public Echarts jobMonitor();
}
