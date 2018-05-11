package com.atlas.crmapp.usercenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atlas.crmapp.R;
import com.atlas.crmapp.alipay.Alipay;
import com.atlas.crmapp.alipay.WxPay;
import com.atlas.crmapp.coffee.PaySucceedActivity;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.activity.BaseStatusActivity;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.network.BizDataRequest;
import com.atlas.crmapp.network.DcnException;
import com.atlas.crmapp.util.ActionUriUtils;
import com.atlas.crmapp.util.FormatCouponInfo;
import com.atlas.crmapp.util.GlideUtils;
import com.atlas.crmapp.util.StringUtils;
import com.atlas.crmapp.view.MoneyEditText;
import com.atlas.crmapp.view.RechargePayChannelView;
import com.atlas.crmapp.workplace.RentWorkPlaceDetailActivity;
import com.jaeger.library.StatusBarUtil;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechargeActivity extends BaseStatusActivity {

    private String referral;//推荐人
    private String mPayType;
    private String rechargeUrl;

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.iv_recharge_info)
    ImageView ivRechargeInfo;

    @BindView(R.id.et_amount)
    MoneyEditText etAmount;

    @OnClick(R.id.ibBack)
    void onBack() {
        this.finish();
    }

    @BindView(R.id.v_pay_ali)
    RechargePayChannelView vPayAli;

    @BindView(R.id.v_pay_wx)
    RechargePayChannelView vPayWx;

    @BindView(R.id.tv_now_recharge)
    TextView tvNowRecharge;
    @BindView(R.id.tv_hint)
    TextView tvNumPrice;
    @BindView(R.id.tv_yuan)
    TextView tvYuan;


    @OnClick({R.id.v_pay_ali, R.id.v_pay_wx})
    void onClickPayChannel(View view) {
        int id = view.getId();
        if(id == R.id.v_pay_ali){
            isSelectWXPay(false);
        }else if(id == R.id.v_pay_wx){
            isSelectWXPay(true);
        }
    }

    @OnClick(R.id.tv_now_recharge)
    void onCLickNowRecharge() {
        String amount = etAmount.getText().toString().trim();
        if(checkEtAmount(amount)){
            rcCharge(amount);
        }
    }

    private void isSelectWXPay(boolean isSelected){
            mPayType = isSelected ? "WXPAY" :"ALIPAY";
            vPayWx.updateView(isSelected, "微信支付",R.drawable.ic_wx_pay);
            vPayAli.updateView(!isSelected, "支付宝支付", R.drawable.ic_ali_pay);
    }


    @Override
    protected boolean translucentStatusBar() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        referral  = getIntent().getStringExtra("referral");
        ButterKnife.bind(this);
        textViewTitle.setText("");
        isSelectWXPay(false);
        etAmount.setOnTextChanged(new MoneyEditText.OnTextChanged() {
            @Override
            public void onTextChanged() {
                String etAmountStr = etAmount.getText().toString().trim();
                if(StringUtils.isEmpty(etAmountStr)){
                    tvNumPrice.setVisibility(View.VISIBLE);
                }else{
                    tvNumPrice.setVisibility(View.GONE);
                }
                if(checkEtAmount(etAmountStr)){
                    tvNowRecharge.setAlpha(1.0f);
                }else{
                    tvNowRecharge.setAlpha(0.5f);
                }
            }
        });
        tvYuan.setText(FormatCouponInfo.getYuanStr());

        StatusBarUtil.setTransparentForImageView(this, null);
        prepareActivityData();
    }

    @Override
    protected int setTopBar() {
        return NO_SHOW_TOP_BAR;
    }

    @Override
    protected void prepareActivityData() {
        super.prepareActivityData();
        BizDataRequest.requestResource(this, String.valueOf(GlobalParams.getInstance().getAtlasId()), Constants.ResourceConfigUrl.rechargeUri, new BizDataRequest.OnRequestResource() {
            @Override
            public void onSuccess(ResourceJson resourceJson) {
                List<ResourceJson.Row> rows = resourceJson.rows;
                if(rows != null  && rows.size() > 0){
                    final List<ResourceJson.ResourceMedia>  resourceMedias = rows.get(0).resourceMedias;
                    if(resourceMedias != null && resourceMedias.size() > 0){
                        rechargeUrl = resourceMedias.get(0).url;
                        updateActivityViews();
                        ivRechargeInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = ActionUriUtils.getIntent(RechargeActivity.this, resourceMedias.get(0));
                                startActivity(intent);
                            }
                        });
                    }
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
        if(StringUtils.isNotEmpty(rechargeUrl)){
            //Glide.with(this).load(rechargeUrl).into(ivRechargeInfo);
            GlideUtils.loadCustomImageView(this, R.drawable.rechager_banner, rechargeUrl, ivRechargeInfo);
        }else{
            ivRechargeInfo.setVisibility(View.GONE);
        }

    }


    private boolean checkEtAmount(String amount){
        if(StringUtils.isNotEmpty(amount)){
            if(Double.valueOf(amount) == 0){
                //showToast("请输入正确金额");
                return false;
            }else {
                return  true;
            }
        }else{
            //Toast.makeText(RechargeActivity.this,"请输入充值金额",Toast.LENGTH_LONG).show();
            return  false;
        }
    }

    private void rcCharge(String amount) {
        if(referral == null){
            referral ="";
        }
        BizDataRequest.requestRechange(RechargeActivity.this, BigDecimal.valueOf(Double.parseDouble(amount)),referral, new BizDataRequest.OnResponseOpenOrderJson() {
            @Override
            public void onSuccess(ResponseOpenOrderJson responseOpenOrderJson) {
                orderConfirm(responseOpenOrderJson);
            }

            @Override
            public void onError(DcnException error) {

            }
        });
    }


    private void orderConfirm(final ResponseOpenOrderJson confirmOrder) {
        long promoId = -1;
        String promoType = "";
        ResponseOpenOrderJson.PromoScreen promoScreen = confirmOrder.getPromo();
        if( promoScreen != null){
            promoId = promoScreen.getPromoId();
            promoType = promoScreen.getPromoType();
        }
        BizDataRequest.requestConfirmOrder(RechargeActivity.this, confirmOrder.getId(), confirmOrder.getAmount(), confirmOrder.getDiscount(), confirmOrder.getActualAmount(), promoId, promoType, mPayType, 0, 0,new BizDataRequest.OnPayOrderRequestResult() {
            @Override
            public void onSuccess(String jsondata) {
                if(mPayType.equals("ALIPAY")) {
                    Alipay alipay = new Alipay(RechargeActivity.this, confirmOrder);
                    alipay.setListener(mAlipayListener);
                    alipay.startPay();
                }else if(mPayType.equals("WXPAY")){
                    WxPay.wxPay(RechargeActivity.this, jsondata);
                }
            }

            @Override
            public void onError(DcnException error) {

            }
        });

    }


    /**
     * 支付宝支付回调
     */
    private Alipay.OnAlipayListener mAlipayListener = new Alipay.OnAlipayListener() {
        @Override
        public void onSuccess() {
            //Toast.makeText(RechargeActivity.this, "充值成功", Toast.LENGTH_LONG).show();
            paySuccessed();

        }

        @Override
        public void onCancel() {
            //Toast.makeText(RechargeActivity.this, "充值失败", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWait() {

        }
    };

    private void paySuccessed(){
        Intent intent = new Intent(RechargeActivity.this,PaySucceedActivity.class);
        intent.putExtra("amount",Double.valueOf(etAmount.getText().toString().trim()));
        intent.putExtra("type", Constants.ToPaySuccessType.RECHARGE);
        startActivityForResult(intent,RentWorkPlaceDetailActivity.RESULT_CODE);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == PaySucceedActivity.RESULT_COMPELE){
            this.finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MyWalletActivity", "注册广播事件");
        // 注册自定义动态广播消息
        IntentFilter filter_dynamic = new IntentFilter();
        filter_dynamic.addAction("com.atlas.crmapp.paysuccess");
        registerReceiver(dynamicReceiver, filter_dynamic);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(dynamicReceiver);
    }

    private BroadcastReceiver dynamicReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.atlas.crmapp.paysuccess")) {
                int code = intent.getIntExtra("err_code", -2);
                String msg = intent.getStringExtra("err_msg");
                if(code == 0){
                    paySuccessed();
                } else {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }

            }

        }
    };

}
