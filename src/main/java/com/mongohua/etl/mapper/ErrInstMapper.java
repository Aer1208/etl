package com.mongohua.etl.mapper;

import com.mongohua.etl.model.ErrInst;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xiaohaifang
 * @date 2018/9/30 9:58
 *        错误实例
 */
public interface ErrInstMapper {
    /**
     * 分页获取错误实例列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<ErrInst> getList(@Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

    /**
     * 获取错误实例记录数
     * @return
     */
    public int getCount();
}
