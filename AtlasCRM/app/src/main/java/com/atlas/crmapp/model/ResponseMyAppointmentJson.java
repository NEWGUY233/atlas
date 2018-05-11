package com.atlas.crmapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Alex on 2017/4/14.
 */

public class ResponseMyAppointmentJson implements Serializable {

    private static final long serialVersionUID = -3981911076576116073L;

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public List<MyAppointment> getRows() {
        return rows;
    }

    public void setRows(List<MyAppointment> rows) {
        this.rows = rows;
    }

    private int recordsFiltered;
    private int draw;
    private int recordsTotal;
    private List<MyAppointment> rows;


    public class MyAppointment{
        public long getId() {
            return id;
        }

        public void setId(long id) {
            id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRefNo() {
            return refNo;
        }

        public void setRefNo(String refNo) {
            this.refNo = refNo;
        }

        public String getRefName() {
            return refName;
        }

        public void setRefName(String refName) {
            this.refName = refName;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String state) {
            this.status = status;
        }

        public String getPaymentState() {
            return paymentState;
        }

        public void setPaymentState(String paymentState) {
            this.paymentState = paymentState;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getPeopleNum() {
            return peopleNum;
        }

        public void setPeopleNum(int peopleNum) {
            this.peopleNum = peopleNum;
        }

        private long id; //订单id
        private String type; //订单类型
        private String userType;
        private long startTime; //创建时间
        private long endTime; //创建时间
        private String title; //订单描述
        private String refNo; //原价
        private String refName; //返回会议室名称，或课程名称，或活动名称
        private String thumbnail;
        private long createTime; //创建时间
        private String state; //订单状态
        private String status; //预约状态
        private String paymentState; //支付状态
        private String userName;
        private int peopleNum;
    }


}
