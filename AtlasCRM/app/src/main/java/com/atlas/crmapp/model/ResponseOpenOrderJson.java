package com.atlas.crmapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Harry on 2017-04-02.
 */

public class ResponseOpenOrderJson implements Serializable {

    private static final long serialVersionUID = -6832561615596943671L;

    private long id; //订单id
    private String type; //订单类型
    private String briefing; //订单描述
    private double discount; //折扣
    private double amount; //原价
    private double actualAmount; //应付=原价-折扣
    private List<OrderItem> items; //订单项列表
    private PromoScreen promo; //优惠信息
    private long createTime; //创建时间
    private String state; //订单状态
    private String paymentState; //支付状态
    private String bizCode; //所属bizcode;
    private boolean billable; //是否可以使用企业账户支付
    private String offlineCode;//台号
    private double deduction; //企业支付，抵扣时间数

    public double getDeduction() {
        return deduction;
    }

    public void setDeduction(double deduction) {
        this.deduction = deduction;
    }

    public String getOfflineCode() {
        return offlineCode;
    }

    public void setOfflineCode(String offlineCode) {
        this.offlineCode = offlineCode;
    }

    public boolean isBillable() {
        return billable;
    }

    public void setBillable(boolean billable) {
        this.billable = billable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public PromoScreen getPromo() {
        return promo;
    }

    public void setPromo(PromoScreen promo) {
        this.promo = promo;
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

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }



    public class OrderItem implements Serializable {

        private static final long serialVersionUID = 3862278473153733161L;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
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

        public void seThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        private long id; //订单项id
        private double price; //价格
        private int count; //数量
        private long skuId; //sku id
        private String options; //sku选项
        private String productName; //产品名称
        private double discount; //折扣
        private double amount; //原价=price * count
        private double actualAmount; //应付=原价 - 折扣
        private String thumbnail; //缩略图

//        {
//            "id": 136849,
//                "price": 0.05,
//                "count": 1,
//                "skuId": 308,
//                "productName": "Microsoft Word - 15272120002（apk）a",
//                "discount": 0.05,
//                "amount": 0.05,
//                "actualAmount": 0,
//                "thumbnail": ""
//        }
    }

    public class PromoScreen implements Serializable {

        private static final long serialVersionUID = -8496581684419251980L;
        private long promoId; //优惠信息id
        private String promoType; //优惠信息类型，COUPONBIND优惠券，BENEFIT促销活动
        private String name; //优惠信息名称

        public long getPromoId() {
            return promoId;
        }

        public void setPromoId(long promoId) {
            this.promoId = promoId;
        }

        public String getPromoType() {
            return promoType;
        }

        public void setPromoType(String promoType) {
            this.promoType = promoType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


    }
}
