package com.atlas.crmapp.model;

import java.io.Serializable;

/**
 * Created by Harry on 2017-04-15.
 */

public class SuggestOrderModel implements Serializable{

    private static final long serialVersionUID = 8060259695097115040L;
    public long id;
    public String promoType;
    public long promoId;
    public double discount;
    public double amount;
    public double actualAmount;
    public int reachCount;
}
