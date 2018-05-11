package com.atlas.crmapp.commonactivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.atlas.crmapp.MainActivity;
import com.atlas.crmapp.R;
import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.activity.index.IndexActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.Utils;
import com.atlas.crmapp.model.CenterUnitJson;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.model.VersionInfoJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.util.AppUtil;
import com.atlas.crmapp.util.KProgressHUDUtils;
import com.atlas.crmapp.util.MTAUtils;
import com.atlas.crmapp.util.RegisterCommonUtils;
import com.atlas.crmapp.util.ScreenUtils;
import com.atlas.crmapp.util.SpUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.logger.Logger;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class WelcomeActivity extends BaseActivity {

    private List<ResourceJson.ResourceMedia> welcomeResUrls = new ArrayList<>();//欢迎页面 图片url

    private int needDonloadNum;

    @BindView(R.id.btnNav)
    Button btnNav;
    @BindView(R.id.tv_skip)
    TextView tvSkip;//跳过

    @BindView(R.id.rl_image)
    RelativeLayout rlImageView;

    private VersionInfoJson versionInfo;
    private boolean isStartInstall;

    double mLat = 22.36;
    double mLng = 113.658;
    //声明AMapLocationClient类对象
    AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (location != null) {
                if(location.getErrorCode() == 0 ){
                    mLat = location.getLatitude();//获取纬度
                    mLng = location.getLongitude();//获取经度
                }
            }else{
            }
            mLocationClient.stopLocation(); //停止定位后，本地定位服务并不会被销
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
            initLoadingData();
        }
    };

    @OnClick({R.id.btnNav,R.id.tv_skip} )
    void onNav() {
        SpUtil.putBoolean(WelcomeActivity.this, Constants.SpKey.KEY_IS_FIRST_INSTALL, false);
//        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        Intent intent = new Intent(WelcomeActivity.this, IndexActivity.class);
        WelcomeActivity.this.startActivity(intent);
        WelcomeActivity.this.finish();
    }

    private KProgressHUD loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.setCurrentActivityFullScrenn(this);
        initLanguage();
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        MTAUtils.startMAT(this);
        Utils.readAccountToken(this);
        if(SpUtil.getBoolean(WelcomeActivity.this, Constants.SpKey.KEY_IS_FIRST_INSTALL, true)){
            showLoadingDialog();
            AppUtil.getUUID(this);// 生成 uuid == 设备号
        }
        WelcomeActivityPermissionsDispatcher.needLocationPermissionWithCheck(this);


        Intent intent = getIntent();
        if(intent != null){
            String scheme = intent.getScheme();
            Uri uri = intent.getData();
            Logger.d("scheme    "  + scheme);
            if (uri != null) {
                String host = uri.getHost();
                String dataString = intent.getDataString();
                //获得参数值
                String key1 = uri.getQueryParameter("key1");
            }
            if("atlasApp".equals(scheme)){
                MTAUtils.trackCustomEvent(this, "opent_source");
            }
        }

    }

    private void showLoadingDialog(){
        if(loadingDialog == null){
            loadingDialog = KProgressHUDUtils.getKProgressHUD(this);
        }
        loadingDialog.setCancellable(false);
        loadingDialog.show();
    }



    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void needLocationPermission(){
        startToLocation();
    }

    //开始定位
    private void startToLocation(){
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WelcomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void deniedLocationPermission(){
        showToast(getString(R.string.please_open_permisstion_location));
        startToLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initViewPage(){

        ViewStub vsViewPager = (ViewStub) findViewById(R.id.vs_view_pager);
        if(vsViewPager != null){
            vsViewPager.inflate();
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.guide_viewPager);
        Indicator indicator = (Indicator) findViewById(R.id.guide_indicator);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position+1 == welcomeResUrls.size()){
                    btnNav.setVisibility(View.VISIBLE);
                    tvSkip.setVisibility(View.GONE);
                }else{
                    tvSkip.setVisibility(View.VISIBLE);
                    btnNav.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        indicatorViewPager.setAdapter(adapter);
    }

    private IndicatorViewPager.IndicatorPagerAdapter adapter = new IndicatorViewPager.IndicatorViewPagerAdapter() {

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView =  LayoutInflater.from(WelcomeActivity.this).inflate(R.layout.tab_guide, container, false);
            }
            return convertView;
        }

        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {
            ImageView view = (ImageView) convertView;
            if (view == null) {
                view = new ImageView(WelcomeActivity.this);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(WelcomeActivity.this).load(welcomeResUrls.get(position).url).apply(new RequestOptions().centerCrop()).into(view);
            return view;
        }

        @Override
        public int getItemPosition(Object object) {
            //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
            // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return welcomeResUrls.size();
        }
    };

    private void initLoadingData(){
        BizDataRequest.requestNearestCenterUnit(this, String.valueOf(mLat), String.valueOf(mLng), new BizDataRequest.OnRequestNearestCenterUnit() {
            @Override
            public void onSuccess(CenterUnitJson centerUnitJson) {
                CenterUnitJson  oldCenterUnit = SpUtil.getGson(WelcomeActivity.this, Constants.SpKey.KEY_CENTER_INFO, CenterUnitJson.class);
                if( oldCenterUnit != null && oldCenterUnit.id != centerUnitJson.id  && oldCenterUnit.id != 0){
                    showChangeAtlasCenter(oldCenterUnit, centerUnitJson);
                }else{
                    initUnits(centerUnitJson);
                }
            }

            @Override
            public void onError(DcnException error) {
                Logger.e( error.getMessage());
                CenterUnitJson centerUnitJson = new CenterUnitJson();
                centerUnitJson.id = 4;
                centerUnitJson.name = "雅居乐中心";
                centerUnitJson.cityName = "广州";
                initUnits(centerUnitJson);
                showNetErrorToRetry();
            }
        });
    }


    public void showChangeAtlasCenter(final CenterUnitJson oldCenterUnitJson, final CenterUnitJson centerUnitJson){

        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(R.string.text_67)
                .setMessage(getString(R.string.t1) + centerUnitJson.name )
                .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        initUnits(oldCenterUnitJson);
                    }
                })
                .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initUnits(centerUnitJson);
                    }
                })
                .setCancelable(true)
                .show();
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                initUnits(oldCenterUnitJson);
            }
        });
    }



    private void initUnits(CenterUnitJson centerUnitJson) {
        SpUtil.setGson(WelcomeActivity.this, Constants.SpKey.KEY_CENTER_INFO, centerUnitJson);
        getGlobalParams().setAtlasId(centerUnitJson.id);
        getGlobalParams().setAtlasName(centerUnitJson.name);
        getGlobalParams().setKeyfree(centerUnitJson.keyfree);
        RegisterCommonUtils.initUnits(WelcomeActivity.this, getGlobalParams(), centerUnitJson, true, new RegisterCommonUtils.OnDownLoadResImageListener() {
            @Override
            public void needDownLoadImage(ArrayList<ResourceJson.ResourceMedia> resList) {
                //downloadResImage(resList);
                if(SpUtil.getBoolean(WelcomeActivity.this, Constants.SpKey.KEY_IS_FIRST_INSTALL ,true)){//加载欢迎页图片
                    requestWelcomeUrl();
                }else {
                    onImagesDownloadComplete();
                }
            }
        }, new RegisterCommonUtils.OnResponseListener() {
            @Override
            public void onError(DcnException error) {
                showNetErrorToRetry();
            }
        });
    }




    private void requestWelcomeUrl(){

        int type = (int) SpUtil.getLong(this,SpUtil.LANGUAGE,-1);
        String url = "app/loading";
        if (type == -1) {
            String locale = Locale.getDefault().toString();
            if (Locale.SIMPLIFIED_CHINESE.toString().equals(locale)) {
                url = "app/loading";
            } else if (Locale.TRADITIONAL_CHINESE.toString().equals(locale)) {
                url = "app/loading/hk";
            } else if ("en_US".equals(locale)) {
                url = "app/loading/en";
            }  else if ("en".equals(locale)) {
                url = "app/loading/en";
            }else {
                url = "app/loading";
            }
        }else {
            switch (type){
                case 0:
                    url = "app/loading";
                    break;
                case 1:
                    url = "app/loading/hk";
                    break;
                case 2:
                    url = "app/loading/en";
                    break;
            }
        }

        BizDataRequest.requestResource(WelcomeActivity.this, String.valueOf(getGlobalParams().getAtlasId()), url, new BizDataRequest.OnRequestResource() {
            @Override
            public void onSuccess(ResourceJson resourceJson) {
                if(resourceJson.rows!=null&&resourceJson.rows.size()>0) {
                    welcomeResUrls = resourceJson.rows.get(0).resourceMedias;
                    needDonloadNum = welcomeResUrls.size();
                    for(int i = 0; i < welcomeResUrls.size(); i++ ){
                        final ResourceJson.ResourceMedia media = welcomeResUrls.get(i);
                        media.url = LoadImageUtils.loadLargeImage(media.url);
                        glideLoadWelcomeImage(media.url);
                    }
                }
            }
            @Override
            public void onError(DcnException error) {
                showNetErrorToRetry();
                rlImageView.setVisibility(View.GONE);
            }
        });
    }




    private int needDownloadImage = 0;
    private void downloadResImage(List<ResourceJson.ResourceMedia> resList)  {
        needDownloadImage += resList.size();
        for (int i=0; i< resList.size(); i++) {
            final ResourceJson.ResourceMedia res = resList.get(i);
            glideLoadImage(res.url);
        }
    }


    //加载主页面 图片
    private void glideLoadImage(final String realUrl){
        Glide.with(getApplicationContext())
                .load(realUrl)
                .apply(new RequestOptions()
                .dontAnimate()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                ).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                needDownloadImage--;
                if (needDownloadImage == 0) {
                    if(SpUtil.getBoolean(WelcomeActivity.this, Constants.SpKey.KEY_IS_FIRST_INSTALL ,true)){//加载欢迎页图片
                        requestWelcomeUrl();
                    }else {
                        onImagesDownloadComplete();
                    }
                }
                Logger.d("needDownloadImage---success-- "  + realUrl+ " ,needDownloadImage:" +needDownloadImage);
            }
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
//
//            }
//

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                glideLoadImage(realUrl);
                String msg = "";
//                if(e!= null){
//                    msg =e.getMessage();
//                }
//                showNetErrorToRetry();
//                Logger.e("needDownloadImage---fail---  "  + realUrl + " ,needDownloadImage:" +needDownloadImage +"  e.getMessage()  is "  + msg);
            }

//            @Override
//            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                super.onLoadFailed(e, errorDrawable);
//                glideLoadImage(realUrl);
//                String msg = "";
//                if(e!= null){
//                    msg =e.getMessage();
//                }
//                showNetErrorToRetry();
//                Logger.e("needDownloadImage---fail---  "  + realUrl + " ,needDownloadImage:" +needDownloadImage +"  e.getMessage()  is "  + msg);
//            }
        });
    }


    //加载 欢迎页面 图片
    private void glideLoadWelcomeImage(final String realUrl){

        Glide.with(getApplicationContext())
                .load(realUrl)
                .apply(new RequestOptions()
                .dontAnimate()
                .centerCrop()
                .skipMemoryCache(true))
                .into(new SimpleTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//
//                    }
                    @Override
                    public void onLoadFailed( Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        glideLoadWelcomeImage(realUrl);
                        showNetErrorToRetry();
                        Logger.e("needDownload WelcomeImage---fail-- "+ realUrl  + " ,needDonloadNum:" +needDonloadNum+ " ,error message");
                    }

                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        needDonloadNum --;
                        if(needDonloadNum == 0){
                            onImagesDownloadComplete();
                            initViewPage();
                        }
                        Logger.d("needDownload WelcomeImage-- "  + realUrl   + "  needDonloadNum:" +needDonloadNum);
                    }
                });

    }



    private void onImagesDownloadComplete() {
        adapter.notifyDataSetChanged();
        if(!this.isFinishing()){
            KProgressHUDUtils.dismissLoading(this, loadingDialog, 0);
        }
        rlImageView.setVisibility(View.GONE);
        if(!SpUtil.getBoolean(WelcomeActivity.this, Constants.SpKey.KEY_IS_FIRST_INSTALL ,true)){
//            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            Log.i("RegisterCommonUtils","RegisterCommonUtils");
            Intent intent = new Intent(WelcomeActivity.this, IndexActivity.class);
            WelcomeActivity.this.startActivity(intent);
            WelcomeActivity.this.finish();
        }
    }

    private AlertDialog alertDialog ;
    public void showNetErrorToRetry() {
        if(alertDialog == null){
            alertDialog = new AlertDialog.Builder(this).setTitle(R.string.text_67)
                    .setMessage(R.string.t2)
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            WelcomeActivity.this.finish();
                        }
                    })
                    .setPositiveButton(R.string.t3, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            WelcomeActivityPermissionsDispatcher.needLocationPermissionWithCheck(WelcomeActivity.this);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }else{
            if(!alertDialog.isShowing()){
                if(loadingDialog != null){
                    loadingDialog.dismiss();
                }
                alertDialog.show();
            }
        }
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                showLoadingDialog();
            }
        });

    }


    private void initLanguage(){

        String locale = Locale.getDefault().toString();
        int type = (int) SpUtil.getLong(this,SpUtil.LANGUAGE,-1);
        Log.i("initLanguage","locale = " + locale + " ; =" + Locale.ENGLISH.toString() + "; type = " + type);
        //获取当前资源对象
        Resources resources = getResources();
        //获取设置对象
        Configuration configuration = resources.getConfiguration();
        //获取屏幕参数
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();

        if (type == -1)
            if (Locale.SIMPLIFIED_CHINESE.toString().equals(locale)){
                type = 0;
            }else if (Locale.TRADITIONAL_CHINESE.toString().equals(locale) || "zh-rHK".equals(locale) ){
                type = 1;
            }else if (locale.startsWith("en")){
                type = 2;
            }else{
                type = 0;
            }
        Locale locale_ = Locale.SIMPLIFIED_CHINESE;;
        //发送结束所有activity的广播
        switch (type){
            case 0:
                locale_ = Locale.SIMPLIFIED_CHINESE;
                break;
            case 1:
                locale_ = Locale.TRADITIONAL_CHINESE;
                break;
            case 2:
                locale_ = Locale.ENGLISH;
                break;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale_);
        } else {
            configuration.locale = locale_;
        }

        resources.updateConfiguration(configuration, displayMetrics);
    }


}
