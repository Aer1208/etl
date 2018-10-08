package com.mongohua.etl.service;

import com.mongohua.etl.model.ErrInst;
import com.mongohua.etl.utils.PageModel;

import java.util.List;

/**
 * @author xiaohaifang
 * @date 2018/9/30 10:17
 *        错误实例服务层
 */
public interface ErrInstService {

    /**
     * 分页获取错误实例列表
     * @param page
     * @param row
     * @return
     */
    public PageModel<ErrInst> getListForPage(int page, int row);
}
