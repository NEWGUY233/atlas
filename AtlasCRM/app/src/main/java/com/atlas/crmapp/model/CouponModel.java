package com.atlas.crmapp.model;

import java.io.Serializable;

/**
 * Created by Harry on 2017-04-15.
 */

public class CouponModel implements Serializable{

    private static final long serialVersionUID = 7487735299655821153L;
    public long id;
    public String createBy;
    public long createTime;
    public String type;
    public String name;
    public long unitId;
    public String targetBizCode;
    public int targetUnitId;
    public boolean enabled;
    public String thumbnail;
    public int allowance;
    public int weekdays;
    public String description;
    public long applyProductCategory;
    public long applyProductCategory2;
    public String applyProduct;
    public String applyOrderType;
    public long totalQuota;
    public long count;
    public int take;
    public int periodQuota;
    public String periodType;
    public String auditState;
    public boolean allowHoliday;
    public boolean recommend;
    public String applyProduct2;
    public double value1;
    public double value2;
    public boolean multiReach;
    public long startTime;//领券时间
    public long endTime;
    public int userUnitId;
    public long validStart;//有效时间
    public long validEnd;
    public String state;
    public boolean have;
    public boolean isShowMoreDetail;
}
