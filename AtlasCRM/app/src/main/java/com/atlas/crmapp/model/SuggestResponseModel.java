package com.atlas.crmapp.model;

import java.util.List;

/**
 * Created by Harry on 2017-04-15.
 */

public class SuggestResponseModel {
    public OriginModel origin;
    public List<SuggestListModel> suggests;
    public List<UseableBenefitModel> useableBenefits;
    public List<UseableCouponsModel> useableCoupons;
    public List<UseableCouponsModel> uselessCoupons;
}
