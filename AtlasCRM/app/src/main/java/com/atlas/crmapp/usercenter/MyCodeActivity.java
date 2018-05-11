package com.atlas.crmapp.usercenter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.ResponseMyCodeJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.QRCodeUtil;
import com.atlas.crmapp.util.ScreenUtils;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * Created by hoda on 2017/9/11.
 */

public class MyCodeActivity extends BaseStatusActivity {

    @BindView(R.id.iv_my_code)
    ImageView ivCode;

    @BindView(R.id.tv_name)
    TextView tvNick;

    @BindView(R.id.tv_company)
    TextView tvCompany;

    @BindView(R.id.iv_avatar)
    ImageView ivHeader;


    private long timestamp;
    private int pauseNum = 0 ;// 轻易点下单，使用APP支付，扫码后，轻易点向APP推送“订单信息-支付”，APP操作取消支付，并放回，APP再次弹出订单

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_code);
        EventBusFactory.getBus().register(this);
        initActivityViews();
    }

    @Override
    protected boolean translucentStatusBar() {
        return true;
    }

    @Override
    protected void initActivityViews() {
        super.initActivityViews();
        initToolbar();
        toolbar.setBackgroundResource(R.color.transparent);
        setTitle(getString(R.string.my_code));
        prepareActivityData();
        ScreenUtils.setScreenBrightness(this, 255);
        FormatCouponInfo.setUserHeadAndNameAndCompany(this, tvNick, null, ivHeader);
    }

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();

        BizDataRequest.requestMyCode(this, new BizDataRequest.OnResponseMyCodeJson() {
            @Override
            public void onSuccess(ResponseMyCodeJson responseMyInfoJson) {
                timestamp = responseMyInfoJson.timestamp;
                if (responseMyInfoJson.errorCode == 0) {
                    Bitmap coderBitmap = QRCodeUtil.encodeAsBitmap(responseMyInfoJson.data, 500, 500 ,1);
                    if(ivCode != null){
                        ivCode.setImageBitmap(coderBitmap);
                    }
                    Event.CheckConfirmOrder checkConfirmOrder = new Event.CheckConfirmOrder();
                    checkConfirmOrder.timestamp = responseMyInfoJson.timestamp;
                    checkConfirmOrder.isStopCheckThread = false;
                    EventBusFactory.getBus().post(checkConfirmOrder);
                }
            }

            @Override
            public void onError(DcnException error) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseNum = pauseNum + 1 ;
    }


    @Subscribe
    public void startOrderConfirmActivity(ResponseOpenOrderJson openOrderJson){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScreenUtils.setWindowBrightness(this, WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE);
        Event.CheckConfirmOrder checkConfirmOrder = new Event.CheckConfirmOrder();//关闭页面后继续请求一分钟
        checkConfirmOrder.isStopCheckThread = true;
        EventBusFactory.getBus().post(checkConfirmOrder);
        EventBusFactory.getBus().unregister(this);


    }
}
