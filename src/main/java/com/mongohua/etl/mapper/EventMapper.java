package com.mongohua.etl.mapper;

import com.mongohua.etl.model.JobEvent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 事件Mybatis接口
 * @author xiaohf
 */
public interface EventMapper {

    /**
     * 读取所有的事件
     * @return
     */
    public List<JobEvent> getList();

    /**
     * 新增一个事件
     * @param jobQueue
     * @return
     */
    public int add(JobEvent jobQueue);

    /**
     * 更新一个事件
     * @param jobQueue
     * @return
     */
    public int update(JobEvent jobQueue);

    /**
     * 删除一组事件
     * @param eventIds
     * @return
     */
    public int delete(@Param("eventIds") String[] eventIds);
}
