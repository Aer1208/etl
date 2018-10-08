package com.mongohua.etl.mapper;

import com.mongohua.etl.model.JobRef;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 作业依赖Mybatis接口
 * @author xiaohf
 */
public interface JobRefMapper {

    /**
     * 获取全部作业依赖列表
     * @return
     */
    public List<JobRef> getList();

    /**
     * 获取一个作业的全部依赖
     * @param jobId
     * @return
     */
    public List<JobRef> getById(int jobId);

    /**
     * 新增一个依赖
     * @param jobRef
     * @return
     */
    public int add(JobRef jobRef);

    /**
     * 新增一组依赖
     * @param refs
     * @return
     */
    public int addRefs(@Param("refs") List<JobRef> refs);

    /**
     * 更新一个依赖
     * @param jobRef
     * @return
     */
    public int update(JobRef jobRef);

    /**
     * 删除一个依赖
     * @param jobRef
     * @return
     */
    public int delete(JobRef jobRef);
}
