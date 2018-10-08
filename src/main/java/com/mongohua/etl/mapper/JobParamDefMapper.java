package com.mongohua.etl.mapper;

import com.mongohua.etl.model.JobParamDef;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 作业参数MyBatis接口
 * @author xiaohf
 */
public interface JobParamDefMapper {

    /**
     * 获取全部作业参数列表
     * @return
     */
    public List<JobParamDef> getList() ;

    /**
     * 获取一个作业的全部参数
     * @param jobId
     * @return
     */
    public List<JobParamDef> getById(int jobId);

    /**
     * 根据作业ID和参数序号删除一个参数
     * @param jobId
     * @param paramSeq
     * @return
     */
    public int delete(@Param("jobId") int jobId, @Param("paramSeq") int paramSeq);

    /**
     * 新增一个作业参数
     * @param jobParamDef
     * @return
     */
    public int add(JobParamDef jobParamDef);

    /**
     * 新增一组作业参数
     * @param paramDefs
     * @return
     */
    public int addParams(@Param("paramDefs") List<JobParamDef> paramDefs);

    /**
     * 根据作业ID和参数序号判断参数是否存在
     * @param jobId
     * @param paramSeq
     * @return
     */
    public int existsParam(@Param("jobId") int jobId, @Param("paramSeq") int paramSeq);

    /**
     * 更新作业参数定义
     * @param jobParamDef
     * @return
     */
    public int update(JobParamDef jobParamDef);
}
