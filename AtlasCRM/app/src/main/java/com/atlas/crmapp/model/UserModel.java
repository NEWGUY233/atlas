package com.atlas.crmapp.model;

import java.util.List;

/**
 * Created by Alex on 2017/3/16.
 */

public class UserModel{
    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public List<UserList> getData() {
        return data;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<UserList> data) {
        this.data = data;
    }

    public int errorCode;
    public String message;
    public List<UserList> data;

    public class UserList{
        public int getId() {
            return id;
        }

        public String getUid() {
            return uid;
        }

        public String getMobile() {
            return mobile;
        }

        public String getNick() {
            return nick;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public int id;
        public String uid;
        public String mobile;
        public String nick;
    }
}
