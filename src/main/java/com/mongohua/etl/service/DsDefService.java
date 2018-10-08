package com.mongohua.etl.service;

import com.mongohua.etl.model.DsDef;
import com.mongohua.etl.utils.PageModel;

import java.util.List;

/**
 * 数据源定义表服务接口
 * @author xiaohf
 */
public interface DsDefService {

    /**
     * 获取数据源定义列表
     * @return
     */
    public List<DsDef> getList();

    /**
     * 新增一个数据源定义表
     * @param dsDef
     * @return
     */
    public int add(DsDef dsDef);

    /**
     * 更新一个数据源定义
     * @param dsDef
     * @return
     */
    public int update(DsDef dsDef);

    /**
     * 按页获取数据源列表
     * @param  key
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageModel<DsDef> getListForPage(String key,int pageNo, int pageSize);

    /**
     * 分页获取数据源列表，不包含最后运行状态，包含数据源调度是否启动信息
     * @param page
     * @param rows
     * @return
     */
    public PageModel<DsDef> getListForPage2(int page, int rows);

    /**
     * 切换数据源状态
     * @param dsIds
     * @param valid 1：切换为有效，0：切换为失效
     * @return
     */
    public int changeDsValid(String[] dsIds, int valid);
}
