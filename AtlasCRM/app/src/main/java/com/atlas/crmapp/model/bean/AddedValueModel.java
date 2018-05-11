package com.atlas.crmapp.model.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by hoda on 2018/1/2.
 */

public class AddedValueModel implements Serializable{

    private static final long serialVersionUID = 604557482263480449L;
    /*
        id:1
        name:"打印机" //可为空
        price:20.0 //可为空
        count:1 //暂为1*/
    private long id;
    private String name;
    private BigDecimal price;
    private int count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
