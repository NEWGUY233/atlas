package com.atlas.crmapp.model;


import java.io.Serializable;

/**
 * Created by Harry on 2017-04-15.
 */

public class UseableCouponsModel implements Serializable{

    private static final long serialVersionUID = -2933113116600853743L;
    public CouponListModel coupon;
    public BindModel bind;

    public CouponListModel getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponListModel coupon) {
        this.coupon = coupon;
    }

    public BindModel getBind() {
        return bind;
    }

    public void setBind(BindModel bind) {
        this.bind = bind;
    }
}
