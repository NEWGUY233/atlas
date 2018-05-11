package com.atlas.crmapp.model.bean;

/**
 * Created by A.Developer on 2017/3/19.
 */

public class MyExpenseModel {

    private String date;
    private String time;
    private String price;
    private String address;
    private int status;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        String s = "";
        switch (status) {
            case 0:
                s = "-" + price;
                break;
            case 1:
                s = "-" + price;
                break;
            case 2:
                s = "+" + price;
                break;
        }
        return s;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        String s = "";
        switch (status) {
            case 0:
                s = "支付";
                break;
            case 1:
                s = "调整";
                break;
            case 2:
                s = "退款";
                break;
        }
        return s;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
