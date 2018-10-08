package com.mongohua.etl.service.impl;

import com.mongohua.etl.mapper.ErrInstMapper;
import com.mongohua.etl.model.ErrInst;
import com.mongohua.etl.service.ErrInstService;
import com.mongohua.etl.utils.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xiaohaifang
 * @date 2018/9/30 10:29
 */
@Service
public class ErrInstServiceImpl implements ErrInstService {

    @Autowired
    private ErrInstMapper errInstMapper;

    @Override
    public PageModel<ErrInst> getListForPage(int pageNo, int pageSize) {
        if (pageNo < 0) {
            pageNo = 1;
        }

        PageModel<ErrInst> dsDefPageModel = new PageModel<ErrInst>();
        dsDefPageModel.setPageNo(pageNo);
        dsDefPageModel.setPageSize(pageSize);

        int count = errInstMapper.getCount();
        dsDefPageModel.setTotal(count);
        int pageIndex = (pageNo - 1) * pageSize;
        dsDefPageModel.setRows(errInstMapper.getList(pageIndex,pageSize));
        int totalPage = (int)Math.ceil(count*1.0/pageSize);
        dsDefPageModel.setTotalPage(totalPage);
        return dsDefPageModel;
    }
}
