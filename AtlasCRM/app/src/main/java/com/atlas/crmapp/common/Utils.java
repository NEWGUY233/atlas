package com.atlas.crmapp.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.util.SpUtil;
import com.orhanobut.logger.Logger;

/**
 * Created by Harry on 2017-03-22.
 */

public class Utils {
    
    private static final String SP_USER = GlobalParams.getInstance().SharedPreferenceName;

    public static void readAccountToken(Context context) {
        GlobalParams globalParams = GlobalParams.getInstance();
        SharedPreferences sp =  context.getSharedPreferences(SP_USER, android.content.Context.MODE_PRIVATE);
        globalParams.setAccessToken(sp.getString("accessToken", ""));
        globalParams.setRefreshToken(sp.getString("refreshToken", ""));
        PersonInfoJson personInfoJson = globalParams.getPersonInfoJson();
        personInfoJson.setNick(sp.getString("nick", ""));
        personInfoJson.setCompany(sp.getString("company", ""));
        personInfoJson.setGender(sp.getString("gender", ""));
        personInfoJson.setAvatar(sp.getString("avatar", ""));
        personInfoJson.setMobile(sp.getString("account", ""));
        personInfoJson.setUid(sp.getString("uid", ""));
        personInfoJson.setEasemobPwd(sp.getString("easepwd", ""));
        personInfoJson.setNoCountPassword(sp.getInt("noCountPassword", 0));
        personInfoJson.setOpenfingerprint(sp.getString("openfingerprint", ""));
        personInfoJson.setZipCode(sp.getString(SpUtil.AREA, "86"));
    }

    public static void storeAccountToken(Context context, String accessToken, String refreshToken, PersonInfoJson personInfoJson) {
        GlobalParams globalParams = GlobalParams.getInstance();
        SharedPreferences sp =  context.getSharedPreferences(SP_USER, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("account", personInfoJson.mobile);
        editor.putString("accessToken", accessToken);
        editor.putString("refreshToken", refreshToken);
        editor.putString("nick", personInfoJson.nick);
        editor.putString("company", personInfoJson.company);
        editor.putString("gender", personInfoJson.gender);
        editor.putString("avatar", personInfoJson.avatar);
        editor.putString(SpUtil.AREA, personInfoJson.getZipCode());
        editor.putString("uid", personInfoJson.uid);
        editor.putString("easepwd", personInfoJson.easemobPwd);
        editor.putInt("noCountPassword", personInfoJson.noCountPassword);
        editor.putString("openfingerprint", personInfoJson.openfingerprint);
        Logger.d("avatar---" + personInfoJson.avatar);
        globalParams.setAccessToken(accessToken);
        globalParams.setRefreshToken(refreshToken);
        globalParams.setPersonInfoJson(personInfoJson);
        editor.commit();
        Utils.readAccountToken(context);
    }

    public static void storeToken(Context context, String accessToken, String refreshToken) {
        GlobalParams globalParams = GlobalParams.getInstance();
        SharedPreferences sp =  context.getSharedPreferences(SP_USER, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("accessToken", accessToken);
        editor.putString("refreshToken", refreshToken);
        editor.commit();
        globalParams.setAccessToken(accessToken);
        globalParams.setRefreshToken(refreshToken);

    }

    public static void clearAccountToken(Context context) {
        GlobalParams globalParams = GlobalParams.getInstance();
        SharedPreferences sp =  context.getSharedPreferences(SP_USER, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        globalParams.setAccessToken("");
        globalParams.setRefreshToken("");
        globalParams.getPersonInfoJson().setNoCountPassword(-1);
        globalParams.getPersonInfoJson().setOpenfingerprint(Constants.ORDER_FINGER_PAY.NO_AGREE_FINGER_PAY);
        globalParams.getPersonInfoJson().setNick("");
        globalParams.getPersonInfoJson().setCompany("");
        globalParams.getPersonInfoJson().setGender("");
        globalParams.getPersonInfoJson().setAvatar("");
        globalParams.getPersonInfoJson().setMobile("");
        globalParams.getPersonInfoJson().setUid("");
        globalParams.getPersonInfoJson().setEasemobPwd("");
    }
}
