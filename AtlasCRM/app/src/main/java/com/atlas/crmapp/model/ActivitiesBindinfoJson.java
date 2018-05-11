package com.atlas.crmapp.model;

import java.io.Serializable;

/**
 * Created by hoda on 2017/12/15.
 */

public class ActivitiesBindinfoJson implements Serializable{
    private static final long serialVersionUID = 1806478750205969381L;


    /**
     * id : 1002
     * activityId : 12
     * orderId : 12
     * startTime : 1002
     * endTime : 1002
     * serialNum :
     * quantity : 10
     * consumed : 1
     * state :
     */

    private long id;
    private long activityId;
    private long orderId;
    private long startTime;
    private long endTime;
    private String serialNum;
    private int quantity;
    private int consumed;
    private String state;
    private String createBy; //活动创建人
    private long bookingId;

    public long getBookingId() {
        return bookingId;
    }

    public void setBookingId(long bookingId) {
        this.bookingId = bookingId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

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
