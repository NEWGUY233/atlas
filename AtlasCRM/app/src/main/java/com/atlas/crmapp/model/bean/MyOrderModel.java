package com.atlas.crmapp.model.bean;

import java.util.ArrayList;

/**
 * Created by A.Developer on 2017/3/19.
 */

public class MyOrderModel {

    private String date;
    private int status;
    private ArrayList<OrderModel> ms = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<OrderModel> getMs() {
        return ms;
    }

    public void setMs(ArrayList<OrderModel> ms) {
        this.ms = ms;
    }

    public class OrderModel {

        private String picture;
        private String title;
        private String content;
        private float price;
        private int number;
        private String cover;
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}
