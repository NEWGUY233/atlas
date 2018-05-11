package com.atlas.crmapp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 2017/4/14.
 */

public class ActivityJson implements Serializable{
    private static final long serialVersionUID = 5162768898712190786L;

    public Long id;
    public String name;
    public String description;
    public long startTime;
    public long endTime;
    public long applyStart;
    public long applyEnd;
    public BigDecimal fee = new BigDecimal(0);
    public String city;
    public String address;
    public int minSize;
    public int maxSize;
    public String authorName;
    public String content;
    public String bizCode;
    public ArrayList<DetailMediaJson> medias;
    public Boolean apply;



    private BigDecimal minfee;
    private BigDecimal maxfee;
    private boolean inviateUserOnly;
    private List<Combo> combos;
    private long remainNum;


    public long getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(long remainNum) {
        this.remainNum = remainNum;
    }

    public BigDecimal getMinfee() {
        return minfee;
    }

    public void setMinfee(BigDecimal minfee) {
        this.minfee = minfee;
    }

    public BigDecimal getMaxfee() {
        return maxfee;
    }

    public void setMaxfee(BigDecimal maxfee) {
        this.maxfee = maxfee;
    }

    public boolean isInviateUserOnly() {
        return inviateUserOnly;
    }

    public void setInviateUserOnly(boolean inviateUserOnly) {
        this.inviateUserOnly = inviateUserOnly;
    }

    public List<Combo> getCombos() {
        return combos;
    }

    public void setCombos(List<Combo> combos) {
        this.combos = combos;
    }

    public Boolean getApply() {
        return apply;
    }

    public void setApply(Boolean apply) {
        this.apply = apply;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public long getApplyStart() {
        return applyStart;
    }

    public void setApplyStart(long applyStart) {
        this.applyStart = applyStart;
    }

    public long getApplyEnd() {
        return applyEnd;
    }

    public void setApplyEnd(long applyEnd) {
        this.applyEnd = applyEnd;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public List<DetailMediaJson> getMedias() {
        return medias;
    }

    public void setMedias(ArrayList<DetailMediaJson> medias) {
        this.medias = medias;
    }


    public class Combo implements Serializable{
        private static final long serialVersionUID = -419174958101588455L;


       /* {
            "id":1,
                "name":"双人套餐",
                "price":12.2,
                "quantity":2
        }*/

        private long id;
        private String name;
        private BigDecimal price;
        private long quantity;

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

        public long getQuantity() {
            return quantity;
        }

        public void setQuantity(long quantity) {
            this.quantity = quantity;
        }
    }
}
