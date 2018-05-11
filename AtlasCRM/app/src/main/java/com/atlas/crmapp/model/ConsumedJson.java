package com.atlas.crmapp.model;

import java.io.Serializable;

/**
 * Created by hoda on 2017/12/15.
 */

public class ConsumedJson  implements Serializable{
    private static final long serialVersionUID = 6047556886525780733L;


    private int quantity ;//总名额
    private int consumed ;//已核销人数
    private String state;//订单状态：UNPAID- 未付款，PAID - 已支付，COMPLETE - 已核消

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getConsumed() {
        return consumed;
    }

    public void setConsumed(int consumed) {
        this.consumed = consumed;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
