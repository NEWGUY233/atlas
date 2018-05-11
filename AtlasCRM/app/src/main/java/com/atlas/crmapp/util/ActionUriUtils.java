package com.atlas.crmapp.util;

import android.content.Context;
import android.content.Intent;

import com.atlas.crmapp.activity.index.fragment.index.activity.IndexDynamicDetailActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagCentreActivity;
import com.atlas.crmapp.activity.index.fragment.index.activity.TagDetailActivity;
import com.atlas.crmapp.coffee.CoffeeDetailActivity;
import com.atlas.crmapp.coffee.JcVideoPlayerAcitvity;
import com.atlas.crmapp.common.BizCode;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.commonactivity.WebActivity;
import com.atlas.crmapp.ecosphere.ActivitiesDetailActivity;
import com.atlas.crmapp.fitness.CourseDetailActivity;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.usercenter.MyScoreActivity;
import com.orhanobut.logger.Logger;

/**
 * Created by Alex on 2017/5/26.
 */

public class ActionUriUtils {

    public static String productId ="productId";
    public static String url = "url";
    public static String imageUrl ="imageUrl";
    public static String id = "id";
    public static String content = "content";

    public static Intent getIntent(Context context, ResourceJson.ResourceMedia resourceMedia){
        if (resourceMedia != null){
            return  getIntent(context, resourceMedia.actionUri, resourceMedia.url, resourceMedia.content);
        }else {
            Logger.e("resourceMedia   is null");
            return  new Intent();
        }
    }

    public static Intent getIntent(Context context, ResourceJson.ResourceMedia resourceMedia, String loadImageType){
        if (resourceMedia != null){
            return  getIntent(context, resourceMedia.actionUri, LoadImageUtils.loadImage(resourceMedia.url, loadImageType), resourceMedia.content);
        }else {
            Logger.e("resourceMedia  is null");
            return  new Intent();
        }
    }


    public static Intent getIntent(Context context, String uri, String imageUrl, String content){
        Logger.d("getIntent uri " + uri);
        Intent intent= null ;
        if(uri!=null&&!uri.equals("")) {
            int index = uri.indexOf(":");
            if (index != -1) {
                intent = new Intent();
                String action = uri.substring(0, index);
                String url = uri.substring(index + 1, uri.length());

                switch (action) {
                    case "product":
                        GlobalParams.getInstance().setCurrentBizCode(currentBiz( GlobalParams.getInstance().getCoffeeId(),  GlobalParams.getInstance().getCoffeeCode()));
                        intent.setClass(context, CoffeeDetailActivity.class);
                        intent.putExtra(ActionUriUtils.productId, Long.parseLong(url));
                        break;
                    case "url":
                        intent.setClass(context, WebActivity.class);
                        intent.putExtra(ActionUriUtils.url, url);
                        if(StringUtils.isNotEmpty(imageUrl)){
                            intent.putExtra(ActionUriUtils.imageUrl, imageUrl);
                        }
                        break;
                    case "activity":
                        intent.setClass(context, ActivitiesDetailActivity.class);
                        intent.putExtra("id", Long.parseLong(url));
                        break;
                    case  "lesson":
                        intent.setClass(context, CourseDetailActivity.class);
                        intent.putExtra("id", Long.parseLong(url));
                        break;
                    case "video":
                        intent.setClass(context, JcVideoPlayerAcitvity.class);
                        intent.putExtra(ActionUriUtils.imageUrl, imageUrl);
                        intent.putExtra(ActionUriUtils.url, url);
                        break;
                    case "bonuspoints":
                        intent.setClass(context, MyScoreActivity.class);
                        break;
                    case "print-job":
//                        intent.setClass(context, MyScoreActivity.class);
                        break;
                    case "topic":
                        intent.setClass(context, TagDetailActivity.class);
                        intent.putExtra(ActionUriUtils.id, url);
                        break;
                    case "forum":
                        intent.setClass(context, TagCentreActivity.class);
                        intent.putExtra("id", url);
                        break;
                    case "moment":
                        intent.setClass(context, IndexDynamicDetailActivity.class);
                        intent.putExtra("id", url);
                        break;
                    case "page":
                        if (getPage(url) == null){
                            intent = null;
                            break;
                        }else
                            intent.setClass(context, getPage(url));
                        break;
                    default:
                        intent = null;
                        break;

                }
                if(StringUtils.isNotEmpty(content) && intent != null){
                    intent.putExtra(ActionUriUtils.content, content);
                }
            }else{
                Logger.e("ActionUriUtils intent is null");
            }
        }
        return intent;
    }

    private static Class getPage(String url){
        switch (url){
            case "bonuspoints":
            case "points":
                return MyScoreActivity.class;
        }

        return null;
    }

    private static BizCode currentBiz(long id, String code){
        BizCode bizCode = new BizCode();
        bizCode.setUnitId(id);
        bizCode.setBizName(GlobalParams.getInstance().getBusinesseName(code));
        bizCode.setBizCode(code);
        return  bizCode;
    }

    //分享活动时 修改url
    public static String getShareActivityUrl(long activityId ){
        return GlobalParams.getInstance().requestUrl.APPSERVER +"app/activity.html#?id="+  activityId ;
    }
}
