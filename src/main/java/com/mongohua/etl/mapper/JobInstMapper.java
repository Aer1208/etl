package com.mongohua.etl.mapper;

import com.mongohua.etl.model.JobInst;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 获取实例
 * @author xiaohf
 */
public interface JobInstMapper {

    /**
     * 查询符合条件的作业运行实例列表
     * @param jobInst  查询条件
     * @param pageIndex 查询起始编号
     * @param pageSize  查询页大小
     * @return
     */
    public List<JobInst> getList(@Param("jobInst") JobInst jobInst, @Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);

    /**
     * 查询符合条件的总记录数
     * @param jobInst
     * @return
     */
    public int getCount(@Param("jobInst") JobInst jobInst);

    /**
     * 查询符合条件的作业运行实例列表
     * @param userId 用户id
     * @param jobInst  查询条件
     * @param pageIndex 查询起始编号
     * @param pageSize  查询页大小
     * @return
     */
    public List<JobInst> getList(@Param("userId") int userId,@Param("jobInst") JobInst jobInst, @Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);

    /**
     * 查询符合条件的总记录数
     * @param userId 用户id
     * @param jobInst
     * @return
     */
    public int getCount(@Param("userId") int userId, @Param("jobInst") JobInst jobInst);

    /**
     * 获取作业运行实例日志
     * @param instId
     * @return
     */
    public String getInstLog(int instId);

    /**
     * 重调失败的实例列表
     * @param ids
     * @return
     */
    public int redoInsts(@Param("ids") String[] ids);

    /**
     * 获取单个作业的运行实例
     * @param jobId
     * @return
     */
    public List<JobInst> instJob(@Param("jobId") int jobId);

}
