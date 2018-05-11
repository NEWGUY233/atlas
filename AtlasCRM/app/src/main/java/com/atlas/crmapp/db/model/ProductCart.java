package com.atlas.crmapp.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class ProductCart {

    @Id(autoincrement = true)
    private Long id;
    private long productId;
    private int num;
    private String bizCode;
    private long unitId;
    @Generated(hash = 2118340541)
    public ProductCart(Long id, long productId, int num, String bizCode,
            long unitId) {
        this.id = id;
        this.productId = productId;
        this.num = num;
        this.bizCode = bizCode;
        this.unitId = unitId;
    }
    @Generated(hash = 1645874073)
    public ProductCart() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getProductId() {
        return this.productId;
    }
    public void setProductId(long productId) {
        this.productId = productId;
    }
    public int getNum() {
        return this.num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public String getBizCode() {
        return this.bizCode;
    }
    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }
    public long getUnitId() {
        return this.unitId;
    }
    public void setUnitId(long unitId) {
        this.unitId = unitId;
    }
}

