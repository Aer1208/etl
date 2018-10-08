package com.mongohua.etl.mapper;

import com.mongohua.etl.model.JobDef;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 作业定义Mybatis接口
 * @author xiaohf
 */
public interface JobDefMapper {
    /**
     * 获取作业定义列表
     * @return
     */
    public List<JobDef> getList();

    /**
     * 根据作业ID获取一个作业对象
     * @param jobId
     * @return
     */
    public JobDef getById(int jobId);

    /**
     * 根据作业ID删除一个作业
     * @param jobId
     * @return
     */
    public int delete(int jobId);

    /**
     * 新增一个作业
     * @param jobDef
     * @return
     */
    public int add(JobDef jobDef);

    /**
     * 获取作业的后续作业
     * @param jobId
     * @return
     */
    public List<JobDef> getNextJob(@Param("jobId") int jobId);

    /**
     * 获取作业的前置作业
     * @param jobId
     * @return
     */
    public List<JobDef> getPreJob(@Param("jobId") int jobId);

    /**
     * 获取有效的作业列表
     * @param key  搜索关键字
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<JobDef> getListForPage(@Param("key") String key,@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);

    /**
     * 获取有效的作业列表记录数
     * @param key 搜索关键字
     * @return
     */
    public int getCount(@Param("key") String key);

    /**
     * 获取全部作业列表
     * @param jobDef
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public List<JobDef> getListForPage2(@Param("jobDef") JobDef jobDef,@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);

    /**
     * 获取全部作业列表记录数
     * @param jobDef
     * @return
     */
    public int getCount2(JobDef jobDef);

    /**
     * 更新一个作业
     * @param jobDef
     * @return
     */
    public int update(JobDef jobDef);
}
