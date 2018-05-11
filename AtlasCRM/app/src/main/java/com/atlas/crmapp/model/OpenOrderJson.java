package com.atlas.crmapp.model;

import java.util.List;

/**
 * Created by Harry on 2017-04-02.
 */

public class OpenOrderJson {

    public long unitId; //业务门店ID
    public double amount; //订单价格
    public List<CartJson> items; //购物车列表
    public String city; //送货城市
    public String address; //送货地址
    public String receiver; //接收人


}
