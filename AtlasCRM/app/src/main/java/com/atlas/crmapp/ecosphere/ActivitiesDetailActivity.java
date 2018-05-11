package com.atlas.crmapp.ecosphere;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.atlas.crmapp.R;
import com.atlas.crmapp.adapter.ActImagesAdapter;
import com.atlas.crmapp.coffee.OrderConfirmActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.commonactivity.PhotoViewActivity;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.map.AMapUtil;
import com.atlas.crmapp.model.ActivitiesBindinfoJson;
import com.atlas.crmapp.model.ActivityJson;
import com.atlas.crmapp.model.ActivityUserJson;
import com.atlas.crmapp.model.ActivityUsersJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.network.LoadImageUtils;
import com.atlas.crmapp.usercenter.MyActivitiesAppointmentDetailActivity;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.ShareHelper;
import com.atlas.crmapp.util.TopBarScrollTransUtils;
import com.atlas.crmapp.view.ActivitiesUsersView;
import com.atlas.crmapp.view.ObserverScrollView;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnClick;
import tangxiaolv.com.library.EffectiveShapeView;

public class ActivitiesDetailActivity extends BaseStatusActivity implements GeocodeSearch.OnGeocodeSearchListener {

/*    @BindView(R.id.recyclerview_horizontal)
    RecyclerView rcUser;*/

    @BindView(R.id.v_activites_user)
    ActivitiesUsersView vActivitiesUser;
    @BindView(R.id.recyclerview_images)
    RecyclerView rcImages;
    @BindView(R.id.iv_shape)
    EffectiveShapeView mIvShape;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    @BindView(R.id.tv_number_people)
    TextView mTvNumberPeople;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.btn_apply)
    TextView mBtnApply;
    @BindView(R.id.tv_limt_person_num)
    TextView tvLimtPersonNum;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.mv_map)
    MapView mMapView;
    @BindView(R.id.v_my_scroll)
    ObserverScrollView vMyScroll;

    @BindView(R.id.textViewTitle)
    TextView tvTitle;
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.ibHome)
    ImageButton ibHome;
    @BindView(R.id.rl_top_bar)
    RelativeLayout tlTopBar;


    private ActivitiesBindinfoJson bindinfoJson;

    @OnClick(R.id.ibBack)
    void onClickBack(){
        finish();
    }

    @OnClick(R.id.ibHome)
    void onClickHome(){
        if(activityJson != null){
            ShareHelper.shareToWX(ActivitiesDetailActivity.this, LoadImageUtils.loadMiddleImage(activityJson.medias.get(0).url), ActionUriUtils.getShareActivityUrl(id), activityJson.name, activityJson.description, null);
        }
    }

    private long id;
    private GeocodeSearch mGeocodeSearch;
    private AMap aMap;
    private Marker geoMarker;
    private ActivityJson activityJson;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_detail);
        EventBusFactory.getBus().register(this);
        tvTitle.setText("");
        id = getIntent().getLongExtra("id", 0);
        TopBarScrollTransUtils.setImage(ibHome, R.drawable.ic_share_transparent);
        TopBarScrollTransUtils.setImage(ibBack, R.drawable.white_back_transparent);

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        //构造 GeocodeSearch 对象，并设置监听。
        mGeocodeSearch = new GeocodeSearch(this);
        mGeocodeSearch.setOnGeocodeSearchListener(this);
        aMap.moveCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(113.658, 22.36), 18, 30, 30)));
        aMap.clear();
        geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        mBtnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getGlobalParams().isLogin()) {
                    if(activityJson.getCombos() != null && activityJson.getCombos().size() > 0){
                        if(bindinfoJson == null){
                            ActivitiesApplyEnterActivity.newInstance(ActivitiesDetailActivity.this, activityJson);
                        }else{
                             if(Constants.ACTIVITIES_APPLY_STATUS.UNPAID.equals(bindinfoJson.getState())){
                                 OrderConfirmActivity.newInstance(ActivitiesDetailActivity.this, bindinfoJson.getOrderId());
                            }else{
                                 MyActivitiesAppointmentDetailActivity.newInstance(ActivitiesDetailActivity.this, bindinfoJson.getBookingId(), bindinfoJson);
                             }
                        }
                    }else{

                    }
                } else {
                    showAskLoginDialog();
                }
            }
        });

        prepareActivityData();

        vMyScroll.setOnTopBarShowListener(onTopBarShowListener);
        StatusBarUtil.setTransparentForImageView(ActivitiesDetailActivity.this, null);

    }

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    //订单完成刷新界面
    @Subscribe
    public void onEventOrderComplete(Event.EventObject eventObject){
        if(eventObject != null && Constants.EventType.ORDER_COMPLETE.equals(eventObject.type)){
            prepareActivityData();
        }
    }





    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        if (getGlobalParams().isLogin()) {
            BizDataRequest.requestGetActivity(ActivitiesDetailActivity.this, id, new BizDataRequest.OnActivity() {
                @Override
                public void onSuccess(ActivityJson activityJson) {
                    setViewData(activityJson);

                    BizDataRequest.requestActivityBindinfo(ActivitiesDetailActivity.this, 0 , id,  "", null, new BizDataRequest.OnResponseBindinfo() {
                        @Override
                        public void onSuccess(ActivitiesBindinfoJson bindinfoJson) {
                            ActivitiesDetailActivity.this.bindinfoJson = bindinfoJson;
                            setBottomState(null, bindinfoJson);
                        }

                        @Override
                        public void onError(DcnException error) {

                        }
                    });
                }

                @Override
                public void onError(DcnException error) {
                    handlerError(error);
                }
            });



        } else {
            BizDataRequest.requestGetActivityForVisitor(ActivitiesDetailActivity.this, id, new BizDataRequest.OnActivity() {
                @Override
                public void onSuccess(ActivityJson activityJson) {
                    setViewData(activityJson);
                }

                @Override
                public void onError(DcnException error) {
                    handlerError(error);
                }
            });
        }


        BizDataRequest.requestGetActivityUsers(ActivitiesDetailActivity.this, 0, 500, id, false, statusLayout, new BizDataRequest.OnActivityUsers() {
            @Override
            public void onSuccess(ActivityUsersJson activityUsersJson) {
                Collections.reverse(activityUsersJson.rows);
                setUsersView(activityUsersJson.recordsTotal, activityUsersJson.rows);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }

    private void handlerError(DcnException error){
        if(error.getCode()!= Constants.NetWorkCode.NO_NET_WORK){
            showToast(error.getMessage());
            finish();
        }

    }

    @Override
    protected void onRefresh(View view, int id) {
        super.onRefresh(view, id);
        prepareActivityData();
    }



    private void setViewData(ActivityJson activity) {
        this.activityJson = activity;
        if(activity.medias != null && activity.medias.size() > 0) {
            GlideUtils.loadCustomImageView(this,R.drawable.ic_product_thum, LoadImageUtils.loadMiddleImage(activity.medias.get(0).url),mIvShape );
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivitiesDetailActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rcImages.setLayoutManager(linearLayoutManager);
            rcImages.setAdapter(new ActImagesAdapter(ActivitiesDetailActivity.this,activity.medias));
        }

        mTvTitle.setText(activity.name);
        setUmengPageTitle(getString(R.string.t4)+ activity.name);
        mTvLocation.setText(activity.city);
        mTvDate.setText(FormatCouponInfo.getActivityValidTimeMin(activity.startTime, activity.endTime));
        //mTvLimitCost.setText(Html.fromHtml(getString(R.string.activity_limt_person_and_cost, String.valueOf(activity.minSeize),String.valueOf(activity.maxSize), FormatCouponInfo.formatDoublePrice(activity.fee.doubleValue(),2))));
        tvLimtPersonNum.setText(activity.minSize + "~" +activity.maxSize + getString(R.string.text_ren));
        String price = FormatCouponInfo.getYuanStr() + FormatCouponInfo.formatDoublePrice(activity.fee.doubleValue(), 2);;
        if(activity.getCombos() != null ){
            if(activity.getMinfee() != null){
                if(activity.getMinfee().intValue() < activity.getMaxfee().intValue()){
                    price = FormatCouponInfo.getYuanStr() + FormatCouponInfo.formatDoublePrice(activity.getMinfee().doubleValue(),2) + " - "  + FormatCouponInfo.getYuanStr() + FormatCouponInfo.formatDoublePrice(activity.getMaxfee().doubleValue(),2);
                }else{
                    price = FormatCouponInfo.getYuanStr() + FormatCouponInfo.formatDoublePrice(activity.getMinfee().doubleValue(),2);
                }
            }
        }
        tvPrice.setText(price);
        mTvContent.setText(activity.content);
        mTvAddress.setText(getString(R.string.t5) + activity.address);
        GeocodeQuery query = new GeocodeQuery(activity.address, activity.city);
        mGeocodeSearch.getFromLocationNameAsyn(query);
        setBottomState(activity , null);
    }



    private void setBottomState(ActivityJson activity, ActivitiesBindinfoJson bindinfoJson ){
        if(activity != null){
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
            long currentTime =  cal.getTimeInMillis();
            mBtnApply.setEnabled(false);
            if(activity.getApplyEnd() < currentTime) {
                mBtnApply.setText(R.string.t6);
            } else if(activity.getApplyStart() > currentTime){
                mBtnApply.setText(R.string.t7);
            } else if(activity.getApply()){
                mBtnApply.setText(R.string.t8);
            } else if (activity.isInviateUserOnly()){
                mBtnApply.setText(getString(R.string.this_activity_inviateUserOnly));
            }else if(activity.getRemainNum() <= 0 ){
                mBtnApply.setText(R.string.t9);
            }else{
                mBtnApply.setText(R.string.t10);
                mBtnApply.setEnabled(true);
            }
        }
        if(bindinfoJson != null){
            if(Constants.ACTIVITIES_APPLY_STATUS.UNPAID.equals(bindinfoJson.getState())){
                mBtnApply.setText(getString(R.string.unpaid));
            }else{
                mBtnApply.setText(R.string.t11);
            }
            mBtnApply.setEnabled(true);
        }
        mBtnApply.setTextColor(mBtnApply.isEnabled() ? Color.parseColor("#212121") : Color.WHITE);
        mBtnApply.setBackgroundColor(mBtnApply.isEnabled() ? ContextCompat.getColor(this, R.color.btn_yellow) : ContextCompat.getColor(this, R.color.btn_gray));
    }

    private void setUsersView(long total, List<ActivityUserJson> users) {
        mTvNumberPeople.setText(getString(R.string.t12) + String.valueOf(total) + ")");
        //Todo 绑定参与人
        if(users == null || users.size() == 0){
            vActivitiesUser.setVisibility(View.GONE);
            vMyScroll.invalidate();
        }else{
            vActivitiesUser.updateViews(users, total, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParticipantsActivity.startActivity(ActivitiesDetailActivity.this, id);
                }
            });

        }

    }

    @OnClick(R.id.iv_shape)
    public void previewImages() {
        Intent intent = new Intent(this, PhotoViewActivity.class);
        intent.putExtra("currentPosition",0);
        if (activityJson == null)
            return;
        intent.putExtra("imagesList", (Serializable) activityJson.medias);
        this.startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        EventBusFactory.getBus().unregister(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
                geoMarker.setPosition(AMapUtil.convertToLatLng(address
                        .getLatLonPoint()));
                String addressName = getString(R.string.t13) + address.getLatLonPoint() + getString(R.string.t14)
                        + address.getFormatAddress();
                Logger.d("addressName" + addressName);
            } else {
                Toast.makeText(ActivitiesDetailActivity.this, R.string.t15, Toast.LENGTH_LONG).show();
            }
        } else {
            AMapUtil.showerror(this, rCode);
        }
    }



    private ObserverScrollView.OnTopBarShowListener onTopBarShowListener = new ObserverScrollView.OnTopBarShowListener() {
        @Override
        public void onTopBarShow(boolean isShow, float alpha) {
            if(tlTopBar == null){
                return;
            }
            TopBarScrollTransUtils.setTitleBarBg(ActivitiesDetailActivity.this, tlTopBar, isShow);
            if(isShow){
                StatusBarUtil.setTranslucentForImageView(ActivitiesDetailActivity.this, Constants.STATUS_BAR_ALPHA.BAR_ALPHA , null);
                tvTitle.setText(R.string.t16);
                TopBarScrollTransUtils.setLeftImageRightImage(ibBack, R.drawable.white_back, ibHome, R.drawable.ic_share);
            }else{
                StatusBarUtil.setTransparentForImageView(ActivitiesDetailActivity.this , null);
                tvTitle.setText("");
                TopBarScrollTransUtils.setLeftImageRightImage(ibBack, R.drawable.white_back_transparent, ibHome, R.drawable.ic_share_transparent);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("WX_SHARE","WX_SHARE onActivityResult");

    }
}