package com.atlas.crmapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.atlas.crmapp.adapter.WheelImageAdapter;
import com.atlas.crmapp.coffee.OrderConfirmActivity;
import com.atlas.crmapp.common.BizCode;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.data.ImageData;
import com.atlas.crmapp.fragment.base.BaseFragment;
import com.atlas.crmapp.model.PrintJson;
import com.atlas.crmapp.model.PrintLogin;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.service.PrintService;
import com.atlas.crmapp.usercenter.ActivitiesVerificationActivity;
import com.atlas.crmapp.usercenter.RechargeActivity;
import com.atlas.crmapp.usercenter.UserCardActivity;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.util.AppUtil;
import com.atlas.crmapp.util.ClickUtil;
import com.atlas.crmapp.util.MTAUtils;
import com.atlas.crmapp.util.ScreenUtils;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.util.UiUtil;
import com.atlas.crmapp.view.popupwindow.MaskImageView;
import com.atlas.crmapp.widget.CodeDialog;
import com.atlas.crmapp.widget.ImageCursorWheelLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.shizhefei.view.indicator.BannerComponent;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;
import github.hellocsl.cursorwheel.CursorWheelLayout;

import static com.atlas.crmapp.R.id.cursor_wheel_layout;
import static com.atlas.crmapp.R.id.iv_wheel_icon;


/**
 * Created by Alex on 2017/5/6.
 */

public class MainFragment extends BaseFragment{
    public static final String TAG = MainFragment.class.getSimpleName();
    private BannerComponent bannerComponent;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.iv_mask)
    MaskImageView ivHeaderView;

    @BindView(R.id.arrow_view)
    LottieAnimationView arrowView;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_content)
    TextView tvContent;

    @BindView(iv_wheel_icon)
    ImageView ivWheelIcon;

    @BindView(R.id.desc_view)
    RelativeLayout descView;//主页 背景

    @BindView(cursor_wheel_layout)
    ImageCursorWheelLayout mCursorWheelLayout;

    @BindView(R.id.iv_code)
    ImageView ivCode;

    @BindView(R.id.linearLayoutHeader)
    LinearLayout llHeader;

    private CodeDialog mCodeDialog;

    private int screenWidth;
    private int screenHeight;
    private int lastY;
    private static String KEY_IS_SHOW_CODE = "KEY_IS_SHOW_CODE";

    private Animation scaleCode;
    private int headViewTop;
    private int arrowViewTop;

    private int currentTab;

    private Boolean isViewPagerAnimation;
    @OnClick(R.id.iv_scan_to_scan)
    void onClickToScanCode(){
        if(getGlobalParams().isLogin()) {
            MTAUtils.trackCustomEvent(getActivity(), "mian_scan");
            IntentIntegrator.forSupportFragment(MainFragment.this).initiateScan();
        } else {
            showAskLoginDialog(getHoldingActivity());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
            } else {
                if(requestCode == IntentIntegrator.REQUEST_CODE){

                    final String urlCode = result.getContents();
                    if(TextUtils.isEmpty(urlCode)){
                        return;
                    }
                    Logger.d("result.getContents()====" + urlCode);
                    Intent intent = null;
                    if(urlCode.contains(Constants.CodeSkipType.TO_USER)){
                        intent = new Intent(getHoldingActivity(), UserCardActivity.class);
                        intent.putExtra("code",result.getContents());
                        startActivity(intent);
                    }else if(urlCode.contains(Constants.CodeSkipType.TO_RECHARGE)){
                        intent = new Intent(getActivity(),RechargeActivity.class);
                        String[]  urlSplit =urlCode.split("/");
                        intent.putExtra("referral", urlSplit[urlSplit.length-1]);
                        startActivity(intent);
                    }else if(urlCode.contains(Constants.CodeSkipType.TO_ACTIVITY_VERIFI)){
                        BizDataRequest.requestAppUserGeteam(getActivity(), new BizDataRequest.OnResponseAppUserGeteam() {
                            @Override
                            public void onSuccess(boolean isGeteam) {
                                if(isGeteam){
                                    ActivitiesVerificationActivity.newInstance(getActivity(), urlCode);
                                }else{
                                    Logger.e(" is not csr-----");
                                }
                            }

                            @Override
                            public void onError(DcnException error) {

                            }
                        });


                    }else if(urlCode.contains(Constants.CodeSkipType.LOGIN_PC)){
                        printLog(StringUtils.getLoginInfo(urlCode));
                    }else if (urlCode.contains(Constants.CodeSkipType.UNLOCK_PRINT)){
                        unlockPrint(StringUtils.unLockPrint(urlCode));
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @OnTouch(R.id.iv_mask)
    boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastY = (int) event.getRawY();
                Logger.d("lastY ---" + lastY);
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) event.getRawY() - lastY;
                lastY = (int) event.getRawY();
                if(ivHeaderView.getTop() > - headViewTop){
                    return true;
                }
                if (v.getTop() + dy > headViewTop  && !isMoveToDisappearing) {
                    /*Logger.d("v.getTop()--- " + v.getTop() +"  headViewTop "+ headViewTop  +"   isMoveToDisappearing:" + isMoveToDisappearing);
                    Logger.d("dy---  " + dy);*/
                    if( ivHeaderView.getTop() > 0 ){// 设置到底部时不能继续下拉。   v.getTop() 为正数时下拉至底部    ivHeaderView.getTop() > 0 && dy>0  ||
                        ivHeaderView.setTop(0);
                        return true;
                    }
                    v.offsetTopAndBottom(dy);//dy  正数向下移动，负数向上移动
                    arrowView.offsetTopAndBottom(dy);
                    ivHeaderView.setRadius(dy);
                }else{
                    ivHeaderView.setTop(headViewTop);
                    arrowView.setTop(arrowViewTop);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (v.getTop() < headViewTop) {
                } else {
                    if (v.getTop() > headViewTop * 0.60) {
                        moveToDisappear(-v.getTop(), true);
                    } else {
                        moveToDisappear(headViewTop - ivHeaderView.getTop(), false);
                    }

                }
                break;
        }

        return true;
    }

    @OnClick(R.id.arrow_view)
    void onClickArrowView(){
        if(ClickUtil.isFastDoubleClick()){
            return;
        }
         moveToDisappear(-ivHeaderView.getTop(), true);
    }

   @OnClick(R.id.v_span)
    void onClickIvWheelIcon(){
       if (ClickUtil.isFastDoubleClick()) {
           return ;
       }
       moveToDisappear(-ivHeaderView.getTop(), true);
    }

/*    float  down;
    @OnTouch(R.id.iv_wheel_icon)
    boolean onTouchWhellIcon(View view, MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                event.getRawX();
                down = event.getRawX();
                Logger.d( "down : " + down);
                break;
            case MotionEvent.ACTION_UP:
                float  up = event.getRawX();
                if(Math.abs(down - up) < 50){//点击时  maskview 向下滑动
                    if (ClickUtil.isFastDoubleClick()) {
                        return true;
                    }
                    moveToDisappear(-ivHeaderView.getTop(), true);
                }
                break;
        }
        return false;
    }*/


    @OnClick(R.id.iv_code)
    public void showUserCode() {

        if ((getHoldingActivity()).getGlobalParams().isLogin()) {

            if (mCodeDialog == null) {
                mCodeDialog = CodeDialog.newInstance();
            }

            if (!mCodeDialog.isAdded() && !mCodeDialog.isVisible() && !mCodeDialog.isRemoving()) {
                mCodeDialog.show(getHoldingActivity().getSupportFragmentManager(), "code_dialog");
            }
        } else {
            showAskLoginDialog(getHoldingActivity());
        }
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }


    public static MainFragment newInstance(boolean isShowCode) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_IS_SHOW_CODE, isShowCode);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initFragmentViews(View inflateView, Bundle savedInstanceState) {
        initViewpager();
        initNavMenu();
        initTopView();
        scaleCode();
        setViewPageCurrentItem(getGlobalParams().getCurrentBizCode());
        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), Constants.STATUS_BAR_ALPHA.BAR_ALPHA , llHeader);
        //BarTextColorUtils.StatusBarLightMode(getActivity());

        if(getArguments() != null){
            boolean isShowCode = getArguments().getBoolean(KEY_IS_SHOW_CODE);
            if(isShowCode){
                showUserCode();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void scaleCode(){
        if(scaleCode == null){
            scaleCode = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_code_scale);
        }
        ivCode.setAnimation(scaleCode);
    }



    @Override
    protected void initFragmentData(Bundle savedInstanceState) {

    }

    //设置mask  位置
    private  void initTopView(){

        int maskMarginTop = (int)(ScreenUtils.getScreenHeight(getHoldingActivity())* 0.65);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivHeaderView.getLayoutParams();
        lp.height = ScreenUtils.getScreenHeight(getHoldingActivity()) + UiUtil.dipToPx(getActivity(), 60);
        lp.width = ScreenUtils.getScreenWidth(getHoldingActivity());
        lp.setMargins(0, 0-maskMarginTop, 0, 0);

        int headerViewBottomY =  lp.height - maskMarginTop;
        RelativeLayout.LayoutParams awlp = (RelativeLayout.LayoutParams) arrowView.getLayoutParams();
        awlp.setMargins(0, (int)(headerViewBottomY - (lp.height * 0.24)), 0, 0);


        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels - 50;

        ivHeaderView.post(new Runnable() {
            @Override
            public void run() {
                if (ivHeaderView != null) {
                    headViewTop = ivHeaderView.getTop();
                    arrowViewTop = arrowView.getTop();
                }
            }
        });
    }

    private void initNavMenu() {
        final List<ImageData> imageDatas = new ArrayList<>();

        imageDatas.add(new ImageData(R.drawable.selector_ic_wp, "work_place"));
        imageDatas.add(new ImageData(R.drawable.selector_ic_coffee, "coffee"));
        imageDatas.add(new ImageData(R.drawable.selector_ic_kitchen, "kitchen"));
        imageDatas.add(new ImageData(R.drawable.selector_ic_gym, "gym"));
        imageDatas.add(new ImageData(R.drawable.selector_ic_gogreen, "gogreen"));
        imageDatas.add(new ImageData(R.drawable.selector_ic_studio, "studio"));

        final WheelImageAdapter simpleImageAdapter = new WheelImageAdapter(getHoldingActivity(), imageDatas);

        mCursorWheelLayout.setAdapter(simpleImageAdapter);
        mCursorWheelLayout.setOnMenuSelectedListener(new CursorWheelLayout.OnMenuSelectedListener() {
            @Override
            public void onItemSelected(CursorWheelLayout parent, View view, int pos) {
                currentTab = pos;
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("position",pos);
                msg.setData(bundle);
                msg.what = 10;
//                mUIhandler.sendMessageDelayed(msg, 10);
                mUIhandler.sendMessage(msg);
            }
        });

    }

    private void initViewpager() {
        Indicator indicator = (Indicator) getHoldingActivity().findViewById(R.id.banner_indicator);
        indicator.setScrollBar(new ColorBar(getHoldingActivity(), Color.WHITE, 0, ScrollBar.Gravity.CENTENT_BACKGROUND));
        viewPager.setOffscreenPageLimit(7);
        bannerComponent = new BannerComponent(indicator, viewPager, false);
        bannerComponent.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 页面正在滚动时不断调用

            }

            @Override
            public void onPageSelected(int position) {
                // 页面选中时调用
                //if(!bIsFirstOpen) {

                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                msg.setData(bundle);
                msg.what = 10;
                mWheelhandler.sendMessage(msg);
                isViewPagerAnimation =true;
                //}

                //moveToDisappear(headViewTop - ivHeaderView.getTop(), isHidden);
                mAnimation = new TranslateAnimation(0, 0, 0, 0);
                mAnimation.setDuration(0);
                ivHeaderView.startAnimation(mAnimation);
                arrowView.startAnimation(mAnimation);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 页面的滚动状态变化时调用



            }
        });


    }

    //private String[] title = {"ATLAS COFFEE", "ATLAS WORKPLACE", "ATLAS FITNESS", "ATLAS KITCHEN", "ATLAS KITCHEN", "ATLAS FITNESS"};
    //private String[] content = {"美好的一天\n从寰图咖啡开始", "工作不再是朝九晚五", "高端健身品牌寰图健身工房\n让您工作健身两不误", "除却味蕾的快感\n美食应该还能带来美的感受", "高端健身品牌寰图健身工房\n让您工作健身两不误", "除却味蕾的快感\n美食应该还能带来美的感受"};
    //private int[] images = {R.drawable.ic_main_wp,R.drawable.ic_main_coffee,  R.drawable.ic_main_fitness, R.drawable.ic_main_kitchen, R.drawable.ic_main_coffee, R.drawable.ic_main_wp};
    private int maskImageRes [] = {R.drawable.mask_wp,R.drawable.mask_co, R.drawable.mask_kc, R.drawable.mask_ft, R.drawable.mask_gg, R.drawable.mask_st};
    private int[] imageWheelIcon = {R.drawable.ic_wp_selected, R.drawable.ic_coffee_selected, R.drawable.ic_kitchen_selected, R.drawable.ic_gym_selected, R.drawable.ic_gogreen_selected, R.drawable.ic_studio_selected, R.drawable.ic_wp_selected};

    private IndicatorViewPager.IndicatorViewPagerAdapter adapter = new IndicatorViewPager.IndicatorViewPagerAdapter() {

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if(convertView == null){
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.view_main_image, null, true);
            }
            return convertView;
        }

        @Override
        public View getViewForPage(final int position, View convertView, ViewGroup container) {

            if(convertView == null){
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.view_main_image, null, true);
            }
            int resListSize = 0;
            final List<ResourceJson.ResourceMedia> resourceMedias = getGlobalParams().getResList();
            if( resourceMedias != null){
                resListSize = resourceMedias.size();
            }
            if(resourceMedias!=null && resListSize>0) {
                String imageUrl = "";
                if(position >=resListSize){
                    imageUrl = resourceMedias.get(resListSize -1).url;
                }else{
                    imageUrl = resourceMedias.get(position).url;
                }
                ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_main_image);
                imageView.setMaxWidth(screenWidth);
                imageView.setMaxHeight( screenHeight * 2);
                Glide.with(getHoldingActivity().getApplicationContext()).load(imageUrl)
//                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(imageView);
                final String finalImageUrl = imageUrl;
               imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(StringUtils.isNotEmpty(finalImageUrl)){
                            ResourceJson.ResourceMedia media = resourceMedias.get(position);
                            Intent intent =  ActionUriUtils.getIntent(getActivity(), media.actionUri, media.url, GlobalParams.getInstance().getCurrentBusinesseName());
                            if(intent != null){
                                startActivity(intent);
                            }
                        }
                    }
                });
            }

            return convertView;
        }

        @Override
        public int getCount() {
            return maskImageRes.length;
        }
    };


    protected Handler mWheelhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:

                    if (mCursorWheelLayout != null){
                        int position = msg.getData().getInt("position", 0);
                        int item = position % maskImageRes.length;
                        int pos = mCursorWheelLayout.getSelectedPosition();
                        if (item != pos && pos != -1) {
                            mCursorWheelLayout.setSelection(item);
                        }
                    }

                    break;

            }
        }
    };

    protected Handler mUIhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:

                    if (viewPager != null) {
                        int pos = msg.getData().getInt("position", 0);
                        if(currentTab != pos){
                            return;
                        }
                        int count = viewPager.getCurrentItem() % maskImageRes.length;
                        if(isViewPagerAnimation ==false) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + pos - count,false);

                        }else{
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + pos - count);
                        }
                        updateMaskView(pos);
                        changeText(pos);
                        MainActivity parentActivity = (MainActivity ) getActivity();
                        parentActivity.setCurrentFragment(pos);
                        ivWheelIcon.setImageDrawable(ContextCompat.getDrawable(getHoldingActivity(),imageWheelIcon[pos] ));//
                    }
                    break;

            }
        }
    };



    private void changeText(int position) {

        if(getGlobalParams().getmTitleList() != null){
             if(position > getGlobalParams().getmTitleList().size() -1){
                 position = getGlobalParams().getmTitleList().size() -1;
             }
        }

        if (getGlobalParams().getmTitleList() != null){
            if (getGlobalParams().getmTitleList().size() > 0) {
                if (getGlobalParams().getmTitleList().size() >= position && getGlobalParams().getmTitleList().get(position) != null) {
                    tvTitle.setText(getGlobalParams().getmTitleList().get(position).content);
                }
            }
        }
        if (getGlobalParams().getmContentList() != null) {
            if (getGlobalParams().getmContentList().size() > 0) {
                if (getGlobalParams().getmContentList().size() >= position && getGlobalParams().getmContentList().get(position) != null) {
                    tvContent.setText(getGlobalParams().getmContentList().get(position).content);
                }
            }
        }

    }


    private void updateMaskView(final int position) {



        int maskSize = maskImageRes.length;
        int maskRes;
        if(position >= maskSize){
            maskRes = maskImageRes[0];
        }else{
            maskRes = maskImageRes[position];
        }

   /*     Glide.with(getHoldingActivity().getApplicationContext()).load(maskRes).asBitmap().diskCacheStrategy(DiskCacheStrategy.RESULT).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (ivHeaderView != null) {
                    ivHeaderView.setImageBitmap(resource);
                    ivHeaderView.setBitmapShader();
                    ivHeaderView.invalidate();
                }
            }
        });*/

//        ivHeaderView.setImageResource(maskRes);
//        ivHeaderView.setImageResource(maskRes);
        ivHeaderView.setBitmapShader(maskRes);
//        ivHeaderView.setBitmapShader();

    }

    private TranslateAnimation mAnimation;
    private boolean isMoveToDisappearing = false;
    private boolean isHidden ;

    private void moveToDisappear(final int from, final Boolean isHidden) {
        this.isHidden = isHidden;
        Logger.d("from: ---   " +from + "isHidden---" + isHidden);
        mAnimation = new TranslateAnimation(0, 0, 0, from);
        mAnimation.setDuration(500);
        mAnimation.setFillAfter(true);

        ivHeaderView.startAnimation(mAnimation);
        arrowView.startAnimation(mAnimation);
        isMoveToDisappearing = true;
        mAnimation.setInterpolator(new AccelerateInterpolator());
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                    //加载fragment
                ivHeaderView.startSpring(isHidden);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isHidden) {
                    disappear();
                }else{


                }
                isMoveToDisappearing = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }



    // mask消失
    private void disappear() {
        final AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 0.2f);
        alphaAnimation.setDuration(500);
        descView.startAnimation(alphaAnimation);
        final MainActivity parentActivity = (MainActivity ) getActivity();


        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivHeaderView.clearAnimation();
                descView.clearAnimation();
                parentActivity.setCurrentTab(currentTab);

                parentActivity.topViewHidden();
                descView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void setViewPageCurrentItem(BizCode code){
        isViewPagerAnimation = false;
        mCursorWheelLayout.setSelection(bizCodeToPosition(code),false);
    }

    private int bizCodeToPosition(BizCode code) {
        if (code != null) {
            if (code.getBizCode().equals(getGlobalParams().getWorkplaceCode())) {
                return 0;
            } else if (code.getBizCode().equals(getGlobalParams().getCoffeeCode())) {
                return 1;
            } else if (code.getBizCode().equals(getGlobalParams().getKitchenCode())) {
                return 2;
            } else if (code.getBizCode().equals(getGlobalParams().getFitnessCode())) {
                return 3;
            } else if (code.getBizCode().equals(getGlobalParams().getGogreenCode())) {
                return 4;
            } else if (code.getBizCode().equals(getGlobalParams().getStudioCode())) {
                return 5;
            }
        }
        return 0;
    }
    PrintLogin bean;
    private void printLog(final PrintJson json){
        BizDataRequest.requestPrintLogin(getContext(), json, new BizDataRequest.PrintLoginInfo() {
            @Override
            public void onSuccess(PrintLogin bean) {
                if (bean != null)
                    MainFragment.this.bean = bean;
                Toast.makeText(getContext(), R.string.s98,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(DcnException error) {

            }
        });

    }

    private void unlockPrint(PrintLogin bean,String stationId){
//        Log.i("unlockPrint","stationId = " + stationId);
        BizDataRequest.requestPrintUnlock(getContext(), bean.getEntId(), stationId, bean.getUserId(), AppUtil.getIPAddress(getContext()), new BizDataRequest.PrintUnlockInfo() {
            @Override
            public void onSuccess() {
                startServiceForPrint();
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void unlockPrint(String stationId){
//        Log.i("unlockPrint","stationId = " + stationId);
        BizDataRequest.requestPrintUnlock(getContext(),  stationId, AppUtil.getIPAddress(getContext()), new BizDataRequest.PrintUnlockInfo() {
            @Override
            public void onSuccess() {
//                startServiceForPrint();
                startActivity(new Intent(context,PrintDetailActivity.class));
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void startServiceForPrint(){
        getActivity().bindService(new Intent(context,PrintService.class),connection, Context.BIND_AUTO_CREATE);
    }

    PrintService service;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MainFragment.this.service =  ((PrintService.MyBinder)service).getService();
            MainFragment.this.service.setOnOrderCheck(onOrderCheck);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private static final String KEY_ORDER_ID = "KEY_ORDER_ID";
    PrintService.OnOrderCheck onOrderCheck = new PrintService.OnOrderCheck() {
        @Override
        public void onOrderCheck(ResponseOpenOrderJson order) {
            Intent orderIntent = new Intent(context, OrderConfirmActivity.class);
            orderIntent.putExtra("confirmOrder",order);
            orderIntent.putExtra(KEY_ORDER_ID,order.getId());
            orderIntent.putExtra("type",order.getType());

            context.startActivity(orderIntent);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (service != null){
            getActivity().unbindService(connection);
        }
    }
}