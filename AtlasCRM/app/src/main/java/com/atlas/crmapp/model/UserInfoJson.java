package com.atlas.crmapp.model;

import java.io.Serializable;

/**
 * Created by Harry on 2017-03-22.
 */

public class UserInfoJson implements Serializable{

    private long id;
    private String uid = "";
    private String mobile = "";
    private String nick = "";
    private String company = "";
    private String gender = "";
    private String avatar = "";
    private int age;


    public String getEasemobPwd() {
        return easemobPwd;
    }

    public void setEasemobPwd(String easemobPwd) {
        this.easemobPwd = easemobPwd;
    }

    private String easemobPwd;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
