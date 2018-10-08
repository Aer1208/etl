package com.mongohua.etl.utils;

import java.util.List;

/**
 * 分页模型
 * @param <T>
 *     @author xiaohf
 */
public class PageModel <T> {

    /**
     * 当前页数据
     */
    private List<T> rows;
    /**
     * 总记录数
     */
    private int total;
    /**
     * 页号
     */
    private int pageNo;
    /**
     * 每页显示记录数
     */
    private int pageSize;
    /**
     * 总计多少页数据
     */
    private int totalPage;

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
