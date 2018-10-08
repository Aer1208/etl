package com.mongohua.etl.model;

import java.util.List;

/**
 * 修改作业参数的实体类
 * @author xiaohf
 */
public class JobParamDef2 {

    /**
     * 新增列表
     */
    private List<JobParamDef> inserted;
    /**
     * 修改列表
     */
    private List<JobParamDef> updated;
    /**
     * 删除列表
     */
    private List<JobParamDef> deleted;

    public List<JobParamDef> getInserted() {
        return inserted;
    }

    public void setInserted(List<JobParamDef> inserted) {
        this.inserted = inserted;
    }

    public List<JobParamDef> getUpdated() {
        return updated;
    }

    public void setUpdated(List<JobParamDef> updated) {
        this.updated = updated;
    }

    public List<JobParamDef> getDeleted() {
        return deleted;
    }

    public void setDeleted(List<JobParamDef> deleted) {
        this.deleted = deleted;
    }
}
