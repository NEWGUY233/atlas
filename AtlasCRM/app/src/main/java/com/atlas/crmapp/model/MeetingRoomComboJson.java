package com.atlas.crmapp.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * 会议室价格表
 * Created by hoda on 2018/1/2.
 */

public class MeetingRoomComboJson implements Serializable {


    private static final long serialVersionUID = -8824318120119998885L;
    private long id;
    private String name;
    private String type;
    private long value;
    private BigDecimal amount;
    private String peroid;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPeroid() {
        return peroid;
    }

    public void setPeroid(String peroid) {
        this.peroid = peroid;
    }
}
