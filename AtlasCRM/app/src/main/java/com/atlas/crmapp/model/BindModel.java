package com.atlas.crmapp.model;

import java.io.Serializable;

/**
 * Created by Harry on 2017-04-15.
 */

public class BindModel implements Serializable{

    private static final long serialVersionUID = 6638604281916251947L;
    public long id;
    public String createBy;
    public long createTime;
    public long couponId;
    public String userType;
    public int userId;
    public long validStart;
    public long validEnd;
    public int quota;
    public int used;
    public String refNo;
}
