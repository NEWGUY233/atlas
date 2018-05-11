package com.atlas.crmapp.commonactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atlas.crmapp.R;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.eventbus.Event;
import com.atlas.crmapp.eventbus.EventBusFactory;
import com.atlas.crmapp.model.CouponModel;
import com.atlas.crmapp.model.ResponseMyCodeJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.model.UseableCouponsModel;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.QRCodeUtil;
import com.atlas.crmapp.util.StringUtils;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CouponDetailActivity extends BaseStatusActivity {
    private static final String KEY_COUPON = "KEY_COUPON";
    private static final String KEY_INDEX = "KEY_INDEX";
    @BindView(R.id.tv_coupon_name)
    TextView tvCouponName;
    @BindView(R.id.tv_valid_date)
    TextView tvValidDate;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @BindView(R.id.tv_use_desc)
    TextView tvUseDesc;
    @BindView(R.id.sv_bg)
    View rlCouponBg;
    @BindView(R.id.ll_desc_info)
    LinearLayout llDescInfo;
    @BindView(R.id.iv_coupon_allowuser)
    ImageView ivAllowuser;
    @BindView(R.id.iv_mask_code)
    ImageView ivMaskCode;

    private final String TAG = "CouponDetail";
    private int index;

    private UseableCouponsModel coupons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_coupon_detail);
        ButterKnife.bind(this);


        Intent intent = getIntent();
        coupons = (UseableCouponsModel) intent.getSerializableExtra(KEY_COUPON);
        index = intent.getIntExtra(KEY_INDEX, 0);
        if(coupons != null){
            if(coupons.coupon != null){
                if(coupons.coupon.coupon != null){
                    String title = FormatCouponInfo.getCouponTypeName(coupons.coupon.coupon.targetBizCode);
                    setTitle(title);
                }
            }
        }
        prepareActivityData();
        if(coupons !=  null){
            updateActivityViews();
        }
        EventBusFactory.getBus().register(this);
        findViewById(R.id.top_line).setVisibility(View.INVISIBLE);
    }


    //当跳转至 订单页面时关闭优惠券详细页面
    @Subscribe
    public void onStartOrderFinishThis(ResponseOpenOrderJson openOrderJson){
        if(openOrderJson!= null){
            finish();
        }
    }

    public static void newInstance(Context context, UseableCouponsModel coupon, int index) {
        Intent intent = new Intent(context, CouponDetailActivity.class);
        intent.putExtra(KEY_COUPON, coupon);
        intent.putExtra(KEY_INDEX, index);
        context.startActivity(intent);
    }

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        BizDataRequest.requestCouponcode(this, TAG, coupons.bind.id, new BizDataRequest.OnResponseMyCodeJson() {
            @Override
            public void onSuccess(ResponseMyCodeJson responseMyCodeJson) {
                if (responseMyCodeJson.errorCode == 0) {
                    if(ivQrcode != null){
                        ivQrcode.setImageBitmap(QRCodeUtil.encodeAsBitmap(CouponDetailActivity.this, responseMyCodeJson.data));
                    }
                    Event.CheckConfirmOrder checkConfirmOrder = new Event.CheckConfirmOrder();
                    checkConfirmOrder.timestamp = responseMyCodeJson.timestamp;
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
    protected void updateActivityViews() {
        super.updateActivityViews();
        CouponModel coupon = coupons.coupon.coupon;
        if(coupon != null ){
            tvCouponName.setText(coupon.name);
            tvValidDate.setText(this.getString(R.string.validity_time) + FormatCouponInfo.formatVaildDate(coupons.bind.validStart, coupons.bind.validEnd));
            if(StringUtils.isNotEmpty(coupon.description)){
                tvUseDesc.setText(coupon.description);
                llDescInfo.setVisibility(View.VISIBLE);
            }else{
                llDescInfo.setVisibility(View.GONE);
            }

            if(Constants.CouponUserType.ALLOWUSER.equals(coupons.bind.userType)){
                ivAllowuser.setVisibility(View.VISIBLE);
            }
            rlCouponBg.setBackgroundColor(FormatCouponInfo.getCouponBg(coupons.coupon.coupon.targetBizCode));

            if(index == 1){
                ivMaskCode.setVisibility(View.VISIBLE);
                ivMaskCode.setImageResource(R.drawable.coupon_code_mask_used);
            }else if(index == 2){
                ivMaskCode.setVisibility(View.VISIBLE);
                ivMaskCode.setImageResource(R.drawable.coupon_code_mask_out);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Event.CheckConfirmOrder checkConfirmOrder = new Event.CheckConfirmOrder();//关闭页面后继续请求一分钟
        checkConfirmOrder.isStopCheckThread = true;
        EventBusFactory.getBus().post(checkConfirmOrder);
        EventBusFactory.getBus().unregister(this);
        OkGo.getInstance().cancelTag(TAG);
    }
}
