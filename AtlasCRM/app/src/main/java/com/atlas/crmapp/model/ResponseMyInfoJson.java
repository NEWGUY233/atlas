package com.atlas.crmapp.model;

/**
 * Created by Alex on 2017/4/14.
 */

public class ResponseMyInfoJson {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(long tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private long id;
    private String userType;
    private long userId;
    private double amount;
    private String token;
    private long tokenExpiry;
    private String state;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;
}
