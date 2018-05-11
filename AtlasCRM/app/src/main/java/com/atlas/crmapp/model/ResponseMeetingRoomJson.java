package com.atlas.crmapp.model;

import java.util.List;

/**
 * Created by Alex on 2017/4/10.
 */

public class ResponseMeetingRoomJson {
    public OrderModel getOrder() {
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }

    public List<OrderItems> getItem() {
        return item;
    }

    public void setItem(List<OrderItems> item) {
        this.item = item;
    }

    private OrderModel order;
    private List<OrderItems> item;


    public class OrderModel{


        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public long getUnitId() {
            return unitId;
        }

        public void setUnitId(long unitId) {
            this.unitId = unitId;
        }

        public String getBizCode() {
            return bizCode;
        }

        public void setBizCode(String bizCode) {
            this.bizCode = bizCode;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreateMethod() {
            return createMethod;
        }

        public void setCreateMethod(String createMethod) {
            this.createMethod = createMethod;
        }

        public String getAuditState() {
            return auditState;
        }

        public void setAuditState(String auditState) {
            this.auditState = auditState;
        }

        public String getRefNo() {
            return refNo;
        }

        public void setRefNo(String refNo) {
            this.refNo = refNo;
        }

        public Boolean getCancelable() {
            return cancelable;
        }

        public void setCancelable(Boolean cancelable) {
            this.cancelable = cancelable;
        }

        public Boolean getBillable() {
            return billable;
        }

        public void setBillable(Boolean billable) {
            this.billable = billable;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getBriefing() {
            return briefing;
        }

        public void setBriefing(String briefing) {
            this.briefing = briefing;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getActualAmount() {
            return actualAmount;
        }

        public void setActualAmount(double actualAmount) {
            this.actualAmount = actualAmount;
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

        public String getPaymentState() {
            return paymentState;
        }

        public void setPaymentState(String paymentState) {
            this.paymentState = paymentState;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        private long id; //订单id
        private String createBy;
        private String userType;
        private long userId;
        private long unitId;
        private String bizCode;
        private String type;
        private String createMethod;
        private String auditState;
        private String refNo;
        private Boolean cancelable;
        private Boolean billable;
        private long updateTime;
        private String briefing; //订单描述
        private double discount; //折扣
        private double amount; //原价
        private double actualAmount; //应付=原价-折扣
        private long createTime; //创建时间
        private String state; //订单状态
        private String paymentState; //支付状态
    }

    public class OrderItems{
        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getOrderId() {
            return orderId;
        }

        public void setOrderId(long orderId) {
            this.orderId = orderId;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public long getSkuId() {
            return skuId;
        }

        public void setSkuId(long skuId) {
            this.skuId = skuId;
        }

        public String getOptions() {
            return options;
        }

        public void setOptions(String options) {
            this.options = options;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getActualAmount() {
            return actualAmount;
        }

        public void setActualAmount(double actualAmount) {
            this.actualAmount = actualAmount;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        private long id; //订单项id
        private long orderId; //订单项id
        private double price; //价格
        private int count; //数量
        private long skuId; //sku id
        private String options; //sku选项
        private String productName; //产品名称
        private double discount; //折扣
        private double amount; //原价=price * count
        private double actualAmount; //应付=原价 - 折扣
        private String thumbnail; //缩略图
        private String startTime; //sku id
        private String endTime; //sku id
    }
}
