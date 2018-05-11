package com.atlas.crmapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.atlas.crmapp.R;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

/**
 * Created by hoda on 2017/7/17.
 */

public class ShareHelper {

    public static void shareToWX(Context context , Bitmap thumbBitmap, String shareUrl, String shareTitle,  String shareDescription, UMShareListener umShareListener){
        shareToWX(context, thumbBitmap, shareUrl, shareTitle, shareDescription, true, umShareListener);
    }

    public static void shareToWX(final Context context , Bitmap thumbBitmap, String shareUrl, String shareTitle, String shareDescription, boolean isShowAppdown, final UMShareListener umShareListener){

        UMWeb web = null;
        final ShareBoardConfig config = new ShareBoardConfig();
        config.setIndicatorVisibility(false);
        config.setShareboardBackgroundColor(context.getResources().getColor(R.color.white_color));
        config.setTitleVisibility(false);
        config.setCancelButtonTextColor(ContextCompat.getColor(context, R.color.gray_simple));
        UMImage image = null;
        if(thumbBitmap != null){
            image = new UMImage(context, BitmapUtils.setBitmapSize(thumbBitmap, 80, 80));//bitmap文件
        }else{
            image = new UMImage(context, R.drawable.icon_share);
        }
        if(StringUtils.isNotEmpty(shareUrl)){
            if(isShowAppdown){
                if(shareUrl.contains("#?")){
                    web = new UMWeb(shareUrl+"&appdown=1");//使分享出去的网页产生 下载应用层。
                }else{
                    web = new UMWeb(shareUrl+"?appdown=1");//使分享出去的网页产生 下载应用层。
                }
            }else {
                web = new UMWeb(shareUrl);
            }
        }

        web.setTitle(shareTitle);//标题
        if(image != null){
            web.setThumb(image);  //缩略图
        }
        if(StringUtils.isEmpty(shareDescription)){
            shareDescription = context.getString(R.string.s74);
        }
        web.setDescription(shareDescription);//描述
        Log.i("shareToWX","shareToWX");
        if(umShareListener != null){
            new ShareAction((Activity) context)
                    .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE)
                    .withMedia(web)
                    .setCallback(new UMShareListener() {
                                     @Override
                                     public void onStart(SHARE_MEDIA share_media) {
                                         umShareListener.onStart(share_media);
//                                         shareScore(context);
                                     }

                                     @Override
                                     public void onResult(SHARE_MEDIA share_media) {
                                         umShareListener.onResult(share_media);
                                         if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE)
                                         shareScore(context);
                                     }



                                     @Override
                                     public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                         umShareListener.onError(share_media,throwable);
                                     }

                                     @Override
                                     public void onCancel(SHARE_MEDIA share_media) {
                                         umShareListener.onCancel(share_media);
                                     }
                                 })
                    .open(config);
        }else{
            new ShareAction((Activity) context)
                    .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE)
                    .withMedia(web)
                    .setCallback(new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {
//                            shareScore(context);
                        }

                        @Override
                        public void onResult(SHARE_MEDIA share_media) {
                            if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE)
                            shareScore(context);
                        }

                        @Override
                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media) {
                        }
                    })
                    .open(config);

        }


    }

    public static void shareToWX(final Context context , Object thumbUrl, final String shareUrl, final String shareTitle, final String shareDescription, final UMShareListener umShareListener) {
        Glide.with(context).load(thumbUrl)
//                .asBitmap()
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        shareToWX(context, BitmapUtils.drawable2Bitmap(resource), shareUrl, shareTitle, shareDescription,  umShareListener);
                    }

//                    @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//
//            }
        });
    }


    /**
     * 发短信
     */
    public static void sendSMS(Activity activity, String smsBody, String address,  int requestCode){
        Uri smsToUri = Uri.parse( "smsto:" );
        Intent sendIntent =  new  Intent(Intent.ACTION_VIEW, smsToUri);
        sendIntent.putExtra("address", address); // 电话号码，这行去掉的话，默认就没有电话
        sendIntent.putExtra( "sms_body", smsBody);
        sendIntent.setType( "vnd.android-dir/mms-sms");
        activity.startActivityForResult(sendIntent, requestCode );
    }


    public static void shareWechartFriend(final Context context , long res, String shareUrl, String shareTitle, String shareDescription, final UMShareListener umShareListener){
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(shareTitle);
        web.setDescription(shareDescription);
        web.setThumb(new UMImage(context, R.drawable.icon_share));
        new ShareAction((Activity) context)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .withMedia(web)//web
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        umShareListener.onStart(share_media);
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        umShareListener.onResult(share_media);
//                        shareScore(context);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        umShareListener.onError(share_media,throwable);
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        umShareListener.onCancel(share_media);
                    }
                } )
                .share();
    }


    private static void shareScore(Context context){
        Log.i("shareToWX","shareScore");
        BizDataRequest.shareScore(context, false, null, new BizDataRequest.OnResponseScoreTranJson() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(DcnException error) {
            }
        });
    }

}