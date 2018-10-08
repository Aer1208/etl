package com.mongohua.etl.mapper;

import com.mongohua.etl.model.DsDef;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据源Mybatis接口
 * @author xiaohf
 */
public interface DsDefMapper {
    /**
     * 根据数据源ID获取一个数据源对象
     * @param id
     * @return
     */
    DsDef getById(Integer id);

    /**
     * 获取全部数据源列表
     * @return
     */
    public List<DsDef> getList();

    /**
     * 新增一个数据源
     * @param dsDef
     * @return
     */
    public int add(DsDef dsDef);

    /**
     * 根据数据源ID删除一个数据源
     * @param id
     * @return
     */
    public int delete(Integer id);

    /**
     * 更新数据源
     * @param dsDef
     * @return
     */
    public int update(DsDef dsDef);

    /**
     * 查询符合条件的作业运行实例列表
     * @param  key 搜索关键字
     * @param pageIndex 查询起始编号
     * @param pageSize  查询页大小
     * @return
     */
    public List<DsDef> getListForPage(@Param("key") String key,@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);

    /**
     * 查询有效的数据源总记录数
     * @param  key 搜索关键字
     * @return
     */
    public int getCount(@Param("key") String key);

    /**
     * 查询符合条件的作业运行实例列表, 不包含数据源最后运行信息
     * @param pageIndex 查询起始编号
     * @param pageSize  查询页大小
     * @return
     */
    public List<DsDef> getListForPage2(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);

    /**
     * 查询全部数据源记录数
     * @return
     */
    public int getCount2();

    /**
     * 将制定数据源列表修改为无效
     * @param dsIds
     * @return
     */
    public int dsInvalid(@Param("dsIds") String[] dsIds);

    /**
     * 将制定数据源列表修改为生效
     * @param dsIds
     * @return
     */
    public int dsValid(@Param("dsIds") String[] dsIds);
}
