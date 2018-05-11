package com.atlas.crmapp.network;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.Utils;
import com.atlas.crmapp.common.statusLayout.IStatusLayout;
import com.atlas.crmapp.model.AuthErrorResponse;
import com.atlas.crmapp.model.AuthResponse;
import com.atlas.crmapp.util.ContextUtil;
import com.atlas.crmapp.util.KProgressHUDUtils;
import com.atlas.crmapp.util.SpUtil;
import com.atlas.crmapp.util.StringUtils;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Harry on 2017-03-21.
 */

public class NetworkUtil {

    public static  int  dimissAfterSeconds = 2 *1000;

    public static interface OnAuthForToken {

        public void onSuccess(String accessToken, String refreshToken);

        public void onError(DcnException error);
    }

    public static void requestJsonRow(Context context, String url, HashMap<String, Object> params, int pageIndex, int pageSize, final boolean isShowProgreeDialog, IStatusLayout statusLayout, final OnResponseData onResponseData) {
        params.put("page", String.valueOf(pageIndex));
        params.put("size", String.valueOf(pageSize));
        params.put("draw", "0");
        requestJson(context, url, params, isShowProgreeDialog, statusLayout, onResponseData);
    }

    public static void requestJson(final Context context, String url, Object params, final OnResponseData onResponseData) {
        requestJson(context, url, params, false, onResponseData);
    }

    public static void requestJson(final Context context, String url, Object params, final IStatusLayout statusLayout, final OnResponseData onResponseData) {
        requestJson(context, url, params, false, statusLayout, onResponseData);
    }

    public static void requestJson(final Context context, String url, Object params, final boolean isShowProgreeDialog, final OnResponseData onResponseData) {
        requestJson(context,url, params, isShowProgreeDialog, null, onResponseData);
    }

   //主要调用
    public static void requestJson(final Context context, String url, Object params, final boolean isShowProgreeDialog, final IStatusLayout statusLayout, final OnResponseData onResponseData) {
        if(context == null){
            Logger.d("context is null");
            return;
        }

        if(isShowProgreeDialog ||( statusLayout !=null && statusLayout.showStatusLayout())){
            if(!(context instanceof BaseActivity)){
                Logger.d("context  must  instanceof BaseActivity");
                return;
            }
        }
        Gson gson = new Gson();
        int type = (int) SpUtil.getLong(ContextUtil.getUtil().getContext(),SpUtil.LANGUAGE,-1);
        if (url.indexOf("?") > 0){
            url += "&";
        }else
            url += "?";
        String locale = Locale.getDefault().toString();
        if (type == -1)
            if (Locale.SIMPLIFIED_CHINESE.toString().equals(locale)){
                type = 0;
            }else if (Locale.TRADITIONAL_CHINESE.toString().equals(locale) || "zh-rHK".equals(locale)){
                type = 1;
            }else if ("en_US".equals(locale)){
                type = 2;
            }else if (locale.startsWith("en")){
                type = 2;
            }else{
                type = 0;
            }

        switch (type){
            case 0:
                url += BizDataRequest.language_CN_CN;
                break;
            case 1:
                url += BizDataRequest.language_CN_GD;
                break;
            case 2:
                url += BizDataRequest.language_EN;
                break;
        }

        OkGo.post(url)
                .cacheKey("")
                .tag(context)
                .upJson(gson.toJson(params))
                .execute(getStringCallback(context, isShowProgreeDialog, statusLayout, onResponseData));

    }

    private static void refreshAccessToken(final Context context, Response response){
        Logger.i("WY", "AccessToken Failure:" + String.valueOf(response.code()) + "/nJson：" + response.message());
        final int code = response.code();
        refreshAccessToken(context, new OnAuthForToken() {
            @Override
            public void onSuccess(String accessToken, String refreshToken) {
                Utils.storeToken(context, accessToken, refreshToken);
                GlobalParams.getInstance().setIsLogin(true);
            }

            @Override
            public void onError(DcnException error) {
                GlobalParams.getInstance().setIsLogin(false);
                if(context instanceof Activity){
                    if(error != null && StringUtils.isNotEmpty(error.getDescription())){
                        Toast.makeText((Activity)context, error.getDescription() , Toast.LENGTH_LONG ).show();
                    }
                }
                Logger.i("WY", "RefreshToken Error:" + error.getMessage() + "/nJson：" + String.valueOf(code));
            }
        });
    }

    private static void dismissDialog(Context context, boolean isShowProgreeDialog, KProgressHUD dialog, DcnException error ){
        if(dialog != null){
            if(error != null){
                dialog.setDetailsLabel(error.getMessage());
                KProgressHUDUtils.dismissLoading(context, dialog, dimissAfterSeconds);
            }else{
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
        }
    }

    private static void onResponseCacheSuccess(Context context, String s, OnResponseData onResponseData, final boolean isShowProgreeDialog, final IStatusLayout statusLayout, KProgressHUD dialog){
        if(StringUtils.isNotEmpty(s) && onResponseData != null){

            Gson gson = new Gson();
            JsonResponse jsonResponse;
            try {
                jsonResponse = gson.fromJson(s, JsonResponse.class);
                if (jsonResponse != null && jsonResponse.errorCode == 0){
                    String sd = gson.toJson(jsonResponse.data);
                    onResponseData.onSuccess(sd, 200);
                    if(statusLayout != null){
                        statusLayout.showContent();
                    }
                }
            } catch (Exception e) {
                DcnException error = new DcnException(-999, e.getMessage(), e.getLocalizedMessage());
                onResponseData.onError(error);
                dismissDialog(context, isShowProgreeDialog, dialog, error);
            }
        }
    }

    private static StringCallback getStringCallback (final Context context, final boolean isShowProgreeDialog, final IStatusLayout statusLayout, final OnResponseData onResponseData){
        StringCallback  stringCallback ;
        if(statusLayout == null || isShowProgreeDialog || !statusLayout.showStatusLayout()){
            KProgressHUD dg = null;
            if (isShowProgreeDialog) {
                dg = KProgressHUDUtils.showLoading(context, "");
            }
            final KProgressHUD dialog = dg;
            stringCallback = new StringCallback() {

                @Override
                public void onCacheSuccess(String s, Call call) {
                    super.onCacheSuccess(s, call);
                    //onResponseCacheSuccess(context, s, onResponseData, isShowProgreeDialog, statusLayout, dialog);
                    Logger.d("CacheSuccess: " + s);
                    /*Response response = new Response.Builder().build();
                    response.code();*/

                   // onSuccess(s, call, response);
                }

                @Override
                public void onSuccess(String s, Call call, Response response) {
                    Logger.json(s);
                    if (context == null) {
                        Logger.i("ActivityIsNull");
                        return ;
                    }
                    Gson gson = new Gson();
                    JsonResponse jsonResponse;
                    String sd;
                    try {
                        jsonResponse = gson.fromJson(s, JsonResponse.class);
                        sd = gson.toJson(jsonResponse.data);
                    } catch (Exception e) {
                        DcnException error = new DcnException(-999, e.getMessage(), e.getLocalizedMessage());
                        onResponseData.onError(error);
                        dismissDialog(context, isShowProgreeDialog, dialog, error);
                        return;
                    }
                    if (response.code() == 200) {
                        if (jsonResponse.errorCode == 0) {
                            onResponseData.onSuccess(sd, response.code());
                            dismissDialog(context, isShowProgreeDialog, dialog, null);
                        } else {
                            DcnException error = new DcnException(jsonResponse.errorCode, jsonResponse.message, jsonResponse.message);
                            if(statusLayout != null && error.getCode() == -1){
                                statusLayout.showLoading(error.getMessage());
                            }
                            onResponseData.onError(error);
                            dismissDialog(context, isShowProgreeDialog, dialog, error);
                        }

                    } else if (response.code() == 401) {
                        dismissDialog(context, isShowProgreeDialog, dialog, null);
                        refreshAccessToken(context, response);
                    } else {
                        try{
                            DcnException error = new DcnException(response.code(), response.message(), jsonResponse.toString());
                            onResponseData.onError(error);
                            dismissDialog(context, isShowProgreeDialog, dialog, error);
                        }catch (Exception e){
                            Logger.d(e.getMessage());
                        }
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    Logger.i("WY", "Error:"+e.getMessage()+"/nJson："+ response);
                    if (context == null) {
                        Logger.i("ActivityIsNull");
                        return ;
                    }
                    if (response == null) {
                        DcnException error = new DcnException(Constants.NetWorkCode.NO_NET_WORK, e.getMessage(), e.getLocalizedMessage());
                        onResponseData.onError(error);
                        if(isShowProgreeDialog){
                            dialog.dismiss();
                        }

                        if(statusLayout!= null){
                            Logger.d("----" + statusLayout.showStatusLayout());
                            statusLayout.showError(error);
                        }
                        return;
                    }
                    if (response.code() == 401) {
                        dismissDialog(context, isShowProgreeDialog, dialog, null);
                        refreshAccessToken(context, response);
                    } else {
                        DcnException error = new DcnException(response.code(), response.message(), e.getMessage());
                        onResponseData.onError(error);
                        dismissDialog(context, isShowProgreeDialog, dialog, error);
                    }
                }
            };

        }

        else {
            statusLayout.showLoading();
            stringCallback = new StringCallback() {

                @Override
                public void onCacheSuccess(String s, Call call) {
                    super.onCacheSuccess(s, call);
                    //onResponseCacheSuccess(context, s, onResponseData, isShowProgreeDialog, statusLayout, dialog);
                    Logger.d("CacheSuccess: " + s);
                /*    Response.Builder response = new Response.Builder();
                    response.code(200);
                    onSuccess(s, call, response.build());*/
                }

                @Override
                public void onSuccess(String s, Call call, Response response) {
                    Logger.json(s);
                    if (context == null) {
                        Logger.i("ActivityIsNull");
                        return ;
                    }
                    Gson gson = new Gson();
                    JsonResponse jsonResponse;
                    String sd;
                    try {
                        jsonResponse = gson.fromJson(s, JsonResponse.class);
                        sd = gson.toJson(jsonResponse.data);
                    } catch (Exception e) {
                        DcnException error = new DcnException(-999, e.getMessage(), e.getLocalizedMessage());
                        statusLayout.showError(error);
                        onResponseData.onError(error);
                        return;
                    }
                    if (response.code() == 200) {
                        if (jsonResponse.errorCode == 0) {
                            statusLayout.showContent();
                            onResponseData.onSuccess(sd, response.code());
                        } else {
                            DcnException error = new DcnException(jsonResponse.errorCode, jsonResponse.message, jsonResponse.message);
                            statusLayout.showError(error);
                            onResponseData.onError(error);
                        }

                    } else if (response.code() == 401) {
                        refreshAccessToken(context,response);
                        DcnException error = new DcnException(response.code(), response.message(), jsonResponse.toString());
                        statusLayout.showError(error);
                        onResponseData.onError(error);

                    } else {
                        DcnException error = new DcnException(response.code(), response.message(), jsonResponse.toString());
                        statusLayout.showError(error);
                        onResponseData.onError(error);
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    Logger.i("WY", "Error:"+e.getMessage()+"/nJson："+ response);
                    if (context == null) {
                        Logger.i("ActivityIsNull");
                        return ;
                    }
                    if (response == null) {
                        DcnException error = new DcnException(Constants.NetWorkCode.NO_NET_WORK, e.getMessage(), e.getLocalizedMessage());
                        statusLayout.showError(error);
                        onResponseData.onError(error);
                        return;
                    }
                    if (response.code() == 401) {
                        refreshAccessToken(context,response);
                        DcnException error = new DcnException(response.code(), response.message(), e.getMessage());
                        statusLayout.showError(error);
                        onResponseData.onError(error);
                    } else {
                        DcnException error = new DcnException(response.code(), response.message(), e.getMessage());
                        statusLayout.showError(error);
                        onResponseData.onError(error);
                    }
                }
            };


        }

        return  stringCallback;
    }

    public static void requestJson(final Context context, String url, ArrayList params, final boolean showProgreeDialog, final OnResponseData onResponseData) {
        JSONArray jsonObject = new JSONArray(params);
        OkGo.post(url)
                .tag(context)
                .upJson(jsonObject)
                .execute(getStringCallback(context, showProgreeDialog, null, onResponseData));
    }

    public static void requestJson(final Context context, String url, ArrayList params, final boolean showProgreeDialog,IStatusLayout statusLayout,  final OnResponseData onResponseData) {
        JSONArray jsonObject = new JSONArray(params);
        OkGo.post(url)
                .tag(context)
                .upJson(jsonObject)
                .execute(getStringCallback(context, showProgreeDialog, statusLayout, onResponseData));
    }

    public static void requestFullJson(final Context context , String url, HashMap<String, Object> params, boolean showProgreeDialog, final OnResponseData onResponseData) {
        requestFullJson(context, "", url, params, showProgreeDialog, onResponseData);
    }

    public static void requestFullJson(final Context context, String tag, String url, HashMap<String, Object> params, boolean showProgreeDialog, final OnResponseData onResponseData) {

        final Context mContext = context;
        final KProgressHUD dialog = KProgressHUDUtils.showLoading(context, "");
        if(showProgreeDialog){
            dialog.show();
        }
        JSONObject jsonObject = new JSONObject(params);
        Object mTag = "";
        if(StringUtils.isNotEmpty(tag)){
            mTag = tag;
        }else {
            mTag = context;
        }

        OkGo.post(url)
                .tag(mTag)
                .upJson(jsonObject)
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i("Network", "Json：" + s);
                        if (context == null) {
                            Logger.i("ActivityIsNull");
                            return ;
                        }
                        if (response.code() == 200) {
                            onResponseData.onSuccess(s, response.code());
                            dialog.dismiss();
                        } else if (response.code() == 401) {

                            refreshAccessToken(context, response);
                            DcnException error = new DcnException(response.code(), response.message(), "");
                            dialog.setDetailsLabel(error.getMessage());
                            onResponseData.onError(error);
                            KProgressHUDUtils.dismissLoading(context, dialog, dimissAfterSeconds);
                        } else {
                            DcnException error = new DcnException(response.code(), response.message(), "");
                            //dialog.setDetailsLabel(error.getMessage());
                            onResponseData.onError(error);
                            KProgressHUDUtils.dismissLoading(context, dialog, dimissAfterSeconds);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.i("WY", "Error:"+e.getMessage()+"/nJson："+ response);
                        if (context == null) {
                            Logger.i("ActivityIsNull");
                            return ;
                        }
                        DcnException error = new DcnException(e);
                        //dialog.setDetailsLabel(error.getMessage());
                        onResponseData.onError(error);
                        KProgressHUDUtils.dismissLoading(context, dialog, dimissAfterSeconds);
                    }
                });
    }

    public static void authUserForAccessToken(Context context, String account, String password, final OnAuthForToken onAuthForToken) {
        String url = ((BaseActivity)context).getGlobalParams().requestUrl.SERVER_OAUTH +
                "client_id=mobile&client_secret="+((BaseActivity)context).getGlobalParams().requestUrl.AUTH_SECRET_KEY
                +"&grant_type=password&scope=read%20write&username=" +account+"&password="+password;
        getToken(url, onAuthForToken);
    }


    //刷新 token
    public static void refreshAccessToken(Context context, final OnAuthForToken onAuthForToken) {
//        String url = ((BaseActivity)context).getGlobalParams().requestUrl.SERVER_OAUTH +
        String url = GlobalParams.getInstance().requestUrl.SERVER_OAUTH +
                "client_id=mobile&client_secret="+((BaseActivity)context).getGlobalParams().requestUrl.AUTH_SECRET_KEY+"&grant_type=refresh_token&refresh_token="+ GlobalParams.getInstance().getRefreshToken();
        //https://testorder.crm.atlasoffice.cn/authz/oauth/token?client_id=mobile&client_secret=mobile&grant_type=refresh_token&refresh_token=08d7311a7bc84b95933a1ae2f25aa10c
        getToken(url, onAuthForToken);
    }

    private static void getToken(String url, final OnAuthForToken onAuthForToken){
        OkGo.post(url)//
                //.tag()
                //.upJson(jsonObject.toString())//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i("Network", "Json：" + s);
                        if (response.code() == 200) {
                            Gson gson = new Gson();
                            AuthResponse authResponse = gson.fromJson(s, AuthResponse.class);
                            onAuthForToken.onSuccess(authResponse.access_token, authResponse.refresh_token);
                        } else if (response.code() == 401) {
                            Gson gson = new Gson();
                            AuthErrorResponse authErrorResponse = gson.fromJson(s, AuthErrorResponse.class);
                            DcnException error = new DcnException(response.code(), authErrorResponse.error, authErrorResponse.error_description);
                            onAuthForToken.onError(error);
                        } else {
                            DcnException error = new DcnException(response.code(), response.message(), s);
                            onAuthForToken.onError(error);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Logger.i("WY", "Error:"+e.getMessage()+"/nJson："+ response);
                        DcnException error = new DcnException(e);
                        onAuthForToken.onError(error);
                    }
                });
    }




}


