package com.atlas.crmapp.util;

/**
 * Created by Alex on 2017/3/16.
 */

public class Urls {
    public static final String SERVER = "http://192.168.109.100:8080/";
    public static final String SERVER_NORMAL = SERVER+"app/api/v1/";
    public static final String SERVER_OAUTH = SERVER+"authz/oauth/token?";
    public static final String URL_LOGIN = SERVER_NORMAL + "register/login/";
    public static final String URL_SENDSMS = SERVER_NORMAL + "register/sendSms/";
}
