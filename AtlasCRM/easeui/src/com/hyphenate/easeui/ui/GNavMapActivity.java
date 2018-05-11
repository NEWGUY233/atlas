package com.hyphenate.easeui.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.map.AMapUtil;
import com.kaopiz.kprogresshud.KProgressHUD;

//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;

public class GNavMapActivity extends EaseBaseActivity implements GeocodeSearch.OnGeocodeSearchListener {

//    @BindView(R.id.map)
//    MapView mMapView;
//
//    @BindView(R.id.textViewTitle)
//    TextView textViewTitle;
//
//    @BindView(R.id.tvText)
//    TextView textViewSend;

    MapView mMapView;
    TextView textViewTitle;
    ImageButton backButton;
    TextView sendButton;

    private boolean isShow;
    private double lat;
    private double lng;
    private AMap aMap;
    private Marker geoMarker;
    private GeocodeSearch geocoderSearch;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private KProgressHUD dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnavmap);
//        ButterKnife.bind(this);
        mMapView = (MapView)findViewById(R.id.map);
        textViewTitle = (TextView)findViewById(R.id.textViewTitle);
        backButton = (ImageButton)findViewById(R.id.ibBack);
        sendButton = (TextView)findViewById(R.id.ibSend);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lat = aMap.getMyLocation().getLatitude();
                lng = aMap.getMyLocation().getLongitude();
                dialog = showLoading(GNavMapActivity.this, "搜索地址...");
                getAddress(new LatLonPoint(lat, lng));

            }
        });
        textViewTitle.setText("位置信息");
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        isShow = getIntent().getBooleanExtra("show", true);
        if (isShow) {
            sendButton.setVisibility(View.INVISIBLE);
            lat = getIntent().getDoubleExtra("latitude", 0);
            lng = getIntent().getDoubleExtra("longitude", 0);
            aMap.clear();
            geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 30));
            geoMarker.setPosition(new LatLng(lat, lng));
        } else {
            setUpMap();
            geocoderSearch = new GeocodeSearch(this);
            geocoderSearch.setOnGeocodeSearchListener(this);
            sendButton.setVisibility(View.VISIBLE);
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 500));
        }
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    private void setUpMap() {
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        setupLocationStyle();
    }

    /**
     * 设置自定义定位蓝点
     */
    private void setupLocationStyle(){
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000);//设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
        dismissLoading(dialog);
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                String addressName = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                lat = regeocodeResult.getRegeocodeQuery().getPoint().getLatitude();
                lng = regeocodeResult.getRegeocodeQuery().getPoint().getLongitude();
                //发送坐标事件
                Log.d("发送的地理位置" , "lat " + lat +" lng"+ lng +"  addressName"+ addressName);
                setResult(RESULT_OK, new Intent().putExtra("latitude", lat).putExtra("longitude", lng).putExtra("address", addressName));
                finish();
            } else {
                Toast.makeText(GNavMapActivity.this, "搜索不到地址", Toast.LENGTH_LONG).show();
            }
        } else {
            AMapUtil.showerror(this, rCode);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {

    }

    public static KProgressHUD showLoading(Context context, String message) {
//        ProgressDialog dialog = null;
//        if (dialog != null && dialog.isShowing()) return dialog;
//        dialog = new ProgressDialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setMessage(message);
//        dialog.show();
//        return dialog;
        return KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                //.setLabel("Please wait")
                //.setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    public static void dismissLoading(KProgressHUD dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
