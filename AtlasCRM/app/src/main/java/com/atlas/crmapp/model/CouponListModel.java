package com.atlas.crmapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Harry on 2017-04-15.
 */

public class CouponListModel implements Serializable{

    private static final long serialVersionUID = -2752512790973527081L;
    public CouponModel coupon;
    public List<Object> periods;
}
