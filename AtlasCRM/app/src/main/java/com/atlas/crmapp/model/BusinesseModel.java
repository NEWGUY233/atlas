package com.atlas.crmapp.model;

/**
 * Created by hoda on 2017/7/20.
 */

public class BusinesseModel {

    /**
     * 用于业态 的标题
     */
    private long id;
    private String code;
    private String name;
    private boolean billable;
    private long sortValue;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isBillable() {
        return billable;
    }

    public void setBillable(boolean billable) {
        this.billable = billable;
    }

    public long getSortValue() {
        return sortValue;
    }

    public void setSortValue(long sortValue) {
        this.sortValue = sortValue;
    }

}
