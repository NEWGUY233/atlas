package com.atlas.crmapp.model;

import java.util.List;

/**
 * Created by Alex on 2017/4/7.
 */

public class ResponseCreateContractOrder {
    public Order order;
    public List<OrderItem> items; //订单项列表

    public class Order{
        public long id;
        public String createBy;
        public long createTime;
        public String userType;
        public int userId;
        public int unitId;
        public String bizCode;
        public double amount;
        public double actualAmount;
        public String paymentState;
        public String state;
        public String type;
        public String createMethod;
        public String auditState;
        public double discount;
        public String briefing;
        public String refNo;
        public Boolean cancelable;
        public Boolean billable;
        public long updateTime;
    }

    public class OrderItem{
        public long id; //订单项id
        public long orderId;//订单
        public double price; //价格
        public int count; //数量
        public long skuId; //sku id
        public String options; //sku选项
        public String productName; //产品名称
        public double discount; //折扣
        public double amount; //原价=price * count
        public double actualAmount; //应付=原价 - 折扣
        public String skuMediaUrl; //缩略图
        public long startTime;
        public long endTime;
        public String refNo;
    }
}
