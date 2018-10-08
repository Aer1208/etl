package com.mongohua.etl.mapper;

import com.mongohua.etl.model.JobQueue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 队列Mybatis接口
 * @author xiaohf
 */
public interface QueueMapper {

    /**
     * 读取所有的队列
     * @return
     */
    public List<JobQueue> getList();

    /**
     * 新增一个队列
     * @param jobQueue
     * @return
     */
    public int add(JobQueue jobQueue);

    /**
     * 更新一个队列
     * @param jobQueue
     * @return
     */
    public int update(JobQueue jobQueue);

    /**
     * 删除一组队列
     * @param queueIds
     * @return
     */
    public int delete(@Param("queueIds") String[] queueIds);
}
