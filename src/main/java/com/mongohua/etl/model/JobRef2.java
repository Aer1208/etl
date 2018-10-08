package com.mongohua.etl.model;

import java.util.List;

/**
 * 批量修改依赖的实体类
 * @author xiaohf
 */
public class JobRef2 {
    /**
     * 新增列表
     */
    private List<JobRef> inserted;
    /**
     * 修改列表
     */
    private List<JobRef> updated;
    /**
     * 删除列表
     */
    private List<JobRef> deleted;

    public List<JobRef> getInserted() {
        return inserted;
    }

    public void setInserted(List<JobRef> inserted) {
        this.inserted = inserted;
    }

    public List<JobRef> getUpdated() {
        return updated;
    }

    public void setUpdated(List<JobRef> updated) {
        this.updated = updated;
    }

    public List<JobRef> getDeleted() {
        return deleted;
    }

    public void setDeleted(List<JobRef> deleted) {
        this.deleted = deleted;
    }
}
